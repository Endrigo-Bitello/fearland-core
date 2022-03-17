package dev.fearland.cangasso.game;

import java.util.List;

import dev.fearland.cangasso.player.Profile;
import org.bukkit.entity.Player;

public interface Game<T extends GameTeam> {
  
  void broadcastMessage(String message);
  
  void broadcastMessage(String message, boolean spectators);
  
  void join(Profile profile);
  
  void leave(Profile profile, Game<?> game);
  
  void kill(Profile profile, Profile killer);
  
  void killLeave(Profile profile, Profile killer);
  
  void start();
  
  void stop(T winners);
  
  void reset();
  
  String getGameName();
  
  GameState getState();
  
  boolean isSpectator(Player player);
  
  int getOnline();
  
  int getMaxPlayers();
  
  T getTeam(Player player);
  
  List<T> listTeams();
  
  List<Player> listPlayers();
  
  List<Player> listPlayers(boolean spectators);
}
