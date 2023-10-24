package io.github.wamel04.prism.requester.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class PP_SendTitleRequester {

    public static void request(PrismPlayer prismPlayer, String title, String subtitle, int fadein, int duration, int fadeout) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                jedis.publish("prism:pp_send_title_request", ProtocolMessageConvertor.convert(prismPlayer.getPrismServer().getServerName(), prismPlayer.getUuid().toString(),
                        title, subtitle, fadein, duration, fadeout));
            }
        });
    }

}
