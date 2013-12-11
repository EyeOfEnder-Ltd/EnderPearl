package com.eyeofender.enderpearl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.eyeofender.enderpearl.commands.BroadcastCommand;
import com.eyeofender.enderpearl.commands.HubCommand;
import com.eyeofender.enderpearl.commands.PurchasesCommand;
import com.eyeofender.enderpearl.commands.SetSpawnCommand;
import com.eyeofender.enderpearl.commands.SpawnCommand;
import com.eyeofender.enderpearl.listeners.PlayerListener;
import com.eyeofender.enderpearl.purchases.Purchase;
import com.eyeofender.enderpearl.purchases.PurchaseManager;
import com.eyeofender.enderpearl.ranks.RankEntry;
import com.eyeofender.enderpearl.ranks.RankManager;

public class EnderPearl extends JavaPlugin {

    private RankManager rankManager;
    private PurchaseManager purchaseManager;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        Util.init(this);

        BroadcastCommand broadcast = new BroadcastCommand();
        getCommand("broadcast").setExecutor(broadcast);
        getCommand("hub").setExecutor(new HubCommand());
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("purchases").setExecutor(new PurchasesCommand(this));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", broadcast);

        try {
            getDatabase().find(RankEntry.class).findRowCount();
            getDatabase().find(Purchase.class).findRowCount();
        } catch (PersistenceException ex) {
            getLogger().info("Installing database due to first time usage.");
            installDDL();
        }

        rankManager = new RankManager(this);
        purchaseManager = new PurchaseManager(this);

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

        for (Player player : Bukkit.getOnlinePlayers()) {
            Util.sendPM(player, "Connect", "hub");
        }
    }

    @Override
    public void installDDL() {
        super.installDDL();
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(RankEntry.class);
        list.add(Purchase.class);
        return list;
    }

    public RankManager getRankManager() {
        return rankManager;
    }

    public PurchaseManager getPurchaseManager() {
        return purchaseManager;
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
