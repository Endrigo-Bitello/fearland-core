package dev.fearland.cangasso.menus.profile;

import dev.fearland.cangasso.database.data.container.PreferencesContainer;
import dev.fearland.cangasso.libraries.menu.PlayerMenu;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.player.enums.*;
import dev.fearland.cangasso.utils.BukkitUtils;
import dev.fearland.cangasso.utils.StringUtils;
import dev.fearland.cangasso.utils.enums.EnumSound;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import dev.fearland.cangasso.Core;
import dev.fearland.cangasso.menus.MenuProfile;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class MenuPreferences extends PlayerMenu {

  @EventHandler
  public void onInventoryClick(InventoryClickEvent evt) {
    if (evt.getInventory().equals(this.getInventory())) {
      evt.setCancelled(true);

      if (evt.getWhoClicked().equals(this.player)) {
        Profile profile = Profile.getProfile(this.player.getName());
        if (profile == null) {
          this.player.closeInventory();
          return;
        }

        if (evt.getClickedInventory() != null && evt.getClickedInventory().equals(this.getInventory())) {
          ItemStack item = evt.getCurrentItem();

          if (item != null && item.getType() != Material.AIR) {
            if (evt.getSlot() == 1 || evt.getSlot() == 2 || evt.getSlot() == 3 || evt.getSlot() == 4 || evt.getSlot() == 5 || evt.getSlot() == 6 || evt.getSlot() == 7 || evt.getSlot() == 28|| evt.getSlot() == 29 || evt.getSlot() == 30) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 10) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePlayerVisibility();
              if (!profile.playingGame()) {
                profile.refreshPlayers();
              }
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 11) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePrivateMessages();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 12) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeBloodAndGore();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 13) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeProtectionLobby();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 14) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              if (player.hasPermission("kcore.fly")) {
                profile.getPreferencesContainer().changeFly();
                new MenuPreferences(profile);
                final BukkitTask runnable = new BukkitRunnable() {
                  @Override
                  public void run() {
                    if (profile.getPreferencesContainer().getFly() == Fly.ATIVADO) {
                      player.setAllowFlight(true);
                    } else if (profile.getPreferencesContainer().getFly() == Fly.DESATIVADO) {
                      player.setAllowFlight(false);
                    }
                    cancel();
                  }
                }.runTaskLater(Core.getInstance(), 1);
              } else {
                player.sendMessage("§cVocê não possui permissão para fazer isso.");
                EnumSound.ENDERMAN_TELEPORT.play(player, 1.0F, 0.5F);
              }
            } else if (evt.getSlot() == 15) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeMention();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 16) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeEntry();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 37) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeClanRequest();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 38) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePartyRequest();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 49) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            }
          }
        }
      }
    }
  }

  public MenuPreferences(Profile profile) {
    super(profile.getPlayer(), "Preferências", 6);

    PreferencesContainer pc = profile.getPreferencesContainer();

    PlayerVisibility pv = pc.getPlayerVisibility();
    this.setItem(1, BukkitUtils.deserializeItemStack("347 : 1 : nome>&aJogadores : desc>&7Ative ou desative os\n&7jogadores no lobby."));
    this.setItem(10, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pv.getInkSack() + " : 1 : nome>" + pv.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pv.getName()) + "\n \n&eClique para modificar!"));

    PrivateMessages pm = pc.getPrivateMessages();
    this.setItem(2, BukkitUtils.deserializeItemStack("PAPER : 1 : nome>&aMensagens privadas : desc>&7Ative ou desative as mensagens\n&7enviadas através do tell."));
    this.setItem(11, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pm.getInkSack() + " : 1 : nome>" + pm.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pm.getName()) + "\n \n&eClique para modificar!"));

    BloodAndGore bg = pc.getBloodAndGore();
    this.setItem(3, BukkitUtils.deserializeItemStack("REDSTONE : 1 : nome>&aViolência : desc>&7Ative ou desative as partículas\n&7de sangue no PvP."));
    this.setItem(12, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + bg.getInkSack() + " : 1 : nome>" + bg.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(bg.getName()) + "\n \n&eClique para modificar!"));

    ProtectionLobby pl = pc.getProtectionLobby();
    this.setItem(4, BukkitUtils.deserializeItemStack("NETHER_STAR : 1 : nome>&aProteção no /lobby : desc>&7Ative ou desative o pedido de\n&7confirmação ao utilizar /lobby."));
    this.setItem(13, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pl.getInkSack() + " : 1 : nome>" + pl.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pl.getName()) + "\n \n&eClique para modificar!"));

    if (player.hasPermission("kcore.fly")) {
      Fly fl = pc.getFly();
      this.setItem(5, BukkitUtils.deserializeItemStack("FEATHER : 1 : nome>&aModo Fly : desc>&7Ative ou desative o modo voar."));
      this.setItem(14, BukkitUtils.deserializeItemStack(
              "INK_SACK:" + fl.getInkSack() + " : 1 : nome>" + fl.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(fl.getName()) + "\n \n&eClique para modificar!"));
    } else {
      this.setItem(5, BukkitUtils.deserializeItemStack("FEATHER : 1 : nome>&aModo Fly : desc>&7Ative ou desative o modo voar."));
      this.setItem(14, BukkitUtils.deserializeItemStack("INK_SACK:8 : 1 : nome>&cDesativado : desc>&fEstado: &7Desativado \n \n&cÉ necessário ter &aVIP &cpara fazer isso."));
    }

    Mention mt = pc.getMention();
    this.setItem(6, BukkitUtils.deserializeItemStack("358:0 : 1 : esconder>tudo : nome>&aMenção no Chat : desc>&7Ative ou desative a menção no\n&7chat."));
    this.setItem(15, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + mt.getInkSack() + " : 1 : nome>" + mt.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(mt.getName()) + "\n \n&eClique para modificar!"));

    AnnounceEntry ae = pc.getAnnounceEntry();
    this.setItem(7, BukkitUtils.deserializeItemStack("386 : 1 : esconder>tudo : nome>&aAnunciar entrada no lobby : desc>&7Ative ou desative a sua entrada no\n&7lobby."));
    this.setItem(16, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + ae.getInkSack() + " : 1 : nome>" + ae.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(ae.getName()) + "\n \n&eClique para modificar!"));

    ClanRequest cr = pc.getClanRequest();
    this.setItem(28, BukkitUtils.deserializeItemStack("DIAMOND_SWORD : 1 : esconder>tudo : nome>&aPedido de clans : desc>&7Ative ou desative pedidos de clans."));
    this.setItem(37, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + cr.getInkSack() + " : 1 : nome>" + cr.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(cr.getName()) + "\n \n&eClique para modificar!"));

    PartyRequest fr = pc.getPartyRequest();
    this.setItem(29, BukkitUtils.deserializeItemStack("38 : 1 : esconder>tudo : nome>&aPedido de party : desc>&7Ative ou desative pedidos de party."));
    this.setItem(38, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + fr.getInkSack() + " : 1 : nome>" + fr.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(fr.getName()) + "\n \n&eClique para modificar!"));

    this.setItem(49, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cVoltar"));

    this.register(Core.getInstance());
    this.open();
  }

  public void cancel() {
    HandlerList.unregisterAll(this);
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent evt) {
    if (evt.getPlayer().equals(this.player)) {
      this.cancel();
    }
  }

  @EventHandler
  public void onInventoryClose(InventoryCloseEvent evt) {
    if (evt.getPlayer().equals(this.player) && evt.getInventory().equals(this.getInventory())) {
      this.cancel();
    }
  }
}