package com.eyeofender.enderpearl.commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eyeofender.enderpearl.Util;
import com.google.common.collect.Maps;

public class BroadcastCommand implements CommandExecutor {

    public static Map<String, String> messageQueue = Maps.newHashMap();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 1) {
            String server = args[0];
            if (args[0].equalsIgnoreCase("all")) server = "ALL";

            Util.sendPM(sender instanceof Player ? (Player) sender : Bukkit.getOnlinePlayers()[0], "PlayerList", server);
            messageQueue.put(args[0], ChatColor.translateAlternateColorCodes('&', Util.createString(args, 1)));
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid usage!");
            return false;
        }
    }

}
