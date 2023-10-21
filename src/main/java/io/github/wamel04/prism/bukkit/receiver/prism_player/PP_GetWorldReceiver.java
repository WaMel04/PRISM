package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class PP_GetWorldReceiver extends Subscriber {

    public PP_GetWorldReceiver() {
        super("pp_get_world_receiver", "pp_get_world_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    try (Jedis jedis = PRISM.getJedis()) {
                        if (player == null) {
                            jedis.publish("prism:pp_get_world_receive_" + split[1], "null");
                        } else {
                            jedis.publish("prism:pp_get_world_receive_" + split[1], player.getWorld().getName());
                        }
                    }
                }
            }
        });
    }

}
