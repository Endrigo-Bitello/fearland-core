package dev.fearland.cangasso.cmd;

import dev.fearland.cangasso.Manager;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.player.fake.FakeManager;
import dev.fearland.cangasso.player.role.Role;
import dev.fearland.cangasso.utils.enums.EnumSound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static dev.fearland.cangasso.player.fake.FakeManager.ALEX;
import static dev.fearland.cangasso.player.fake.FakeManager.STEVE;

public class FakeCommand extends Commands {

  public FakeCommand() {
    super("fake", "faker", "fakel");
  }

  @Override
  public void perform(CommandSender sender, String label, String[] args) {
    if (!(sender instanceof Player)) {
      sender.sendMessage("§cApenas jogadores podem utilizar este comando.");
      return;
    }

    Player player = (Player) sender;
    if (!player.hasPermission("kcore.cmd.fake") || (label.equalsIgnoreCase("fakel") && !player.hasPermission("kcore.cmd.fakelist"))) {
      player.sendMessage("§cVocê não possui autorização.");
      return;
    }

    Profile profile = Profile.getProfile(player.getName());
    if (label.equalsIgnoreCase("fake")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cVocê não pode utilizar um fake enquanto joga.");
        return;
      }

      if (FakeManager.getRandomNicks().stream().noneMatch(FakeManager::isUsable)) {
        player.sendMessage(" \n §c§lNENHUM DISFARCE DISPONÍVEL!\n \n §cNão encontramos nenhum apelido para você utilizar, tente novamente mais tarde.\n ");
        return;
      }

      if (args.length == 0) {
        FakeManager.sendRole(player);
        return;
      }

      String roleName = args[0];
      if (!FakeManager.isFakeRole(roleName)) {
        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
        FakeManager.sendRole(player);
        return;
      }

      if (Role.getRoleByName(roleName) == null) {
        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
        FakeManager.sendRole(player);
        return;
      }

      if (args.length == 1) {
        EnumSound.ORB_PICKUP.play(player, 1.0F, 2.0F);
        FakeManager.sendSkin(player, roleName);
        return;
      }

      String skin = args[1];
      if (!skin.equalsIgnoreCase("alex") && !skin.equalsIgnoreCase("steve") && !skin.equalsIgnoreCase("you")) {
        EnumSound.VILLAGER_NO.play(player, 1.0F, 1.0F);
        FakeManager.sendSkin(player, roleName);
        return;
      }

      List<String> enabled = FakeManager.getRandomNicks().stream().filter(FakeManager::isUsable).collect(Collectors.toList());
      String fakeName = enabled.isEmpty() ? null : enabled.get(ThreadLocalRandom.current().nextInt(enabled.size()));
      if (fakeName == null) {
        player.sendMessage(" \n §c§lNENHUM DISFARCE DISPONÍVEL!\n \n §cNão encontramos nenhum apelido para você utilizar, tente novamente mais tarde.\n ");
        return;
      }

      enabled.clear();
      FakeManager.applyFake(player, fakeName, roleName, skin.equalsIgnoreCase("steve") ? STEVE : skin.equalsIgnoreCase("you") ? Manager.getSkin(player.getName(), true) : ALEX);
    } else if (label.equalsIgnoreCase("faker")) {
      if (profile != null && profile.playingGame()) {
        player.sendMessage("§cVocê não pode utilizar este comando no momento.");
        return;
      }

      if (!FakeManager.isFake(player.getName())) {
        player.sendMessage("§cVocê não está utilizando um nickname falso.");
        return;
      }

      FakeManager.removeFake(player);
    } else {
      List<String> nicked = FakeManager.listNicked();
      StringBuilder sb = new StringBuilder();
      for (int index = 0; index < nicked.size(); index++) {
        sb.append("§c").append(nicked.get(index)).append(" §fé na verdade ").append("§akcorefakereal:").append(nicked.get(index)).append(index + 1 == nicked.size() ? "" : "\n");
      }

      nicked.clear();
      if (sb.length() == 0) {
        sb.append("§cNão há nenhum usuário utilizando um nickname falso.");
      }

      player.sendMessage(" \n§eLista de nicknames falsos:\n \n" + sb + "\n ");
    }
  }
}
