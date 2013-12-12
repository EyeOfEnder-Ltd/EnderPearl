package com.eyeofender.enderpearl.purchases;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.eyeofender.enderpearl.EnderPearl;

public class PurchaseManager {

    private EnderPearl plugin;

    public PurchaseManager(EnderPearl plugin) {
        this.plugin = plugin;

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    updatePurchases(player, false);
                }
            }
        }, 0L, 20L * 60L);
    }

    public List<Purchase> getPurchases(Player player) {
        return plugin.getDatabase().find(Purchase.class).where().ieq("name", player.getName()).findList();
    }

    public List<Purchase> getPurchases(Player player, String minigame, String type, String purchase) {
        return plugin.getDatabase().find(Purchase.class).where().ieq("name", player.getName()).ieq("minigame", minigame).ieq("type", type).ieq("purchase", purchase).findList();
    }

    public boolean has(Player player, String minigame, String type, String purchase) {
        List<Purchase> purchases = getPurchases(player, minigame, type, purchase);
        return purchases != null && purchases.size() > 0;
    }

    public boolean hasKit(Player player, String minigame, String kit) {
        return has(player, minigame, "kit", kit);
    }

    public boolean hasPerk(Player player, String minigame, String perk) {
        return has(player, minigame, "perk", perk);
    }

    public boolean hasKillstreak(Player player, String minigame, String killstreak) {
        return has(player, minigame, "killstreak", killstreak);
    }

    public void addPurchase(Purchase purchase) {
        plugin.getDatabase().save(purchase);
    }

    public void updatePurchases(Player player, boolean warn) {
        List<Purchase> purchases = getPurchases(player);
        if (purchases == null) return;

        for (Purchase purchase : purchases) {
            Timestamp expiry = purchase.getExpiry();
            if (expiry == null) continue;
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            String item = ChatColor.GOLD + "" + ChatColor.BOLD + purchase.getPurchase() + ChatColor.RESET + ChatColor.YELLOW + " " + purchase.getType();
            String game = ChatColor.GOLD + "" + ChatColor.BOLD + purchase.getMinigame() + ChatColor.RESET + ChatColor.YELLOW;

            if (expiry.after(currentTime)) {
                if (warn) {
                    long days = TimeUnit.MILLISECONDS.toDays(expiry.getTime() - currentTime.getTime()) + 1;
                    if (days <= 7) {
                        player.sendMessage(ChatColor.YELLOW + "Your " + item + " for " + game + " will expire in " + (days <= 1 ? "less than a day!" : days + " days!"));
                        player.sendMessage(ChatColor.GOLD + "Visit http://eyeofender.com/shop to purchase a new one.");
                    }
                }
            } else {
                player.sendMessage(ChatColor.YELLOW + "Your " + item + " for " + game + " has expired!");
                player.sendMessage(ChatColor.GOLD + "Visit http://eyeofender.com/shop to purchase a new one.");
                plugin.getDatabase().delete(purchases);
            }
        }
    }

}
