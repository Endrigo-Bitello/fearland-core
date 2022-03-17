package dev.fearland.cangasso.libraries.npclib.api.event;

import dev.fearland.cangasso.libraries.npclib.api.npc.NPC;
import org.bukkit.event.HandlerList;

public class NPCNeedsRespawnEvent extends NPCEvent {

  private NPC npc;

  public NPCNeedsRespawnEvent(NPC npc) {
    this.npc = npc;
  }

  public NPC getNPC() {
    return npc;
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
