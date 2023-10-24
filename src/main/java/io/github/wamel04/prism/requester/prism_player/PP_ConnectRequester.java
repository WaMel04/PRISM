package io.github.wamel04.prism.requester.prism_player;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import io.github.wamel04.prism.prism_object.ingame.server.PrismServer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CompletableFuture;

public class PP_ConnectRequester {

    public static CompletableFuture<Void> request(PrismPlayer prismPlayer, PrismServer prismServer) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        new Thread(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                jedis.publish("prism:pp_connect_request_proxy", ProtocolMessageConvertor.convert(prismServer.getServerName(), prismPlayer.getUuid().toString()));
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        future.complete(null);
                        unsubscribe();
                    }
                }, "prism:pp_connect_receive_" + prismPlayer.getUuid().toString());
            }
        }).start();

        return future;
    }

}
