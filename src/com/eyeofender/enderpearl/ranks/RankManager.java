package com.eyeofender.enderpearl.ranks;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.persistence.PersistenceException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.eyeofender.enderpearl.EnderPearl;
import com.google.common.collect.Maps;

public class RankManager {

    private EnderPearl plugin;
    private FileConfiguration config;
    private File configFile;
    private Map<String, Rank> ranks;

    public RankManager(EnderPearl plugin) {
        this.plugin = plugin;
        reloadConfig();
        this.ranks = Maps.newHashMap();

        loadRanks();
        loadPlayers();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updateRank(player, false);
                }
            }
        }, 0L, 20L * 60L);
    }

    private void reloadConfig() {
        if (configFile == null) {
            configFile = new File("/root/config/ranks.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        InputStream defConfigStream = plugin.getResource("ranks.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            config.setDefaults(defConfig);
            saveConfig();
        }
    }

    private FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    private void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }

    private void loadRanks() {
        for (String name : getConfig().getConfigurationSection("ranks").getKeys(false)) {
            String path = "ranks." + name + ".";
            String colour = getConfig().getString(path + "colour");
            String display = getConfig().getString(path + "displayname");
            ranks.put(name, new Rank(colour, display, Bukkit.getScoreboardManager().getMainScoreboard()));
        }
    }

    private void loadPlayers() {
        try {
            plugin.getDatabase().find(RankEntry.class).findRowCount();
        } catch (PersistenceException ex) {
            plugin.getLogger().info("Installing database due to first time usage.");
            plugin.installDDL();
        }
    }

    public void updateRank(Player player, boolean warn) {
        String rankName = "default";
        RankEntry entry = plugin.getDatabase().find(RankEntry.class).where().ieq("name", player.getName()).findUnique();

        if (entry != null) {
            Date expiry = entry.getExpiry();

            if (expiry != null) {
                Date date = new Date(new java.util.Date().getTime());
                String type = entry.getType();
                if (expiry.after(date)) {
                    if (warn) {
                        long days = TimeUnit.MILLISECONDS.toDays(expiry.getTime() - date.getTime()) + 1;
                        if (days <= 7) {
                            player.sendMessage("Your " + type + " membership will expire in " + days + (days == 1 ? " day!" : " days!"));
                            player.sendMessage("Visit http://eyeofender.com/shop to renew your membership.");
                        }
                    }
                    rankName = entry.getType();
                } else {
                    player.sendMessage("Your " + type + " membership has expired!");
                    player.sendMessage("Visit http://eyeofender.com/shop to renew your membership.");
                    plugin.getDatabase().delete(entry);
                }
            } else {
                rankName = entry.getType();
            }
        }

        Rank rank = ranks.get(rankName);

        if (rank == null) {
            plugin.getLogger().warning("Could not find " + player.getName() + "'s rank, " + rankName);
            return;
        }

        rank.apply(player);
    }

}
