package dev.fearland.cangasso.database.data.interfaces;

import dev.fearland.cangasso.database.data.DataContainer;

public abstract class AbstractContainer {

  protected DataContainer dataContainer;

  public AbstractContainer(DataContainer dataContainer) {
    this.dataContainer = dataContainer;
  }

  public void gc() {
    this.dataContainer = null;
  }
}
