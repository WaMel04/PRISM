package io.github.wamel04.prism.prism_object.ingame.server;

import com.google.gson.Gson;
import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import io.github.wamel04.prism.requester.prism_server.PS_GetIsWhitelistRequester;
import redis.clients.jedis.Jedis;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PrismServer {

    private static final String NAME_MAP_REDIS_KEY = "prism:prism_object:prism_server_name_map";
    private static final String PORT_MAP_REDIS_KEY = "prism:prism_object:prism_server_port_map";

    public static PrismServer getByName(String name) {
        try (Jedis jedis = PRISM.getJedis()) {
            String data = jedis.hget(NAME_MAP_REDIS_KEY, name);

            if (data == null) {
                return null;
            } else {
                Gson gson = new Gson();
                return gson.fromJson(data, PrismServer.class);
            }
        }
    }

    public static PrismServer getByPort(Integer port) {
        try (Jedis jedis = PRISM.getJedis()) {
            String data = jedis.hget(PORT_MAP_REDIS_KEY, String.valueOf(port));

            if (data == null) {
                return null;
            } else {
                Gson gson = new Gson();
                return gson.fromJson(data, PrismServer.class);
            }
        }
    }

    public static List<PrismServer> getServerList() {
        Gson gson = new Gson();
        List<PrismServer> servers = new LinkedList<>();

        try (Jedis jedis = PRISM.getJedis()) {
            for (String data : jedis.hvals(NAME_MAP_REDIS_KEY)) {
                servers.add(gson.fromJson(data, PrismServer.class));
            }
        }

        return servers;
    }

    String serverName;
    int port;

    public PrismServer(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
    }

    public void register() {
        try (Jedis jedis = PRISM.getJedis()) {
            Gson gson = new Gson();
            String data = gson.toJson(this);

            jedis.hset(NAME_MAP_REDIS_KEY, getServerName(), data);
            jedis.hset(PORT_MAP_REDIS_KEY, String.valueOf(getPort()), data);
        }
    }

    public void unregister() {
        try (Jedis jedis = PRISM.getJedis()) {
            jedis.hdel(NAME_MAP_REDIS_KEY, getServerName());
            jedis.hdel(PORT_MAP_REDIS_KEY, String.valueOf(getPort()));
        }
    }

    public String getServerName() {
        return serverName;
    }

    public int getPort() {
        return port;
    }

    public List<PrismPlayer> getPlayers() {
        List<PrismPlayer> players = new LinkedList<>();

        try (Jedis jedis = PRISM.getJedis()) {
            Gson gson = new Gson();

            String redisKey = "prism:prism_object:prism_player_name_map:" + getServerName();
            Map<String, String> map = jedis.hgetAll(redisKey);

            for (String data : map.values()) {
                PrismPlayer prismPlayer = gson.fromJson(data, PrismPlayer.class);
                players.add(prismPlayer);
            }
        }

        return players;
    }

    public boolean isWhitelist() {
        try {
            return PS_GetIsWhitelistRequester.request(this).get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isOnline() {
        try {
            return PS_GetIsWhitelistRequester.request(this).get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            return false;
        }
    }

}
