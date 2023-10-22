package io.github.wamel04.prism.prism_object;

import com.google.gson.Gson;
import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.requester.prism_player.*;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PrismPlayer {

    public static PrismPlayer getByUuid(UUID uuid) {
        try (Jedis jedis = PRISM.getJedis()) {
            for (String serverName : jedis.hkeys("prism:prism_object:prism_server_name_map")) {
                String redisKey = "prism:prism_object:prism_player_uuid_map:" + serverName;

                if (jedis.hexists(redisKey, uuid.toString())) {
                    String data = jedis.hget(redisKey, uuid.toString());
                    return new Gson().fromJson(data, PrismPlayer.class);
                }
            }
        }

        return null;
    }

    public static PrismPlayer getByName(String nickname) {
        try (Jedis jedis = PRISM.getJedis()) {
            nickname = nickname.toLowerCase();

            for (String serverName : jedis.hkeys("prism:prism_object:prism_server_name_map")) {
                String redisKey = "prism:prism_object:prism_player_name_map:" + serverName;

                if (jedis.hexists(redisKey, nickname)) {
                    String data = jedis.hget(redisKey, nickname);
                    return new Gson().fromJson(data, PrismPlayer.class);
                }
            }
        }

        return null;
    }

    UUID uuid;
    String nickname;

    public PrismPlayer(UUID uuid, String nickname) {
        this.uuid = uuid;
        this.nickname = nickname;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return nickname;
    }

    public PrismServer getPrismServer() {
        try (Jedis jedis = PRISM.getJedis()) {
            String serverName = jedis.hget("prism:prism_object:prism_player_server_map", uuid.toString());
            return serverName == null ? null : PrismServer.getByName(serverName);
        }
    }

    public CompletableFuture<Void> connect(PrismServer server) {
        return PP_ConnectRequester.request(this, server);
    }

    public PrismWorld getPrismWorld() {
        try {
            String worldName = PP_GetWorldRequester.request(this).get(3, TimeUnit.SECONDS);
            return new PrismWorld(getPrismServer(), worldName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public PrismLocation getPrismLocation() {
        try {
            String[] split = PP_GetLocationRequester.request(this).get(3, TimeUnit.SECONDS).split("\\|");

            PrismWorld prismWorld = new PrismWorld(getPrismServer(), split[0]);
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            float pitch = Float.parseFloat(split[4]);
            float yaw = Float.parseFloat(split[5]);

            return new PrismLocation(prismWorld, x, y, z, pitch, yaw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void teleport(PrismLocation prismLocation) {
        if (getPrismServer().getServerName().equals(prismLocation.getPrismWorld().getPrismServer().getServerName())) {
            PP_TeleportRequester.request(this, prismLocation);
        } else {
            try {
                connect(prismLocation.getPrismWorld().getPrismServer()).get(3, TimeUnit.SECONDS);
                PP_TeleportRequester.request(this, prismLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        PP_SendMessageRequester.request(this, message);
    }

}
