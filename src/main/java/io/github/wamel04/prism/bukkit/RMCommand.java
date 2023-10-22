package io.github.wamel04.prism.bukkit;

import io.github.wamel04.prism.prism_object.PrismLocation;
import io.github.wamel04.prism.prism_object.PrismPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RMCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp())
            return false;
        if (!(sender instanceof Player))
            return false;
        if (args.length == 0)
            return false;

        Player player = (Player) sender;
        String target = args[0];

        PrismPlayer pPlayer = new PrismPlayer(player.getUniqueId(), player.getName());
        PrismPlayer pTarget = PrismPlayer.getByName(target);

        if (pTarget == null) {
            player.sendMessage(args[0] + " 님은 존재하지 않는 플레이어입니다.");
            return false;
        }

        PrismLocation prismLocation = pTarget.getPrismLocation();

        Bukkit.broadcastMessage(prismLocation.getPrismWorld().getPrismServer().getServerName() + " " + prismLocation.getPrismWorld().getWorldName() + " " +
                prismLocation.getX() + " " + prismLocation.getY() + " " + prismLocation.getZ() + " " + prismLocation.getPitch() + " " + prismLocation.getYaw());
        pPlayer.teleport(pTarget.getPrismLocation());

        pPlayer.sendMessage(pTarget.getName() + "님에게 텔레포트합니다!");
        pTarget.sendMessage(pPlayer.getName() + "님이 당신에게 텔레포트했습니다.");

        return false;
    }

}
