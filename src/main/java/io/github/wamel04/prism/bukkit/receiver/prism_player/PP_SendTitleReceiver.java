package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PP_SendTitleReceiver extends Subscriber {

    public PP_SendTitleReceiver() {
        super("pp_send_title_receiver", "pp_send_title_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    if (player == null)
                        return;

                    String title = split[2];
                    String subtitle = split[3];
                    int fadein = Integer.parseInt(split[4]);
                    int duration = Integer.parseInt(split[5]);
                    int fadeout = Integer.parseInt(split[6]);

                    player.sendTitle(title, subtitle, fadein, duration, fadeout);
                }
            }
        });
    }

}
