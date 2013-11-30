package com.eyeofender.enderpearl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.collect.Maps;

public class BroadcastCommand implements CommandExecutor, PluginMessageListener {

    private Plugin plugin;
    private Map<String, String> messageQueue;

    public BroadcastCommand(Plugin plugin) {
        this.plugin = plugin;
        this.messageQueue = Maps.newHashMap();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length > 1) {
            String server = args[0];
            if (args[0].equalsIgnoreCase("all")) server = "ALL";

            send(sender instanceof Player ? (Player) sender : Bukkit.getOnlinePlayers()[0], "PlayerList", server);
            messageQueue.put(args[0], ChatColor.translateAlternateColorCodes('&', Util.createString(args, 1)));
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid usage!");
            return false;
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("PlayerList")) {
                String server = in.readUTF();
                String playerList = in.readUTF();

                for (String name : playerList.split(", ")) {
                    send(player, "Message", name, messageQueue.get(server));
                }
                messageQueue.remove("server");
            }
        } catch (Exception e) {
        }

    }

    private void send(Player sender, String... messages) {
        if (sender == null) return;

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            for (String message : messages) {
                out.writeUTF(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        sender.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
    }

}
