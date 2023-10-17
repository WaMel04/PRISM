package io.github.wamel04.redismanager.proxy;

import io.github.wamel04.redismanager.proxy.redis.RedisConfig;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

public class ProxyInitializer extends Plugin {

    private static ProxyInitializer instance;
    private static JedisPool pool;

    public void onEnable() {
        instance = this;
        RedisConfig.load();

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(0);
        jedisPoolConfig.setMaxTotal(300 * 8);
        jedisPoolConfig.setMaxWaitMillis(-1);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);

        pool = new JedisPool(jedisPoolConfig, "localhost", Integer.parseInt(RedisConfig.port), 1000 * 15, RedisConfig.password);

        try (Jedis jedis = pool.getResource()) {
            ScanParams scanParams = new ScanParams();
            scanParams.match("redis_manager:*");

            String cursor = "0";
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                for (String key : scanResult.getResult()) {
                    jedis.del(key);
                }
                cursor = scanResult.getCursor();
            } while (!cursor.equals("0"));
        }
    }

    public void onDisable() {

    }

    public static ProxyInitializer getInstance() {
        return instance;
    }

    public static JedisPool getPool() {
        return pool;
    }

}
