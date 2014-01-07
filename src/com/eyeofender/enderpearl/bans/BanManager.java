package com.eyeofender.enderpearl.bans;

import java.sql.Timestamp;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.eyeofender.enderpearl.EnderPearl;

public class BanManager {

    private static final long HOUR = 60L * 60L * 1000L;

    private EnderPearl plugin;

    public BanManager(EnderPearl plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateBans(player, false);
                }
            }
        }, 0L, 20L * 60L);
    }

    public List<PlayerBan> getBans() {
        return plugin.getDatabase().find(PlayerBan.class).findList();
    }

    public List<PlayerBan> getBans(Player player) {
        return plugin.getDatabase().find(PlayerBan.class).where().ieq("name", player.getName()).findList();
    }

    public void ban(Player player, CommandSender banner, String reason, long hours) {
        PlayerBan ban = new PlayerBan();
        long time = System.currentTimeMillis();
        ban.setName(player.getName());
        ban.setByWhom(banner.getName());
        ban.setReason(reason);
        ban.setTimestamp(new Timestamp(time));
        ban.setExpiry(new Timestamp(time + (hours * HOUR)));
    }

    public void permBan(Player player, CommandSender banner, String reason) {
        PlayerBan ban = new PlayerBan();
        ban.setName(player.getName());
        ban.setByWhom(banner.getName());
        ban.setReason(reason);
        ban.setTimestamp(new Timestamp(System.currentTimeMillis()));
        ban.setExpiry(null);
    }

    public void unban(Player player) {
        List<PlayerBan> purchases = getBans(player);
        if (purchases == null || purchases.isEmpty()) return;

        for (PlayerBan purchase : purchases) {
            plugin.getDatabase().delete(purchase);
        }
    }

    public void updateBans(Player player, boolean warn) {
        List<PlayerBan> bans = getBans(player);
        if (bans == null || bans.isEmpty()) return;

        for (PlayerBan ban : bans) {
            Timestamp expiry = ban.getExpiry();
            if (expiry == null) continue;

            if (new Timestamp(System.currentTimeMillis()).after(expiry)) {
                plugin.getDatabase().delete(ban);
            }
        }
    }
}
