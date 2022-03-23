package dev.fearland.cangasso.menus;

import dev.fearland.cangasso.booster.Booster;
import dev.fearland.cangasso.booster.NetworkBooster;
import dev.fearland.cangasso.libraries.menu.UpdatablePlayerMenu;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.player.role.Role;
import dev.fearland.cangasso.servers.ServerItem;
import dev.fearland.cangasso.utils.BukkitUtils;
import dev.fearland.cangasso.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import dev.fearland.cangasso.Core;

public class MenuServers extends UpdatablePlayerMenu {

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
            if (ServerItem.DISABLED_SLOTS.contains(evt.getSlot())) {
              this.player.sendMessage("§cVocê já está conectado a este servidor.");
              return;
            }

            ServerItem.listServers().stream().filter(s -> s.getSlot() == evt.getSlot()).findFirst().ifPresent(serverItem -> serverItem.connect(profile));
          }
        }
      }
    }
  }

  public MenuServers(Profile profile) {
    super(profile.getPlayer(), ServerItem.CONFIG.getString("title"), ServerItem.CONFIG.getInt("rows"));

    this.update();
    this.register(Core.getInstance(), 20);
    this.open();

    /*String booster = profile.getBoostersContainer().getEnabled();
    StringBuilder result = new StringBuilder(), network = new StringBuilder();
    for (int index = 0; index < Core.minigames.size(); index++) {
      String minigame = Core.minigames.get(index);
      NetworkBooster nb = Booster.getNetworkBooster(minigame);
      }*/

  }

  @Override
  public void update() {

    for(ServerItem serverItem : ServerItem.listServers()) {
      StringBuilder resultsky = new StringBuilder(), networksky = new StringBuilder();
      String sky = Core.minigames.get(0);
      NetworkBooster nbs = Booster.getNetworkBooster(sky);
      networksky.append(nbs == null ? "" : "\n&7Multiplicador de Coins: &6" + nbs.getMultiplier() + "x\n&8➟ Ativado por: &6" + Role.getColored(nbs.getBooster() + "\n "));

      StringBuilder resultbed = new StringBuilder(), networkbed = new StringBuilder();
      String bed = Core.minigames.get(3);
      NetworkBooster nbb = Booster.getNetworkBooster(bed);
      networkbed.append(nbb == null ? "" : "\n&7Multiplicador de Coins: &6" + nbb.getMultiplier() + "x\n&8➟ Ativado por: &6" + Role.getColored(nbb.getBooster() + "\n "));

      //this.setItem(serverItem.getSlot(),
              //this.setItem(serverItem.getSlot(),
            //  BukkitUtils.deserializeItemStack(serverItem.getIcon().replace("{players}", StringUtils.formatNumber(ServerItem.getServerCount(serverItem))).replace("{booster_skywars}", networksky.toString()).replace("{booster_bedwars}", networkbed.toString());

              this.setItem(serverItem.getSlot(), BukkitUtils.deserializeItemStack(serverItem.getIcon().replace("{players}", StringUtils.formatNumber(ServerItem.getServerCount(
                      serverItem))).replace("{booster_skywars}", networksky.toString()).replace("{booster_bedwars}", networkbed.toString())));


    }
  }

  public void cancel() {
    super.cancel();
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
