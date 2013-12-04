package com.eyeofender.enderpearl;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.eyeofender.enderpearl.commands.BroadcastCommand;
import com.eyeofender.enderpearl.listeners.PlayerListener;
import com.eyeofender.enderpearl.ranks.RankEntry;
import com.eyeofender.enderpearl.ranks.RankManager;

public class EnderPearl extends JavaPlugin {

    private RankManager rankManager;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        BroadcastCommand broadcast = new BroadcastCommand(this);
        getCommand("broadcast").setExecutor(broadcast);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", broadcast);

        rankManager = new RankManager(this);

        if (getConfig().getConfigurationSection("spawn") != null) {
            World world = Bukkit.getWorld(getConfig().getString("spawn.world"));
            double x = getConfig().getDouble("spawn.x");
            double y = getConfig().getDouble("spawn.y");
            double z = getConfig().getDouble("spawn.z");
            double yaw = getConfig().getDouble("spawn.yaw");
            double pitch = getConfig().getDouble("spawn.pitch");

            spawnLocation = new Location(world, x, y, z, (float) yaw, (float) pitch);
        }

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @Override
    public void installDDL() {
        super.installDDL();
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(RankEntry.class);
        return list;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;

        getConfig().set("spawn.world", spawnLocation.getWorld().getName());
        getConfig().set("spawn.x", spawnLocation.getBlockX() + 0.5);
        getConfig().set("spawn.y", spawnLocation.getY());
        getConfig().set("spawn.z", spawnLocation.getBlockZ() + 0.5);
        getConfig().set("spawn.yaw", spawnLocation.getYaw());
        getConfig().set("spawn.pitch", spawnLocation.getPitch());
        saveConfig();
    }

}
