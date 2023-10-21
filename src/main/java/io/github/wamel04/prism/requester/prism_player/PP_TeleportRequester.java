package io.github.wamel04.prism.requester.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.PrismLocation;
import io.github.wamel04.prism.prism_object.PrismPlayer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class PP_TeleportRequester {

    public static void request(PrismPlayer prismPlayer, PrismLocation prismLocation) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                jedis.publish("prism:pp_teleport_request", ProtocolMessageConvertor.convert(prismPlayer.getPrismServer(), prismPlayer.getUuid().toString(),
                        prismLocation.getPrismWorld().getWorldName(), prismLocation.getX(), prismLocation.getY(), prismLocation.getZ(),
                        prismLocation.getPitch(), prismLocation.getYaw()));
            }
        });
    }

}
