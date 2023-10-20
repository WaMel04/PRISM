package io.github.wamel04.prism;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.proxy.ProxyInitializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.sql.Connection;

public class PRISM {

    /**
     * 해당 쓰레드의 버킷 여부 를 반환합니다.
     * <p>
     *     true - 버킷에서 실행 중
     * </p>
     * <p>
     *     false - 프록시에서 실행 중
     * </p>
     * @return boolean
     */
    public static boolean isBukkit() {
        try {
            Class.forName("org.bukkit.plugin.java.JavaPlugin");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 해당 쓰레드의 적절한 JedisPool을 반환합니다.
     * @return JedisPool
     */
    public static JedisPool getJedisPool() {
        return isBukkit() ? BukkitInitializer.getPool() : ProxyInitializer.getPool();
    }

    /**
     * 해당 쓰레드의 적절한 Jedis를 반환합니다.
     * @return Jedis
     */
    public static Jedis getJedis() {
        return getJedisPool().getResource();
    }

    /**
     * 해당 쓰레드의 적절한 Connection을 반환합니다.
     * @return Connection
     */
    public static Connection getConnection() {
        return isBukkit() ? BukkitInitializer.getConnection() : ProxyInitializer.getConnection();
    }

    public static BukkitInitializer getBukkitInstance() {
        if (!isBukkit()) {
            System.err.println("[PRISM] 버킷에서만 사용할 수 있습니다.");
            return null;
        }

        return BukkitInitializer.getInstance();
    }

    public static ProxyInitializer getProxyInstance() {
        if (!isBukkit()) {
            System.err.println("[PRISM] 프록시에서만 사용할 수 있습니다.");
            return null;
        }

        return ProxyInitializer.getInstance();
    }

}
