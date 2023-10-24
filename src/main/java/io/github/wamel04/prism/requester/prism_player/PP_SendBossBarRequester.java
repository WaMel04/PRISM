package io.github.wamel04.prism.requester.prism_player;

import com.google.gson.Gson;
import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.ingame.boss.PrismBossBar;
import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class PP_SendBossBarRequester {

    public static void request(PrismPlayer prismPlayer, PrismBossBar bossBar) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                Gson gson = new Gson();
                jedis.publish("prism:pp_send_bossbar_request", ProtocolMessageConvertor.convert(prismPlayer.getPrismServer().getServerName(), prismPlayer.getUuid().toString(),
                        gson.toJson(bossBar)));
            }
        });
    }

}
