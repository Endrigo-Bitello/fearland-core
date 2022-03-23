package dev.fearland.cangasso;

import com.comphenix.protocol.ProtocolLibrary;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import dev.fearland.cangasso.booster.Booster;
import dev.fearland.cangasso.cmd.Commands;
import dev.fearland.cangasso.database.Database;
import dev.fearland.cangasso.hook.KCoreExpansion;
import dev.fearland.cangasso.hook.protocollib.FakeAdapter;
import dev.fearland.cangasso.hook.protocollib.HologramAdapter;
import dev.fearland.cangasso.hook.protocollib.NPCAdapter;
import dev.fearland.cangasso.nms.NMS;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.player.fake.FakeManager;
import dev.fearland.cangasso.player.role.Role;
import dev.fearland.cangasso.plugin.KPlugin;
import dev.fearland.cangasso.plugin.config.KConfig;
import dev.fearland.cangasso.queue.Queue;
import dev.fearland.cangasso.queue.QueuePlayer;
import dev.fearland.cangasso.servers.ServerItem;
import dev.fearland.cangasso.titles.Title;
import dev.fearland.cangasso.utils.SlickUpdater;
import dev.fearland.cangasso.achievements.Achievement;
import dev.fearland.cangasso.deliveries.Delivery;
import dev.fearland.cangasso.libraries.MinecraftVersion;
import dev.fearland.cangasso.libraries.holograms.HologramLibrary;
import dev.fearland.cangasso.libraries.npclib.NPCLibrary;
import dev.fearland.cangasso.listeners.Listeners;
import dev.fearland.cangasso.listeners.PluginMessageListener;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@SuppressWarnings("unchecked")
public class Core extends KPlugin {

  private static Core instance;
  public static boolean validInit;
  public static final List<String> warnings = new ArrayList<>();
  public static final List<String> minigames = Arrays.asList("Sky Wars", "The Bridge", "Murder", "Bed Wars", "Build Battle");
  public static String minigame = "";

  @Override
  public void start() {
    instance = this;
  }

  @Override
  public void load() {}

  @Override
  public void enable() {
    if (!NMS.setupNMS()) {
      this.setEnabled(false);
      this.getLogger().warning("Sua versao nao e compativel com o plugin, utilize a versao 1_8_R3 (Atual: " + MinecraftVersion.getCurrentVersion().getVersion() + ")");
      return;
    }

    saveDefaultConfig();
    lobby = Bukkit.getWorlds().get(0).getSpawnLocation();

    // Remover o spawn-protection-size
    if (Bukkit.getSpawnRadius() != 0) {
      Bukkit.setSpawnRadius(0);
    }

    // Plugins que causaram incompatibilidades
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getResource("blacklist.txt"), StandardCharsets.UTF_8))) {
      String plugin;
      while ((plugin = reader.readLine()) != null) {
        if (Bukkit.getPluginManager().getPlugin(plugin.split(" ")[0]) != null) {
          warnings.add(" - " + plugin);
        }
      }
    } catch (IOException ex) {
      getLogger().log(Level.SEVERE, "Cannot load blacklist.txt: ", ex);
    }

    if (!warnings.isEmpty()) {
      CommandSender sender = Bukkit.getConsoleSender();
      StringBuilder sb = new StringBuilder(" \n §6§lAVISO IMPORTANTE\n \n §7Aparentemente você utiliza plugins que conflitam com o kCore.\n §7Você não poderá iniciar o servidor com os seguintes plugins:");
      for (String warning : warnings) {
        sb.append("\n§f").append(warning);
      }
      sb.append("\n ");
      sender.sendMessage(sb.toString());
      System.exit(0);
      return;
    }


    // Remover /reload
    try {
      SimpleCommandMap simpleCommandMap = (SimpleCommandMap) Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer());
      Field field = simpleCommandMap.getClass().getDeclaredField("knownCommands");
      field.setAccessible(true);
      Map<String, Command> knownCommands = (Map<String, Command>) field.get(simpleCommandMap);

      //RELOAD
      knownCommands.remove("rl");
      knownCommands.remove("reload");
      knownCommands.remove("bukkit:rl");
      knownCommands.remove("bukkit:reload");

      //PL ETC
      knownCommands.remove("pl");
      knownCommands.remove("plugins");
      knownCommands.remove("ver");
      knownCommands.remove("version");
      knownCommands.remove("help");
      knownCommands.remove("?");
      knownCommands.remove("bukkit:pl");
      knownCommands.remove("bukkit:plugins");
      knownCommands.remove("bukkit:ver");
      knownCommands.remove("bukkit:version");
      knownCommands.remove("bukkit:help");
      knownCommands.remove("bukkit:?");
      knownCommands.remove("about");
      knownCommands.remove("bukkit:about");
    } catch (ReflectiveOperationException ex) {
      getLogger().log(Level.SEVERE, "Cannot remove reload command: ", ex);
    }

    if (!PlaceholderAPIPlugin.getInstance().getDescription().getVersion().equals("2.10.5")) {
      Bukkit.getConsoleSender().sendMessage(" \n §6§lAVISO IMPORTANTE\n \n §7Utilize a versão 2.10.5 do PlaceHolderAPI, você está utilizando a v" + PlaceholderAPIPlugin.getInstance().getDescription().getVersion() + "\n ");
      System.exit(0);
      return;
    }

    PlaceholderAPI.registerExpansion(new KCoreExpansion());

    Database.setupDatabase(
      getConfig().getString("database.tipo"), //TIPO: MySQL, Hikari ou Mongo
      getConfig().getString("database.mysql.host"), //IP
      getConfig().getString("database.mysql.porta"), //Porta
      getConfig().getString("database.mysql.nome"), //Database
      getConfig().getString("database.mysql.usuario"), //Usuário
      getConfig().getString("database.mysql.senha"), //Senha

      getConfig().getBoolean("database.mysql.hikari",false),
      getConfig().getBoolean("database.mysql.mariadb", false),
      getConfig().getString("database.mongodb.url", "")
    );

    NPCLibrary.setupNPCs(this);
    HologramLibrary.setupHolograms(this);

    setupRoles();
    FakeManager.setupFake();
    Title.setupTitles();
    Booster.setupBoosters();
    Delivery.setupDeliveries();
    ServerItem.setupServers();
    Achievement.setupAchievements();

    Commands.setupCommands();
    Listeners.setupListeners();

    ProtocolLibrary.getProtocolManager().addPacketListener(new FakeAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new NPCAdapter());
    ProtocolLibrary.getProtocolManager().addPacketListener(new HologramAdapter());

    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    getServer().getMessenger().registerOutgoingPluginChannel(this, "kCore");
    getServer().getMessenger().registerIncomingPluginChannel(this, "kCore", new PluginMessageListener());

   // Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> new SlickUpdater(this, 2).run());

    validInit = true;
    this.getLogger().info("O plugin foi ativado.");
  }

  @Override
  public void disable() {
    if (validInit) {
      Bukkit.getOnlinePlayers().forEach(player -> {
        Profile profile = Profile.unloadProfile(player.getName());
        if (profile != null) {
          profile.saveSync();
          profile.save();
          this.getLogger().info("O jogador " + profile.getName() + " foi salvo!");
          profile.destroy();
        }
      });
      Database.getInstance().close();
    }

    File update = new File("plugins/kCore/update", "kCore.jar");
    if (update.exists()) {
      try {
        this.getFileUtils().deleteFile(new File("plugins/kCore.jar"));
        this.getFileUtils().copyFile(new FileInputStream(update), new File("plugins/kCore.jar"));
        this.getFileUtils().deleteFile(update.getParentFile());
        this.getLogger().info("Update do nCore aplicada.");
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    this.getLogger().info("O plugin foi desativado.");
  }

  private void setupRoles() {
    KConfig config = getConfig("roles");
    for (String key : config.getSection("roles").getKeys(false)) {
      String name = config.getString("roles." + key + ".name");
      String prefix = config.getString("roles." + key + ".prefix");
      String permission = config.getString("roles." + key + ".permission");
      boolean broadcast = config.getBoolean("roles." + key + ".broadcast", true);
      boolean broadcastplus = config.getBoolean("roles." + key + ".broadcastplus", true);
      boolean alwaysVisible = config.getBoolean("roles." + key + ".alwaysvisible", false);

      Role.listRoles().add(new Role(name, prefix, permission, alwaysVisible, broadcast, broadcastplus));
    }

    if (Role.listRoles().isEmpty()) {
      Role.listRoles().add(new Role("&7Membro", "&7", "", false, false, false));
    }
  }

  private static Location lobby;

  public static void setLobby(Location location) {
    lobby = location;
  }

  public static Location getLobby() {
    return lobby;
  }

  public static Core getInstance() {
    return instance;
  }

  public static void sendServer(Profile profile, String name) {
    if (!Core.getInstance().isEnabled()) {
      return;
    }

    Player player = profile.getPlayer();
    if (Core.getInstance().getConfig("utils").getBoolean("queue")) {
      if (player != null) {
        player.closeInventory();
        Queue queue = player.hasPermission("core.queue") ? Queue.VIP : Queue.MEMBER;
        QueuePlayer qp = queue.getQueuePlayer(player);
        if (qp != null) {
          if (qp.server.equalsIgnoreCase(name)) {
            qp.player.sendMessage("§cVocê já está na fila de conexão!");
          } else {
            qp.server = name;
          }
          return;
        }

        queue.queue(player, profile, name);
      }
    } else {
      if (player != null) {
        Bukkit.getScheduler().runTask(Core.getInstance(), () -> {
          if (player.isOnline()) {
            player.closeInventory();
            NMS.sendActionBar(player, "");
            profile.saveSync();
            player.sendMessage("§aConectando...");
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(name);
            player.sendPluginMessage(Core.getInstance(), "BungeeCord", out.toByteArray());
          }
        });
      }
    }
  }
}
