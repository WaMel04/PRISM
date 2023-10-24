package io.github.wamel04.prism.bukkit.receiver.prism_player;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PP_TeleportReceiver extends Subscriber {

    public PP_TeleportReceiver() {
        super("pp_teleport_receiver", "pp_teleport_request", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|\\|\\|");

                if (split[0].equals(BukkitInitializer.getPrismServer().getServerName())) {
                    Player player = Bukkit.getPlayer(UUID.fromString(split[1]));

                    if (player == null)
                        return;

                    String worldName = split[2];

                    if (Bukkit.getWorld(worldName) == null)
                        return;

                    double x = Double.parseDouble(split[3]);
                    double y = Double.parseDouble(split[4]);
                    double z = Double.parseDouble(split[5]);
                    float pitch = Float.parseFloat(split[6]);
                    float yaw = Float.parseFloat(split[7]);

                    Location location = new Location(Bukkit.getWorld(worldName), x, y, z, pitch, yaw);

                    Bukkit.getScheduler().runTask(BukkitInitializer.getInstance(), () -> player.teleport(location));
                }
            }
        });
    }

}
