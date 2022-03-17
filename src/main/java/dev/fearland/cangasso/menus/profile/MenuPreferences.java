package dev.fearland.cangasso.menus.profile;

import dev.fearland.cangasso.database.data.container.PreferencesContainer;
import dev.fearland.cangasso.libraries.menu.PlayerMenu;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.player.enums.BloodAndGore;
import dev.fearland.cangasso.player.enums.PlayerVisibility;
import dev.fearland.cangasso.player.enums.PrivateMessages;
import dev.fearland.cangasso.player.enums.ProtectionLobby;
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
            if (evt.getSlot() == 10 || evt.getSlot() == 11 || evt.getSlot() == 12 || evt.getSlot() == 14 || evt.getSlot() == 15 || evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
            } else if (evt.getSlot() == 20) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePlayerVisibility();
              if (!profile.playingGame()) {
                profile.refreshPlayers();
              }
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 21) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changePrivateMessages();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 23) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeBloodAndGore();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 24) {
              EnumSound.ITEM_PICKUP.play(this.player, 0.5F, 2.0F);
              profile.getPreferencesContainer().changeProtectionLobby();
              new MenuPreferences(profile);
            } else if (evt.getSlot() == 40) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuProfile(profile);
            }
          }
        }
      }
    }
  }

  public MenuPreferences(Profile profile) {
    super(profile.getPlayer(), "Preferências", 5);

    PreferencesContainer pc = profile.getPreferencesContainer();

    this.setItem(9, BukkitUtils.deserializeItemStack("FEATHER : 1 : nome>&aVoar : desc>&7Decida se quer manter o modo\n&7voar habilitado nos lobbies."));
    this.setItem(18, BukkitUtils.deserializeItemStack("INK_SACK:10 : 1 : nome>&aAtivado : desc>&fEstado &aAtivado\n \n&7Exclusivo para &aVIP&7.\n \n&eClique para modificar!"));

    PlayerVisibility pv = pc.getPlayerVisibility();
    this.setItem(10, BukkitUtils.deserializeItemStack("347 : 1 : nome>&aJogadores : desc>&7Ative ou desative os\n&7jogadores no lobby."));
    this.setItem(19, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pv.getInkSack() + " : 1 : nome>" + pv.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pv.getName()) + "\n \n&eClique para modificar!"));

    PrivateMessages pm = pc.getPrivateMessages();
    this.setItem(11, BukkitUtils.deserializeItemStack("358:2 : 1 : esconder>tudo : nome>&aMensagens privadas : desc>&7Ative ou desative as mensagens\n&7enviadas através do tell."));
    this.setItem(20, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pm.getInkSack() + " : 1 : nome>" + pm.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pm.getName()) + "\n \n&eClique para modificar!"));

    ProtectionLobby pl = pc.getProtectionLobby();
    this.setItem(12, BukkitUtils.deserializeItemStack("NETHER_STAR : 1 : nome>&aProteção no /lobby : desc>&7Ative ou desative o pedido de\n&7confirmação ao utilizar /lobby."));
    this.setItem(21, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + pl.getInkSack() + " : 1 : nome>" + pl.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(pl.getName()) + "\n \n&eClique para modificar!"));

    BloodAndGore bg = pc.getBloodAndGore();
    this.setItem(13, BukkitUtils.deserializeItemStack("SKULL_ITEM:3 : 1 : nome>&aSangue : desc>&7Ative ou desative as partículas\n&7de sangue no PvP. : skin>eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjNiYzBjNWVhMTg2YzI3YTY0ZjZjMjBkYTE5ODU1ZDJlMDkxMGQ0OTVmMDk4NWJmYjAxY2EzYzdkMTUxNGNhNyJ9fX0="));
    this.setItem(22, BukkitUtils.deserializeItemStack(
            "INK_SACK:" + bg.getInkSack() + " : 1 : nome>" + bg.getName() + " : desc>&fEstado: &7" + StringUtils.stripColors(bg.getName()) + "\n \n&eClique para modificar!"));


    this.setItem(14, BukkitUtils.deserializeItemStack("BANNER:9 : 1 : nome>&aConvites de party : desc>&7Ative ou desative os convites\n&7para parties."));
    this.setItem(23, BukkitUtils.deserializeItemStack("INK_SACK:10 : 1 : nome>&aAtivado : desc>&fEstado &aAtivado\n\n&eClique para modificar!"));

    this.setItem(15, BukkitUtils.deserializeItemStack("LEATHER_CHESTPLATE : 1 : pintar>BLUE : esconder>tudo : nome>&aSolicitações de amizade : desc>&7Ative ou desative os convites\n&7de amizade de outros jogadores."));
    this.setItem(24, BukkitUtils.deserializeItemStack("INK_SACK:10 : 1 : nome>&aAtivado : desc>&fEstado: &aAtivado\n \n&eClique para modificar!"));

    this.setItem(16, BukkitUtils.deserializeItemStack("ITEM_FRAME : 1 : nome>&aConvites de clans : desc>&7Ative ou desative o convite\n&7de membros de clans."));
    this.setItem(25, BukkitUtils.deserializeItemStack("INK_SACK:10 : 1 : nome>&aAtivado : desc>&fEstado: &aAtivado\n \n&eClique para modificar!"));

    this.setItem(17, BukkitUtils.deserializeItemStack("JACK_O_LANTERN : 1 : nome>&aAtividade de amigos : desc>&7Ative ou desative alertas de\n&7entrada ou saída dos amigos."));
    this.setItem(26, BukkitUtils.deserializeItemStack("INK_SACK:10 : 1 : nome>&aAtivado : desc>&fEstado: &aAtivado\n \n&eClique para modificar!"));



    this.setItem(40, BukkitUtils.deserializeItemStack("INK_SACK:1 : 1 : nome>&cVoltar : desc>&7Para o Perfil"));

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
