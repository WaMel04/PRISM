package io.github.wamel04.prism.bukkit;

import io.github.wamel04.prism.bukkit.mysql.DbConnection;
import io.github.wamel04.prism.bukkit.mysql.MysqlConfig;
import io.github.wamel04.prism.bukkit.redis.RedisConfig;
import io.github.wamel04.prism.subscriber.Subscriber;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Connection;
import java.sql.SQLException;

public final class BukkitInitializer extends JavaPlugin {

    private static BukkitInitializer instance;
    private static JedisPool pool;
    private static DbConnection dbConnection;

    @Override
    public void onEnable() {
        instance = this;

        RedisConfig.load();
        MysqlConfig.load();

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

        dbConnection = new DbConnection();

        getCommand("rm_test").setExecutor(new RMCommand());
    }

    @Override
    public void onDisable() {
        for (Subscriber subscriber : Subscriber.getSubscriberMap().values()) {
            subscriber.unregister();
        }

        pool.close();
        dbConnection.closeConnection();
    }

    public static BukkitInitializer getInstance() {
        return instance;
    }

    public static JedisPool getPool() {
        return pool;
    }

    public static Connection getConnection() {
        try {
            return dbConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
