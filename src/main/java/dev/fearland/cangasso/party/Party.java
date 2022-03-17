package dev.fearland.cangasso.party;

import dev.fearland.cangasso.player.role.Role;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import dev.fearland.cangasso.Manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class Party {

  /**
   * O tempo em minutos que demora até deletar uma Party caso todos os jogadores dela estejam offline.
   */
  private static final long MINUTES_UNTIL_DELETE = 5L;
  /**
   * O tempo em minutos que demora até deletar uma Party caso todos os jogadores dela estejam offline.
   */
  private static final long MINUTES_UNTIL_EXPIRE_INVITE = 1L;

  private int slots;
  private boolean isOpen;
  protected PartyPlayer leader;
  protected List<PartyPlayer> members;
  protected Map<String, Long> invitesMap;

  private long lastOnlineTime;

  public Party(String leader, int slots) {
    this.slots = slots;
    this.leader = new PartyPlayer(leader, PartyRole.LEADER);
    this.members = new ArrayList<>();
    this.invitesMap = new ConcurrentHashMap<>();
    this.members.add(this.leader);
  }

  public void setIsOpen(boolean flag) {
    this.isOpen = flag;
  }

  public void invite(Object target) {
    String leader = Role.getColored(this.getLeader());
    this.invitesMap.put(Manager.getName(target).toLowerCase(), System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_EXPIRE_INVITE));
    BaseComponent component = new TextComponent("");
    for (BaseComponent components : TextComponent.fromLegacyText(" \n" + leader + " §econvidou você para uma party!\n")) {
      component.addExtra(components);
    }
    BaseComponent accept = new TextComponent("CLIQUE AQUI");
    accept.setColor(ChatColor.AQUA);
    accept.setBold(true);
    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party aceitar " + this.getLeader()));
    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§7Entrar na party de " + leader + "§7.")));
    component.addExtra(accept);
    for (BaseComponent components : TextComponent.fromLegacyText(" §epara aceitar o convite. \n")) {
      component.addExtra(components);
    }


    Manager.sendMessage(target, component);
  }

  public void reject(String member) {
    this.invitesMap.remove(member.toLowerCase());
    this.leader.sendMessage("§dParty> " + Role.getColored(member) + " §crecusou o convite.");
  }

  public void join(String member) {
    this.broadcast("§dParty> " + Role.getColored(member) + " §eentrou na party.");
    this.members.add(new PartyPlayer(member, PartyRole.MEMBER));
    this.invitesMap.remove(member.toLowerCase());
  }

  public void leave(String member) {
    String leader = this.getLeader();
    this.members.removeIf(pp -> pp.getName().equalsIgnoreCase(member));
    if (this.members.isEmpty()) {
      this.delete();
      return;
    }

    String prefixed = Role.getColored(member);
    if (leader.equals(member)) {
      this.leader = this.members.get(0);
      this.leader.setRole(PartyRole.LEADER);
      this.broadcast("§dParty> " + prefixed + " §etornou-se o líder da party.");
    }
    this.broadcast("§dParty> " + prefixed + " §csaiu da party.");
  }

  public void kick(String member) {
    this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(member)).findFirst().ifPresent(pp -> {
      pp.sendMessage("§dParty> " + Role.getColored(this.getLeader()) + " §cexpulsou você da party.");
      this.members.removeIf(pap -> pap.equals(pp));
    });
  }

  public void transfer(String name) {
    PartyPlayer newLeader = this.getPlayer(name);
    if (newLeader == null) {
      return;
    }
    this.leader.setRole(newLeader.getRole());
    newLeader.setRole(PartyRole.LEADER);
    this.leader = newLeader;
  }

  public void broadcast(String message) {
    this.broadcast(message, false);
  }

  public void broadcast(String message, boolean ignoreLeader) {
    this.members.stream().filter(pp -> !ignoreLeader || !pp.equals(this.leader)).forEach(pp -> pp.sendMessage(message));
  }

  public void update() {
    if (onlineCount() == 0) {
      if (this.lastOnlineTime + (TimeUnit.MINUTES.toMillis(MINUTES_UNTIL_DELETE)) < System.currentTimeMillis()) {
        this.delete();
      }

      return;
    }

    this.lastOnlineTime = System.currentTimeMillis();
    this.invitesMap.entrySet().removeIf(entry -> entry.getValue() < System.currentTimeMillis());
  }

  public abstract void delete();

  public void destroy() {
    this.slots = 0;
    this.leader = null;
    this.members.clear();
    this.members = null;
    this.invitesMap.clear();
    this.invitesMap = null;
    this.lastOnlineTime = 0L;
  }

  public int getSlots() {
    return this.slots;
  }

  public long onlineCount() {
    return this.members.stream().filter(PartyPlayer::isOnline).count();
  }

  public String getLeader() {
    return this.leader.getName();
  }

  public String getName(String name) {
    return this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(name)).map(PartyPlayer::getName).findAny().orElse(name);
  }

  public PartyPlayer getPlayer(String name) {
    return this.members.stream().filter(pp -> pp.getName().equalsIgnoreCase(name)).findAny().orElse(null);
  }

  public boolean isOpen() {
    return this.isOpen;
  }

  public boolean canJoin() {
    return this.members.size() < this.slots;
  }

  public boolean isInvited(String name) {
    return this.invitesMap.containsKey(name.toLowerCase());
  }

  public boolean isMember(String name) {
    return this.members.stream().anyMatch(pp -> pp.getName().equalsIgnoreCase(name));
  }

  public boolean isLeader(String name) {
    return this.leader.getName().equalsIgnoreCase(name);
  }

  public List<PartyPlayer> listMembers() {
    return this.members;
  }
}
