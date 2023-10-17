package io.github.wamel04.redismanager;

import com.google.gson.Gson;
import io.github.wamel04.redismanager.bukkit.BukkitInitializer;
import io.github.wamel04.redismanager.proxy.ProxyInitializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisManager {

    public static boolean isBukkit() {
        try {
            Class.forName("org.bukkit.plugin.java.JavaPlugin");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static JedisPool getJedisPool() {
        return isBukkit() ? BukkitInitializer.getPool() : ProxyInitializer.getPool();
    }

    public static Jedis getJedis() {
        return getJedisPool().getResource();
    }

    public static Object loadObject(JedisPool jedisPool, String key, Class<?> clazz) {
        Gson gson = new Gson();

        try (Jedis jedis = jedisPool.getResource()) {
            String data = jedis.get(key);
            return gson.fromJson(data, clazz);
        }
    }

    public static void saveObject(JedisPool jedisPool, String key, Object object) {
        Gson gson = new Gson();

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, gson.toJson(object));
        }
    }

}
