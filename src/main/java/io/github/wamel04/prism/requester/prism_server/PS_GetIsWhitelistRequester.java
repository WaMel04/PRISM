package io.github.wamel04.prism.requester.prism_server;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.ingame.server.PrismServer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.concurrent.CompletableFuture;

public class PS_GetIsWhitelistRequester {

    public static CompletableFuture<Boolean> request(PrismServer server) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        new Thread(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                long now = System.currentTimeMillis();

                jedis.publish("prism:ps_get_iswhitelist_request", ProtocolMessageConvertor.convert(server.getServerName(), now));
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (message.equalsIgnoreCase("true")) {
                            future.complete(true);
                        } else {
                            future.complete(false);
                        }

                        unsubscribe();
                    }
                }, "prism:ps_get_iswhitelist_receive_" + now);
            }
        }).start();

        return future;
    }

}
