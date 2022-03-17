package dev.fearland.cangasso.bukkit;

import dev.fearland.cangasso.party.Party;

public class BukkitParty extends Party {

  public BukkitParty(String leader, int slots) {
    super(leader, slots);
  }

  @Override
  public void delete() {
    BukkitPartyManager.listParties().remove(this);
    this.destroy();
  }
}
