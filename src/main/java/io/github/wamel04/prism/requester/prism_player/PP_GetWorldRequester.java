package io.github.wamel04.prism.requester.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.PrismPlayer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CompletableFuture;

public class PP_GetWorldRequester {

    public static CompletableFuture<String> request(PrismPlayer prismPlayer) {
        CompletableFuture<String> future = new CompletableFuture<>();

        new Thread(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                jedis.publish("prism:pp_get_world_request", ProtocolMessageConvertor.convert(prismPlayer.getPrismServer().getServerName(), prismPlayer.getUuid().toString()));
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        future.complete(message);
                        unsubscribe();
                    }
                }, "prism:pp_get_world_receive_" + prismPlayer.getUuid().toString());
            }
        }).start();

        return future;
    }

}
