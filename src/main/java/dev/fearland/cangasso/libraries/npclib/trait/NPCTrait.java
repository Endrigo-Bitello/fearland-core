package dev.fearland.cangasso.libraries.npclib.trait;

import dev.fearland.cangasso.libraries.npclib.api.npc.NPC;
import dev.fearland.cangasso.libraries.npclib.api.trait.Trait;

public abstract class NPCTrait implements Trait {

  private NPC npc;

  public NPCTrait(NPC npc) {
    this.npc = npc;
  }

  public NPC getNPC() {
    return npc;
  }

  @Override
  public void onAttach() {}

  @Override
  public void onSpawn() {}

  @Override
  public void onDespawn() {}

  @Override
  public void onRemove() {}
}
