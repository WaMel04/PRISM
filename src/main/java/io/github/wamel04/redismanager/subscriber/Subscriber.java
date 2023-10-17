package io.github.wamel04.redismanager.subscriber;

import io.github.wamel04.redismanager.RedisManager;
import io.github.wamel04.redismanager.bukkit.BukkitInitializer;
import io.github.wamel04.redismanager.proxy.ProxyInitializer;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;

public class Subscriber extends JedisPubSub {

    Jedis jedis;
    String id;
    String channelName;
    SubscriberRunnable subscriberRunnable;

    public Subscriber(String id, String channelName, SubscriberRunnable subscriberRunnable) {
        this.id = id;
        this.channelName = channelName;
        this.subscriberRunnable = subscriberRunnable;
    }

    public String getId() {
        return id;
    }

    public String getChannelName() {
        return channelName;
    }

    public SubscriberRunnable getSubscriberRunnable() {
        return subscriberRunnable;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

    public void start() {
        try {
            jedis.connect();
            jedis.subscribe(this, channelName);
        } catch (Exception e) {
        }
    }

    public void stop() {
        this.unsubscribe();
        jedis.close();
    }

    public void publish(String message) {
        jedis.publish(channelName, message);
    }

    @Override
    public void onMessage(String channelName, String message) {
        subscriberRunnable.run(channelName, message);
    }

    public static HashMap<String, Subscriber> subscriberMap = new HashMap<>();

    public static void registerBukkitSubscriber(Subscriber subscriber) {
        subscriber.setJedis(RedisManager.getBukkitJedis());
        subscriberMap.put(subscriber.getId(), subscriber);

        Bukkit.getScheduler().runTaskAsynchronously(BukkitInitializer.getInstance(), () -> {
            subscriber.start();
        });
    }

    public static void registerProxySubscriber(Subscriber subscriber) {
        subscriber.setJedis(RedisManager.getProxyJedis());
        subscriberMap.put(subscriber.getId(), subscriber);

        ProxyInitializer.getInstance().getProxy().getScheduler().runAsync(ProxyInitializer.getInstance(), () -> {
            subscriber.start();
        });
    }

}
