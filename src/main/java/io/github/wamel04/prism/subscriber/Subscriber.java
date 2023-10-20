package io.github.wamel04.prism.subscriber;

import io.github.wamel04.prism.PRISM;
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
        this.jedis = PRISM.getJedis();
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

    public void register() {
        subscriberMap.put(id, this);

        if (PRISM.isBukkit())
            BukkitSubscriberRegister.register(jedis, this);
        else
           ProxySubscriberRegister.register(jedis, this);
    }

    public void unregister() {
        this.unsubscribe();
    }

    public void publish(String message) {
        jedis.publish(channelName, message);
    }

    @Override
    public void onMessage(String channelName, String message) {
        subscriberRunnable.run(channelName, message);
    }

    private static HashMap<String, Subscriber> subscriberMap = new HashMap<>();

    public static HashMap<String, Subscriber> getSubscriberMap() {
        return subscriberMap;
    }

}
