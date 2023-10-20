package io.github.wamel04.prism.subscriber;

import io.github.wamel04.prism.proxy.ProxyInitializer;
import redis.clients.jedis.Jedis;

public class ProxySubscriberRegister {

    public static void register(Jedis jedis, Subscriber subscriber) {
        ProxyInitializer.getInstance().getProxy().getScheduler().runAsync(ProxyInitializer.getInstance(), () -> {
            jedis.subscribe(subscriber, subscriber.getChannelName());
        });
    }

}
