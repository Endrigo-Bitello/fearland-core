package dev.fearland.cangasso.menus.profile;

import dev.fearland.cangasso.achievements.Achievement;
import dev.fearland.cangasso.achievements.types.MurderAchievement;
import dev.fearland.cangasso.achievements.types.SkyWarsAchievement;
import dev.fearland.cangasso.achievements.types.TheBridgeAchievement;
import dev.fearland.cangasso.libraries.menu.PlayerMenu;
import dev.fearland.cangasso.menus.profile.achievements.MenuAchievementsList;
import dev.fearland.cangasso.player.Profile;
import dev.fearland.cangasso.utils.BukkitUtils;
import dev.fearland.cangasso.utils.enums.EnumSound;
import dev.fearland.cangasso.achievements.types.BedWarsAchievement;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import dev.fearland.cangasso.Core;
import dev.fearland.cangasso.menus.MenuProfile;

import java.util.List;

public class MenuAchievements extends PlayerMenu {

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
            if (evt.getSlot() == 10) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Sky Wars", SkyWarsAchievement.class);
            } else if (evt.getSlot() == 12) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "The Bridge", TheBridgeAchievement.class);
            } else if (evt.getSlot() == 14) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Murder", MurderAchievement.class);
            } else if (evt.getSlot() == 16) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              new MenuAchievementsList<>(profile, "Bed Wars", BedWarsAchievement.class);
            } else if (evt.getSlot() == 31) {
              EnumSound.CLICK.play(this.player, 0.5F, 2.0F);
              player.closeInventory();
            }
          }
        }
      }
    }
  }

  public MenuAchievements(Profile profile) {
    super(profile.getPlayer(), "Desafios", 4);

    List<SkyWarsAchievement> skywars = Achievement.listAchievements(SkyWarsAchievement.class);
    long max = skywars.size();
    long completed = skywars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    String color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    skywars.clear();
    this.setItem(10, BukkitUtils.deserializeItemStack("GRASS : 1 : nome>&aSky Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));

    List<TheBridgeAchievement> thebridge = Achievement.listAchievements(TheBridgeAchievement.class);
    max = thebridge.size();
    completed = thebridge.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    thebridge.clear();
    this.setItem(12,
      BukkitUtils.deserializeItemStack("STAINED_CLAY:11 : 1 : nome>&aThe Bridge : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));

    List<MurderAchievement> murder = Achievement.listAchievements(MurderAchievement.class);
    max = murder.size();
    completed = murder.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    murder.clear();
    this.setItem(14, BukkitUtils.deserializeItemStack("BOW : 1 : nome>&aMurder : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));

    List<BedWarsAchievement> bedwars = Achievement.listAchievements(BedWarsAchievement.class);
    max = bedwars.size();
    completed = bedwars.stream().filter(achievement -> achievement.isCompleted(profile)).count();
    color = (completed == max) ? "&a" : (completed > max / 2) ? "&7" : "&c";
    bedwars.clear();
    this.setItem(16, BukkitUtils.deserializeItemStack("BED : 1 : nome>&aBed Wars : desc>&fDesafios: " + color + completed + "/" + max + "\n \n&eClique para visualizar!"));


    this.setItem(31, BukkitUtils.deserializeItemStack("ARROW : 1 : nome>&cFechar"));

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
