package com.eyeofender.enderpearl;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    private EnderPearl plugin;

    public PlayerListener(EnderPearl plugin) {
        this.plugin = plugin;
    }

    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getRankManager().updateRank(event.getPlayer());
    }

}