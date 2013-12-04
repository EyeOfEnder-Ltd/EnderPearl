package com.eyeofender.enderpearl.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eyeofender.enderpearl.EnderPearl;

public class SpawnCommand implements CommandExecutor {

    private EnderPearl plugin;

    public SpawnCommand(EnderPearl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (plugin.getSpawnLocation() != null) {
            player.teleport(plugin.getSpawnLocation());
        } else {
            player.sendMessage(ChatColor.RED + "Spawn point not set!");
        }
        return true;
    }

}
