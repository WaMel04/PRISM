package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.PRISM;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import redis.clients.jedis.Jedis;

public class PP_ConnectReceiver implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        try (Jedis jedis = PRISM.getJedis()) {
            jedis.publish("prism_pp_connect_receive_" + player.getUniqueId(), "success");
        }
    }
}
