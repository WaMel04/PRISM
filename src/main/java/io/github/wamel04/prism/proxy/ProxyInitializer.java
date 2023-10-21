package io.github.wamel04.prism.proxy;

import io.github.wamel04.prism.prism_object.PrismServer;
import io.github.wamel04.prism.prism_object.subscriber_register.ProxyPrismSubscriberRegister;
import io.github.wamel04.prism.proxy.listener.ProxyBukkitEventListener;
import io.github.wamel04.prism.proxy.listener.ProxyDatabaseListener;
import io.github.wamel04.prism.proxy.mysql.DbConnection;
import io.github.wamel04.prism.proxy.mysql.MysqlConfig;
import io.github.wamel04.prism.proxy.receiver_register.ReceiverRegister;
import io.github.wamel04.prism.proxy.redis.RedisConfig;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.sql.Connection;
import java.sql.SQLException;

public class ProxyInitializer extends Plugin {

    private static ProxyInitializer instance;
    private static JedisPool pool;
    private static DbConnection dbConnection;

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
            pool = new JedisPool(jedisPoolConfig, "localhost", Integer.parseInt(RedisConfig.port), 1000 * 15);
        else
            pool = new JedisPool(jedisPoolConfig, "localhost", Integer.parseInt(RedisConfig.port), 1000 * 15, RedisConfig.password);

        try (Jedis jedis = pool.getResource()) {
            ScanParams scanParams = new ScanParams();
            scanParams.match("prism:*");

            String cursor = "0";
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                for (String key : scanResult.getResult()) {
                    jedis.del(key);
                }
                cursor = scanResult.getCursor();
            } while (!cursor.equals("0"));
        }
        try (Jedis jedis = pool.getResource()) {
            ScanParams scanParams = new ScanParams();
            scanParams.match("prism_data:*");

            String cursor = "0";
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                for (String key : scanResult.getResult()) {
                    jedis.del(key);
                }
                cursor = scanResult.getCursor();
            } while (!cursor.equals("0"));
        }

        ProxyPrismSubscriberRegister.start();
        ReceiverRegister.start();

        dbConnection = new DbConnection();

        initEventListeners();

        for (ServerInfo serverInfo : getProxy().getServersCopy().values()) {
            PrismServer server = new PrismServer(serverInfo.getName(), serverInfo.getAddress().getPort());
            server.register();
        }
    }

    public void onDisable() {
        pool.close();
        dbConnection.closeConnection();
    }

    public static ProxyInitializer getInstance() {
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

    private void initEventListeners() {
        getProxy().getPluginManager().registerListener(this, new ProxyDatabaseListener());
        getProxy().getPluginManager().registerListener(this, new ProxyBukkitEventListener());
    }

}
