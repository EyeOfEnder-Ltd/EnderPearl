package com.eyeofender.enderpearl;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EnderPearl extends JavaPlugin {

    private RankManager rankManager;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);

        BroadcastCommand broadcast = new BroadcastCommand(this);
        getCommand("broadcast").setExecutor(broadcast);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", broadcast);

        rankManager = new RankManager(this);
    }

    public RankManager getRankManager() {
        return rankManager;
    }

}
