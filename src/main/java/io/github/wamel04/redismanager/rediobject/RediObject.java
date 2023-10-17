package io.github.wamel04.redismanager.rediobject;

import com.google.gson.Gson;
import io.github.wamel04.redismanager.RedisManager;
import redis.clients.jedis.Jedis;

public interface RediObject {

    default Object bukkitLoad(String key) {
        Gson gson = new Gson();

        try (Jedis jedis = RedisManager.getBukkitJedis()) {
            String data = jedis.get(key);
            return gson.fromJson(data, this.getClass());
        }
    }

    default void bukkitSave(String key) {
        Gson gson = new Gson();

        try (Jedis jedis = RedisManager.getBukkitJedis()) {
            jedis.set(key, gson.toJson(this));
        }
    }

    default Object proxyLoad(String key) {
        Gson gson = new Gson();

        try (Jedis jedis = RedisManager.getProxyJedis()) {
            String data = jedis.get(key);
            return gson.fromJson(data, this.getClass());
        }
    }

    default void proxySave(String key) {
        Gson gson = new Gson();

        try (Jedis jedis = RedisManager.getProxyJedis()) {
            jedis.set(key, gson.toJson(this));
        }
    }

}
