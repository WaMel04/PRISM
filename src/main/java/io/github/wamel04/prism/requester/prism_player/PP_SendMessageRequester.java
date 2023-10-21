package io.github.wamel04.prism.requester.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.PrismPlayer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class PP_SendMessageRequester {

    public static void request(PrismPlayer prismPlayer, String message) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                jedis.publish("prism:pp_send_message_request", ProtocolMessageConvertor.convert(prismPlayer.getPrismServer(), prismPlayer.getUuid().toString(),
                        message));
            }
        });
    }

}
