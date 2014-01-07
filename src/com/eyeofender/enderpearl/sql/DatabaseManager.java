package com.eyeofender.enderpearl.sql;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.sql.Timestamp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.eyeofender.enderpearl.EnderPearl;
import com.eyeofender.enderpearl.Util;
import com.eyeofender.enderpearl.commands.BroadcastCommand;

public class DatabaseManager implements PluginMessageListener {

    private EnderPearl plugin;
    private SQLServer server;

    public DatabaseManager(EnderPearl plugin) {
        this.plugin = plugin;
    }

    public void onJoin(final Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (server == null) Util.sendPM(player, "GetServer");
                Util.sendPM(player, "UUID");
                Util.sendPM(player, "IP");
            }
        }, 10L);
    }

    private void initServer(String name) {
        plugin.getLogger().info("We found the name " + name + "!");

        SQLServer server = plugin.getDatabase().find(SQLServer.class).where().ieq("name", name).findUnique();
        boolean exists = server != null;

        if (!exists) {
            server = new SQLServer();
            server.setName(name);
        }

        server.setIp(Bukkit.getIp());
        server.setPort(Bukkit.getPort());

        String abbreviation = name.toLowerCase().replaceAll("[^a-z]", "").trim();
        SQLGame game = plugin.getDatabase().find(SQLGame.class).where().ieq("abbreviation", abbreviation).findUnique();

        plugin.getLogger().info("The game is " + (game != null ? game.getName() : "") + ".");
        server.setGame(game);

        this.server = server;

        if (exists) {
            plugin.getDatabase().update(server);
        } else {
            plugin.getDatabase().save(server);
        }
    }

    private void updatePlayer(Player player, String uuid) {
        SQLPlayer sqlPlayer = plugin.getDatabase().find(SQLPlayer.class).where().ieq("uuid", uuid).findUnique();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        boolean exists = sqlPlayer != null;

        if (!exists) {
            sqlPlayer = new SQLPlayer();
            sqlPlayer.setUuid(uuid);
        }

        sqlPlayer.setName(player.getName());
        sqlPlayer.setLastJoin(timestamp);

        if (exists) {
            plugin.getDatabase().update(player);
        } else {
            plugin.getDatabase().save(player);
        }

        SQLPlayerServer players = plugin.getDatabase().find(SQLPlayerServer.class).where().ieq("player_id", uuid).findUnique();
        exists = players != null;

        if (!exists) {
            players = new SQLPlayerServer();
            players.setPlayer(sqlPlayer);
        }

        players.setServer(server);
        players.setJoinTime(timestamp);

        if (exists) {
            plugin.getDatabase().update(player);
        } else {
            plugin.getDatabase().save(player);
        }
    }

    private void setPlayerIP(Player player, String ip) {
        SQLPlayer sqlPlayer = plugin.getDatabase().find(SQLPlayer.class).where().ieq("name", player.getName()).findUnique();
        if (sqlPlayer == null) return;

        sqlPlayer.setLastIp(ip);
        plugin.getDatabase().update(player);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));

        try {
            String subchannel = in.readUTF();
            if (subchannel.equals("GetServer")) {
                initServer(in.readUTF());
            } else if (subchannel.equals("UUID")) {
                updatePlayer(player, in.readUTF());
            } else if (subchannel.equals("IP")) {
                setPlayerIP(player, in.readUTF());
            } else if (subchannel.equals("PlayerList")) {
                String server = in.readUTF();
                String playerList = in.readUTF();

                for (String name : playerList.split(", ")) {
                    Util.sendPM(player, "Message", name, BroadcastCommand.messageQueue.get(server));
                }
                BroadcastCommand.messageQueue.remove("server");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
