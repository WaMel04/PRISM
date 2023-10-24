package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PP_SendActionBarReceiver extends Subscriber {

    public PP_SendActionBarReceiver() {
        super("pp_send_actionbar_receiver", "pp_send_actionbar_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    if (player == null)
                        return;

                    String context = split[2];
                    player.sendActionBar(context);
                }
            }
        });
    }

}
