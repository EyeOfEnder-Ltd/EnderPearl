package com.eyeofender.enderpearl;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Rank {

    private String prefix;
    private String displayName;

    private Team team;

    public Rank(String prefix, String displayName, Scoreboard scoreboard) {
        this.prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        this.displayName = displayName;

        if (displayName != null) {
            this.team = scoreboard.getTeam(displayName);
            if (team == null) scoreboard.registerNewTeam(displayName);
            team.setPrefix(this.prefix);
        }
    }

    public void apply(Player player) {
        if (team != null) team.addPlayer(player);
        String name = ChatColor.WHITE + player.getName();
        if (displayName != null) name = ChatColor.WHITE + prefix + "[" + displayName + "] " + ChatColor.WHITE + player.getName();
        player.setDisplayName(name);
    }
}
