package io.github.wamel04.redismanager.bukkit;

import io.github.wamel04.redismanager.bukkit.redis.RedisConfig;
import io.github.wamel04.redismanager.subscriber.Subscriber;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class BukkitInitializer extends JavaPlugin {

    private static BukkitInitializer instance;
    private static JedisPool pool;

    @Override
    public void onEnable() {
        instance = this;

        RedisConfig.load();

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(0);
        jedisPoolConfig.setMaxTotal(300 * 8);
        jedisPoolConfig.setMaxWaitMillis(-1);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);

        if (RedisConfig.password.equals(""))
            pool = new JedisPool(jedisPoolConfig, RedisConfig.ip, Integer.parseInt(RedisConfig.port), 1000 * 15);
        else
            pool = new JedisPool(jedisPoolConfig, RedisConfig.ip, Integer.parseInt(RedisConfig.port), 1000 * 15, RedisConfig.password);

        getCommand("rm_test").setExecutor(new RMCommand());
    }

    @Override
    public void onDisable() {
        for (Subscriber subscriber : Subscriber.getSubscriberMap().values()) {
            subscriber.unregister();
        }

        pool.close();
    }

    public static BukkitInitializer getInstance() {
        return instance;
    }

    public static JedisPool getPool() {
        return pool;
    }

}
