package io.github.wamel04.prism.example;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class Example {

    public void run(Player player) {
        House house = new House(player.getUniqueId().toString(), new CustomLocation(0, 0, 0, "null"));
        house.load(house.getOwnerUuid(), 3, TimeUnit.SECONDS)
                .thenRun(() -> {
                    CustomLocation customLocation = house.getCustomLocation();

                    player.sendMessage("§c"+ customLocation.getX() + " " + customLocation.getY() + " " + customLocation.getZ() + " " + customLocation.getWorldName());
                    customLocation.setX(customLocation.getX() + 1);
                    customLocation.setY(customLocation.getY() + 1);
                    customLocation.setZ(customLocation.getZ() + 1);
                    player.sendMessage("§a" + customLocation.getX() + " " + customLocation.getY() + " " + customLocation.getZ() + " " + customLocation.getWorldName());

                    house.setCustomLocation(customLocation);
                    house.save(house.getOwnerUuid());
                })
                .exceptionally(ex -> {
                    Bukkit.broadcastMessage("load failure...");
                    return null;
                });
    }

}
