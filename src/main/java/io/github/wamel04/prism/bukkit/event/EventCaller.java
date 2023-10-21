package io.github.wamel04.prism.bukkit.event;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import org.bukkit.Bukkit;

public class EventCaller {

    public static void call(String eventId, String message) {
        switch (eventId) {
            case "proxy_connect_event" -> {
                ProxyConnectEvent event = new ProxyConnectEvent(message);
                Bukkit.getScheduler().runTask(BukkitInitializer.getInstance(), () -> {
                    Bukkit.getPluginManager().callEvent(event);
                });
            }
            case "proxy_disconnect_event" -> {
                ProxyDisconnectEvent event = new ProxyDisconnectEvent(message);
                Bukkit.getScheduler().runTask(BukkitInitializer.getInstance(), () -> {
                    Bukkit.getPluginManager().callEvent(event);
                });
            }
            case "server_change_event" -> {
                ServerChangeEvent event = new ServerChangeEvent(message);
                Bukkit.getScheduler().runTask(BukkitInitializer.getInstance(), () -> {
                    Bukkit.getPluginManager().callEvent(event);
                });
            }
        }
    }

}
