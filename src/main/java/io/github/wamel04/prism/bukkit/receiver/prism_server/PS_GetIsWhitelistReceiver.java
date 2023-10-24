package io.github.wamel04.prism.bukkit.receiver.prism_server;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import redis.clients.jedis.Jedis;

public class PS_GetIsWhitelistReceiver extends Subscriber {

    public PS_GetIsWhitelistReceiver() {
        super("ps_get_iswhitelist_receiver", "ps_get_iswhitelist_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    try (Jedis jedis = PRISM.getJedis()) {
                        jedis.publish("prism:ps_get_iswhitelist_receive_" + split[1], String.valueOf(Bukkit.isWhitelistEnforced()));
                    }
                }
            }
        });
    }

}
