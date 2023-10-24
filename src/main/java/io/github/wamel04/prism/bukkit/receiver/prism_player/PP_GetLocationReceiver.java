package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

public class PP_GetLocationReceiver extends Subscriber {

    public PP_GetLocationReceiver() {
        super("pp_get_location_receiver", "pp_get_location_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    try (Jedis jedis = PRISM.getJedis()) {
                        if (player == null) {
                            jedis.publish("prism:pp_get_location_receive_" + split[1], "null|||0|||0|||0|||0.0|||0.0");
                            jedis.publish("prism:pp_get_location_receive_" + split[1], ProtocolMessageConvertor.convert("null", 0, 0, 0, 0.0f, 0.0f));
                        } else {
                            Location location = player.getLocation();
                            jedis.publish("prism:pp_get_location_receive_" + split[1],
                                    ProtocolMessageConvertor.convert(player.getWorld().getName(), location.getX(), location.getY(), location.getZ(),
                                            location.getPitch(), location.getYaw()));
                        }
                    }
                }
            }
        });
    }

}
