package dev.fearland.cangasso.database.data.container;

import dev.fearland.cangasso.database.data.interfaces.AbstractContainer;
import dev.fearland.cangasso.player.enums.*;
import org.json.simple.JSONObject;
import dev.fearland.cangasso.database.data.DataContainer;

@SuppressWarnings("unchecked")
public class PreferencesContainer extends AbstractContainer {

  public PreferencesContainer(DataContainer dataContainer) {
    super(dataContainer);
  }

  public void changePlayerVisibility() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pv", PlayerVisibility.getByOrdinal((long) preferences.get("pv")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changePrivateMessages() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pm", PrivateMessages.getByOrdinal((long) preferences.get("pm")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeBloodAndGore() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("bg", BloodAndGore.getByOrdinal((long) preferences.get("bg")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeProtectionLobby() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("pl", ProtectionLobby.getByOrdinal((long) preferences.get("pl")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeFly() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("fl", ProtectionLobby.getByOrdinal((long) preferences.get("fl")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public void changeMention() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("mt", Mention.getByOrdinal((long) preferences.get("mt")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  public void changeEntry() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("ae", AnnounceEntry.getByOrdinal((long) preferences.get("ae")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  public void changeClanRequest() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("cr", ClanRequest.getByOrdinal((long) preferences.get("cr")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }
  public void changePartyRequest() {
    JSONObject preferences = this.dataContainer.getAsJsonObject();
    preferences.put("fr", PartyRequest.getByOrdinal((long) preferences.get("fr")).next().ordinal());
    this.dataContainer.set(preferences.toString());
    preferences.clear();
  }

  public PlayerVisibility getPlayerVisibility() {
    return PlayerVisibility.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pv"));
  }

  public PrivateMessages getPrivateMessages() {
    return PrivateMessages.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pm"));
  }

  public BloodAndGore getBloodAndGore() {
    return BloodAndGore.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("bg"));
  }

  public ProtectionLobby getProtectionLobby() {
    return ProtectionLobby.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("pl"));
  }
  public Fly getFly() {
    return Fly.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("fl"));
  }
  public Mention getMention() {
    return Mention.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("mt"));
  }
  public AnnounceEntry getAnnounceEntry() {
    return AnnounceEntry.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("ae"));
  }
  public ClanRequest getClanRequest() {
    return ClanRequest.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("cr"));
  }
  public PartyRequest getPartyRequest() {
    return PartyRequest.getByOrdinal((long) this.dataContainer.getAsJsonObject().get("fr"));
  }
}