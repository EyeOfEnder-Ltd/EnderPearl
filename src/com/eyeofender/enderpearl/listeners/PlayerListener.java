package com.eyeofender.enderpearl.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.eyeofender.enderpearl.EnderPearl;

public class PlayerListener implements Listener {

    private EnderPearl plugin;

    public PlayerListener(EnderPearl plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if (event.getResult() == Result.KICK_FULL) {
            if (plugin.getRankManager().hasRank(event.getPlayer())) {
                event.allow();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getRankManager().updateRank(player, true);

        if (plugin.getSpawnLocation() != null) {
            player.teleport(plugin.getSpawnLocation());
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat("%s: " + ChatColor.RESET + "%s");
    }

}
