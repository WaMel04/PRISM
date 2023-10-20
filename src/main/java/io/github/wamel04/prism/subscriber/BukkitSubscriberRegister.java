package io.github.wamel04.prism.subscriber;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

public class BukkitSubscriberRegister {

    public static void register(Jedis jedis, Subscriber subscriber) {
        Bukkit.getScheduler().runTaskAsynchronously(BukkitInitializer.getInstance(), () -> {
            jedis.subscribe(subscriber, subscriber.getChannelName());
        });
    }

}
