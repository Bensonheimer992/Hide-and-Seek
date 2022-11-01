package net.tylermurphy.hideAndSeek.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tylermurphy.hideAndSeek.Main;
import net.tylermurphy.hideAndSeek.game.events.Border;
import net.tylermurphy.hideAndSeek.world.VoidGenerator;
import net.tylermurphy.hideAndSeek.world.WorldLoader;
import org.bukkit.*;
import org.bukkit.util.Vector;

import static net.tylermurphy.hideAndSeek.configuration.Config.*;

public class Map {

  private final String name;

  private Location 
    spawnPosition = new Location(null, 0, 0, 0),
    lobbyPosition = new Location(null, 0, 0, 0),
    seekerLobbyPosition = new Location(null, 0, 0, 0);

  private String
    spawnWorldName = "world",
    lobbyWorldName = "world",
    seekerLobbyWorldName = "world";

  private int 
    xBoundMin = 0,
    zBoundMin = 0,
    xBoundMax = 0,
    zBoundMax = 0,
    xWorldBorder = 0,
    zWorldBorder = 0,
    worldBorderSize = 0,
    worldBorderDelay = 0,
    worldBorderChange = 0;

  private boolean
    blockhunt = false;

  private List<Material>
    blockhuntBlocks = new ArrayList<>();

  private final Border
    worldBorder;

  private final WorldLoader
    worldLoader;

  public Map(String name) {
    this.name = name;
    this.worldBorder = new Border(this);
    this.worldLoader = new WorldLoader(this);
  }

  public void setSpawn(Location pos) {
    this.spawnPosition = pos;
    if(pos.getWorld() != null)
      this.spawnWorldName = pos.getWorld().getName();
  }

  public void setLobby(Location pos) {
    this.lobbyPosition = pos;
    if(pos.getWorld() != null)
      this.lobbyWorldName = pos.getWorld().getName();
  }

  public void setSeekerLobby(Location pos) {
    this.seekerLobbyPosition = pos;
    if(pos.getWorld() != null)
      this.seekerLobbyWorldName = pos.getWorld().getName();
  }

  public void setSpawnName(String name) {
    this.spawnWorldName = name;
  }

  public void setLobbyName(String name) {
    this.lobbyWorldName = name;
  }

  public void setSeekerLobbyName(String name) {
    this.seekerLobbyWorldName = name;
  }

  public void setWorldBorderData(int x, int z, int size, int delay, int move) {
    if(size < 1) {
      this.worldBorderSize = 0;
      this.worldBorderDelay = 0;
      this.worldBorderChange = 0;
      this.xWorldBorder = 0;
      this.zWorldBorder = 0;
    } else {
      this.worldBorderSize = size;
      this.worldBorderDelay = delay;
      this.worldBorderChange = move;
      this.xWorldBorder = x;
      this.zWorldBorder = z;
    }
    this.worldBorder.resetWorldBorder();
  }

  public void setBlockhunt(boolean enabled, List<Material> blocks) {
    this.blockhunt = enabled;
    this.blockhuntBlocks = blocks;
  }

  public void setBoundMin(int x, int z) {
    this.xBoundMin = x;
    this.zBoundMin = z;
  }

  public void setBoundMax(int x, int z) {
    this.xBoundMax = x;
    this.zBoundMax = z;
  }

  public Location getGameSpawn() {
    if(mapSaveEnabled) {
      return new Location(
              Bukkit.getWorld("hs_" + spawnWorldName),
              spawnPosition.getX(),
              spawnPosition.getY(),
              spawnPosition.getZ()
      );
    } else {
      return spawnPosition;
    }
  }

  public String getGameSpawnName() {
    if(mapSaveEnabled)
      return "hs_"+ spawnWorldName;
    else
      return spawnWorldName;
  }

  public Location getSpawn() {
    return spawnPosition;
  }

  public String getSpawnName() {
    return spawnWorldName;
  }

  public Location getLobby() {
    return lobbyPosition;
  }

  public String getLobbyName() {
    return lobbyWorldName;
  }

  public Location getSeekerLobby() {
    return seekerLobbyPosition;
  }

  public String getSeekerLobbyName() {
    return seekerLobbyWorldName;
  }

  public Location getGameSeekerLobby() {
    if(mapSaveEnabled) {
      return new Location(
              Bukkit.getWorld("hs_" + getSeekerLobbyName()),
              seekerLobbyPosition.getX(),
              seekerLobbyPosition.getY(),
              seekerLobbyPosition.getZ()
      );
    } else {
      return seekerLobbyPosition;
    }
  }

  public boolean isWorldBorderEnabled() {
    return worldBorderSize > 0;
  }

  public Vector getWorldBorderPos() {
    return new Vector(
      xWorldBorder,
      0,
      zWorldBorder
    );
  }

  public Vector getWorldBorderData() {
    return new Vector(
      worldBorderSize,
      worldBorderDelay,
      worldBorderChange
    );
  }

  public Border getWorldBorder() {
    return worldBorder;
  }

  public boolean isBlockHuntEnabled() {
    return blockhunt;
  }

  public List<Material> getBlockHunt() {
    return blockhuntBlocks;
  }

  public Vector getBoundsMin() {
    return new Vector(
      xBoundMin,
      0,
      zBoundMin
    );
  }

  public Vector getBoundsMax() {
    return new Vector(
      xBoundMax,
      0,
      zBoundMax
    );
  }

  public String getName() {
    return name;
  }

  public WorldLoader getWorldLoader() {
    return worldLoader;
  }

  public boolean isNotSetup() {
    if (spawnPosition.getBlockX() == 0 && spawnPosition.getBlockY() == 0 && spawnPosition.getBlockZ() == 0 || !Map.worldExists(spawnWorldName)) return true;
    if (lobbyPosition.getBlockX() == 0 && lobbyPosition.getBlockY() == 0 && lobbyPosition.getBlockZ() == 0 || !Map.worldExists(lobbyWorldName)) return true;
    if (exitPosition == null || exitPosition.getBlockX() == 0 && exitPosition.getBlockY() == 0 && exitPosition.getBlockZ() == 0 || !Map.worldExists(exitWorld) ) return true;
    if (seekerLobbyPosition.getBlockX() == 0 && seekerLobbyPosition.getBlockY() == 0 && seekerLobbyPosition.getBlockZ() == 0 || !Map.worldExists(seekerLobbyWorldName)) return true;
    if (mapSaveEnabled && !Map.worldExists(getGameSpawnName())) return true;
    if(isWorldBorderEnabled() &&
        new Vector(spawnPosition.getX(), 0, spawnPosition.getZ()).distance(new Vector(xWorldBorder, 0, zWorldBorder)) > 100) return true;
    return xBoundMin == 0 || zBoundMin == 0 || xBoundMax == 0 || zBoundMax == 0;
  }

  public boolean isSpawnNotSetup() {
    return spawnPosition.getBlockX() == 0 && spawnPosition.getBlockY() == 0 && spawnPosition.getBlockZ() == 0;
  }

  public static boolean worldExists(String worldName) {
    File destination = new File(Main.getInstance().getWorldContainer()+File.separator+worldName);
    return destination.isDirectory();
  }

}
