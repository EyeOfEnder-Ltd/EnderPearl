package com.eyeofender.enderpearl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.google.common.collect.Maps;

public class RankManager {

    private Plugin plugin;
    private FileConfiguration config;
    private File configFile;
    private Map<String, Rank> ranks;

    public RankManager(Plugin plugin) {
        this.plugin = plugin;
        reloadConfig();
        this.ranks = Maps.newHashMap();

        loadRanks();
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

    public void updateRank(Player player) {
        String rankName = getConfig().getString("players." + player.getName(), "default");
        plugin.getLogger().warning("Could not find " + player.getName() + "'s rank, " + rankName);

        Rank rank = ranks.get(rankName);
        if (rank == null) return;
        rank.apply(player);
    }

}
