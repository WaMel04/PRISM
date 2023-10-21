package io.github.wamel04.prism.prism_object;

import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.requester.prism_player.*;
import redis.clients.jedis.Jedis;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class PrismPlayer {

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
        if (getPrismServer().equals(prismLocation.getPrismWorld().getPrismServer())) {
            PP_TeleportRequester.request(this, prismLocation);
        } else {
            try {
                Void ignored = connect(prismLocation.getPrismWorld().getPrismServer()).get(3, TimeUnit.SECONDS);
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
