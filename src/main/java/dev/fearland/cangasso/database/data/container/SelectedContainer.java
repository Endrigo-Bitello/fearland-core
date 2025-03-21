package dev.fearland.cangasso.database.data.container;

import dev.fearland.cangasso.database.data.interfaces.AbstractContainer;
import dev.fearland.cangasso.titles.Title;
import org.json.simple.JSONObject;
import dev.fearland.cangasso.database.data.DataContainer;

@SuppressWarnings("unchecked")
public class SelectedContainer extends AbstractContainer {

  public SelectedContainer(DataContainer dataContainer) {
    super(dataContainer);
  }

  public void setTitle(String id) {
    JSONObject selected = this.dataContainer.getAsJsonObject();
    selected.put("title", id);
    this.dataContainer.set(selected.toString());
    selected.clear();
  }

  public void setIcon(String id) {
    JSONObject selected = this.dataContainer.getAsJsonObject();
    selected.put("icon", id);
    this.dataContainer.set(selected.toString());
    selected.clear();
  }

  public Title getTitle() {
    return Title.getById(this.dataContainer.getAsJsonObject().get("title").toString());
  }
}
