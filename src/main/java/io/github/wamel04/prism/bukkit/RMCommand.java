package io.github.wamel04.prism.bukkit;

import io.github.wamel04.prism.example.CustomLocation;
import io.github.wamel04.prism.example.House;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RMCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.isOp())
            return false;
        if (!(commandSender instanceof Player))
            return false;

        Player player = (Player) commandSender;

        House house = new House(player.getUniqueId().toString(), new CustomLocation(0, 0, 0, "null"));
        house.load(house.getOwnerUuid()).thenRun(() -> {
            CustomLocation customLocation = house.getCustomLocation();

            commandSender.sendMessage("§c"+ customLocation.getX() + " " + customLocation.getY() + " " + customLocation.getZ() + " " + customLocation.getWorldName());
            customLocation.setX(customLocation.getX() + 1);
            customLocation.setY(customLocation.getY() + 1);
            customLocation.setZ(customLocation.getZ() + 1);
            commandSender.sendMessage("§a" + customLocation.getX() + " " + customLocation.getY() + " " + customLocation.getZ() + " " + customLocation.getWorldName());

            house.setCustomLocation(customLocation);
            house.save(house.getOwnerUuid());
        });

        return false;
    }

}
