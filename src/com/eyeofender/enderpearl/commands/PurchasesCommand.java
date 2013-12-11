package com.eyeofender.enderpearl.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.eyeofender.enderpearl.EnderPearl;
import com.eyeofender.enderpearl.purchases.Purchase;

public class PurchasesCommand implements CommandExecutor {

    private EnderPearl plugin;

    public PurchasesCommand(EnderPearl plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        List<Purchase> purchases = plugin.getPurchaseManager().getPurchases(player);

        player.sendMessage(ChatColor.GREEN + "-- " + ChatColor.BOLD + "Purchases:" + ChatColor.RESET + ChatColor.GREEN + " -------");

        if (purchases == null || purchases.isEmpty()) {
            player.sendMessage(ChatColor.RED + "You do not own any items!");
            player.sendMessage(ChatColor.GREEN + "Visit http://eyeofender.com/shop to purchase some.");
        } else {
            for (Purchase purchase : purchases) {
                String info = ChatColor.GOLD + "" + ChatColor.BOLD + purchase.getMinigame() + " " + purchase.getType() + ": " + ChatColor.RESET + ChatColor.GREEN + purchase.getPurchase();
                player.sendMessage(purchase.getExpiry() != null ? info + ChatColor.GRAY + " Until " + purchase.getExpiry() : info);
            }
        }
        player.sendMessage(ChatColor.GREEN + "--------------------");
        return true;
    }

}
