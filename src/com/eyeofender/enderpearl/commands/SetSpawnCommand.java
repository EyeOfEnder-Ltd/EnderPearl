package com.eyeofender.enderpearl.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eyeofender.enderpearl.EnderPearl;

public class SetSpawnCommand implements CommandExecutor {

    private EnderPearl plugin;

    public SetSpawnCommand(EnderPearl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        plugin.setSpawnLocation(player.getLocation());
        player.sendMessage(ChatColor.GREEN + "Spawn point set to your current location.");
        return true;
    }

}
