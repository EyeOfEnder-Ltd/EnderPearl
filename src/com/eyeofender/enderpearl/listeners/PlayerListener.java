package com.eyeofender.enderpearl.listeners;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import com.eyeofender.enderpearl.EnderPearl;

public class PlayerListener implements Listener {

    private static Random rand = new Random();
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
        plugin.getPurchaseManager().updatePurchases(player, true);

        if (plugin.getSpawnLocation() != null) {
            Location loc = plugin.getSpawnLocation();
            double xAddition = rand.nextInt(7) - 3;
            double zAddition = rand.nextInt(7) - 3;
            float yaw = (float) Math.toDegrees(-Math.atan2(xAddition, zAddition));

            player.teleport(new Location(loc.getWorld(), loc.getX() + xAddition, loc.getY(), loc.getZ() + zAddition, yaw, loc.getPitch()));
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat("%s: " + ChatColor.RESET + "%s");
    }

}
