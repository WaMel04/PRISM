package io.github.wamel04.prism.bukkit.listener;

import com.google.gson.Gson;
import io.github.wamel04.prism.PRISM;
import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.bukkit.event.ProxyDisconnectEvent;
import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import redis.clients.jedis.Jedis;

import java.util.concurrent.CompletableFuture;

public class PrismPlayerListener implements Listener {

    private static String UUID_MAP_REDIS_KEY;
    private static String NAME_MAP_REDIS_KEY;
    private static String SERVER_MAP_REDIS_KEY;

    public PrismPlayerListener() {
         UUID_MAP_REDIS_KEY = "prism:prism_object:prism_player_uuid_map:" + BukkitInitializer.getPrismServer().getServerName();
         NAME_MAP_REDIS_KEY = "prism:prism_object:prism_player_name_map:" + BukkitInitializer.getPrismServer().getServerName();
         SERVER_MAP_REDIS_KEY = "prism:prism_object:prism_player_server_map";
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                Player player = event.getPlayer();
                PrismPlayer prismPlayer = new PrismPlayer(player.getUniqueId(), player.getName());

                Gson gson = new Gson();
                String data = gson.toJson(prismPlayer);

                jedis.hset(UUID_MAP_REDIS_KEY, player.getUniqueId().toString(), data);
                jedis.hset(NAME_MAP_REDIS_KEY, player.getName().toLowerCase(), data);
                jedis.hset(SERVER_MAP_REDIS_KEY, player.getUniqueId().toString(), BukkitInitializer.getPrismServer().getServerName());

                jedis.publish("prism:pp_connect_receive_" + player.getUniqueId(), "Success");
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                Player player = event.getPlayer();

                jedis.hdel(UUID_MAP_REDIS_KEY, player.getUniqueId().toString());
                jedis.hdel(NAME_MAP_REDIS_KEY, player.getName().toLowerCase());
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    @EventHandler
    public void onProxyDisconnect(ProxyDisconnectEvent event) {
        CompletableFuture.runAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                jedis.hdel(SERVER_MAP_REDIS_KEY, event.getPrismPlayer().getUuid().toString());
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

}
