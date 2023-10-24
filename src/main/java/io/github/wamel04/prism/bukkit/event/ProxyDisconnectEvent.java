package io.github.wamel04.prism.bukkit.event;

import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import io.github.wamel04.prism.prism_object.ingame.server.PrismServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProxyDisconnectEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    PrismServer originServer;
    PrismPlayer prismPlayer;

    public ProxyDisconnectEvent(String message) {
        String[] split = message.split("\\|");
        originServer = PrismServer.getByName(split[0]);
        prismPlayer = new PrismPlayer(UUID.fromString(split[1]), split[2]);
    }

    public PrismServer getOriginServer() {
        return originServer;
    }

    public PrismPlayer getPrismPlayer() {
        return prismPlayer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
