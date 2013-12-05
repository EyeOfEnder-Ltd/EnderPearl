package com.eyeofender.enderpearl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

public class Util {

    private static EnderPearl plugin;

    public static void init(EnderPearl plugin) {
        Util.plugin = plugin;
    }

    public static String createString(String[] args, int start) {
        StringBuilder string = new StringBuilder();

        for (int x = start; x < args.length; x++) {
            string.append(args[x]);
            if (x != args.length - 1) {
                string.append(" ");
            }
        }

        return string.toString();
    }

    public static void sendPM(Player sender, String... messages) {
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
