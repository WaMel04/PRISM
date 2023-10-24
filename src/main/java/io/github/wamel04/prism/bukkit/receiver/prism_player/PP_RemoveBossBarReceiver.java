package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PP_RemoveBossBarReceiver extends Subscriber {

    public PP_RemoveBossBarReceiver() {
        super("pp_remove_bossbar_receiver", "pp_remove_bossbar_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    if (player == null)
                        return;

                    String id = split[2];

                    if (PP_SendBossBarReceiver.playerBarMap.containsKey(player.getUniqueId())) {
                        PP_SendBossBarReceiver.PlayerBarData barData = PP_SendBossBarReceiver.playerBarMap.get(player.getUniqueId());
                        barData.remove(id);

                        PP_SendBossBarReceiver.playerBarMap.put(player.getUniqueId(), barData);
                    }
                }
            }
        });
    }


}
