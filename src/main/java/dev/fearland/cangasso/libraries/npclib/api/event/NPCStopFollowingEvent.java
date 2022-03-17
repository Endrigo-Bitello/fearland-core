package dev.fearland.cangasso.libraries.npclib.api.event;

import dev.fearland.cangasso.libraries.npclib.api.npc.NPC;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;

public class NPCStopFollowingEvent extends NPCEvent {

  private NPC npc;
  private Entity target;

  public NPCStopFollowingEvent(NPC npc, Entity target) {
    this.npc = npc;
    this.target = target;
  }

  public NPC getNPC() {
    return npc;
  }

  public Entity getTarget() {
    return target;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  private static final HandlerList HANDLER_LIST = new HandlerList();

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
