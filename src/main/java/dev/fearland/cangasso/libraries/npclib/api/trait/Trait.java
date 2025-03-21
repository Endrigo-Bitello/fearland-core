package dev.fearland.cangasso.libraries.npclib.api.trait;

public interface Trait {
  
  /**
   * chamado ao Rastreio ser adicionado.
   */
  public void onAttach();
  
  /**
   * chamado ao Rastreio ser removido.
   */
  public void onRemove();
  
  /**
   * chamado ao NPC ser spawnado.
   */
  public void onSpawn();
  
  /**
   * chamado ao NPC ser despawnado.
   */
  public void onDespawn();
}
