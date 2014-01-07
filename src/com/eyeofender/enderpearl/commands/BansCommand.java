package com.eyeofender.enderpearl.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eyeofender.enderpearl.EnderPearl;
import com.eyeofender.enderpearl.bans.PlayerBan;

public class BansCommand implements CommandExecutor {

    private EnderPearl plugin;

    public BansCommand(EnderPearl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        List<PlayerBan> bans = plugin.getBanManager().getBans();

        player.sendMessage(ChatColor.GREEN + "-- " + ChatColor.BOLD + "Bans:" + ChatColor.RESET + ChatColor.GREEN + " -------");

        if (bans == null || bans.isEmpty()) {
            player.sendMessage(ChatColor.RED + "No bans found!");
        } else {
            for (PlayerBan ban : bans) {
                String info = ChatColor.GOLD + "" + ChatColor.BOLD + ban.getName() + ": " + ChatColor.RESET;
                String reason = ChatColor.GREEN + "By " + ban.getByWhom() + " for " + ban.getReason();
                String expiry = ban.getExpiry() != null ? ChatColor.GRAY + " Until " + ban.getExpiry() : "";
                player.sendMessage(info + reason + expiry);
            }
        }
        player.sendMessage(ChatColor.GREEN + "--------------------");
        return true;
    }

}
