package com.eyeofender.enderpearl.ranks;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.eyeofender.enderpearl.EnderPearl;
import com.eyeofender.enderpearl.sql.SQLPlayerRank;

public class RankManager {

    private EnderPearl plugin;
    private Scoreboard scoreboard;

    public RankManager(EnderPearl plugin) {
        this.plugin = plugin;
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateRank(player, false);
                }
            }
        }, 0L, 20L * 60L);
    }

    public SQLPlayerRank getRank(Player player) {
        return plugin.getDatabase().find(SQLPlayerRank.class).where().ieq("name", player.getName()).findUnique();
    }

    public boolean hasRank(Player player) {
        return getRank(player) != null;
    }

    public void updateRank(Player player, boolean warn) {
        Team team = scoreboard.getPlayerTeam(player);
        if (team != null) team.removePlayer(player);

        SQLPlayerRank rank = getRank(player);

        if (rank != null) {
            Timestamp expiry = rank.getExpiry();

            if (expiry == null) {
                rank.getRank().apply(player, scoreboard);
                return;
            }

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());

            if (expiry.after(currentTime)) {
                if (warn) {
                    long days = TimeUnit.MILLISECONDS.toDays(expiry.getTime() - currentTime.getTime()) + 1;
                    if (days <= 7) {
                        player.sendMessage(ChatColor.YELLOW + "Your " + rank.getRank().getName() + " membership will expire in " + days + (days == 1 ? " day!" : " days!"));
                        player.sendMessage(ChatColor.GOLD + "Visit http://eyeofender.com/shop to renew your membership.");
                    }
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "Your " + rank.getRank().getName() + " membership has expired!");
                player.sendMessage(ChatColor.GOLD + "Visit http://eyeofender.com/shop to purchase a new one.");
                plugin.getDatabase().delete(rank);
                rank = null;
            }
        }

        if (rank == null) {
            team = scoreboard.getTeam("Default");

            if (team == null) {
                team = scoreboard.registerNewTeam("Default");
            }

            team.addPlayer(player);
            player.setDisplayName(ChatColor.GRAY + player.getName());
        }

        rank.getRank().apply(player, Bukkit.getScoreboardManager().getMainScoreboard());
    }
}
