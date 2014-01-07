package com.eyeofender.enderpearl.sql;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "Rank")
public class SQLRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    @Column(length = 4)
    private String colour;

    @NotNull
    private int priority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void apply(Player player, Scoreboard scoreboard) {
        Team team = scoreboard.getTeam(name);
        String prefix = ChatColor.translateAlternateColorCodes('&', colour);

        if (team == null) {
            team = scoreboard.registerNewTeam(name);
            team.setPrefix(prefix);
        }

        team.addPlayer(player);
        player.setDisplayName(prefix + "[" + this.name + "] " + ChatColor.GRAY + player.getName());
    }

}
