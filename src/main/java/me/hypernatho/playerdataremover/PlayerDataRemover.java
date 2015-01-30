package me.hypernatho.playerdataremover;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerDataRemover extends JavaPlugin implements Listener {

    public List<World> worlds = new ArrayList<World>();

    @Override
    public void onEnable() {
        {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            if (!new File(getDataFolder(), "config.yml").exists()) {
                saveDefaultConfig();
            }
        }
        {
            if (getConfig().getStringList("worlds") != null) {
                for (String world : getConfig().getStringList("worlds")) {
                    if (Bukkit.getWorld(world) != null) {
                        worlds.add(Bukkit.getWorld(world));
                        Bukkit.getConsoleSender().sendMessage("  - Using world '" + world + "'");
                    }
                    else {
                        Bukkit.getConsoleSender().sendMessage("  - [PlayerDataRemover] Unable to use world '" + world + "' (Doesn't Exist)");
                    }
                }
            }
        }
    }

    @Override
    public void onDisable() {
        int files = 0;
        for (World world : worlds) {
            File dataFolder = new File(world.getWorldFolder().getAbsolutePath(), "playerdata/");
            if (!dataFolder.exists()) { continue; }

            File[] datafiles = dataFolder.listFiles();
            for (File datafile : datafiles) {
                datafile.delete();
                files++;
            }
        }
        Bukkit.getConsoleSender().sendMessage("[PlayerDataRemover] Cleaned up " + files + " data files from " + worlds.size() + " worlds!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (World world : worlds) {
            File dataFolder = new File(world.getWorldFolder().getAbsolutePath(), "playerdata/");
            File datafile = new File(dataFolder, player.getUniqueId().toString() + ".dat");
            if (datafile.exists()) {
                datafile.delete();
                Bukkit.getConsoleSender().sendMessage("[PlayerDataRemover] Removed player data for " + player.getName() + " from world " + world.getName());
            }
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        for (World world : worlds) {
            File dataFolder = new File(world.getWorldFolder().getAbsolutePath(), "playerdata/");
            File datafile = new File(dataFolder, player.getUniqueId().toString() + ".dat");
            if (datafile.exists()) {
                datafile.delete();
                Bukkit.getConsoleSender().sendMessage("[PlayerDataRemover] Removed player data for " + player.getName() + " from world " + world.getName());
            }
        }
    }

}
