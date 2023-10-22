package io.github.wamel04.prism.bukkit.event;

import io.github.wamel04.prism.prism_object.PrismPlayer;
import io.github.wamel04.prism.prism_object.PrismServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProxyConnectEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    PrismServer destinationServer;
    PrismPlayer prismPlayer;

    public ProxyConnectEvent(String message) {
        String[] split = message.split("\\|");
        destinationServer = PrismServer.getByName(split[0]);
        prismPlayer = new PrismPlayer(UUID.fromString(split[1]), split[2]);
    }

    public PrismServer getDestinationServer() {
        return destinationServer;
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
