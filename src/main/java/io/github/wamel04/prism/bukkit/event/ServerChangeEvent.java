package io.github.wamel04.prism.bukkit.event;

import io.github.wamel04.prism.prism_object.ingame.entity.PrismPlayer;
import io.github.wamel04.prism.prism_object.ingame.server.PrismServer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ServerChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    PrismServer originServer;
    PrismServer destinationServer;
    PrismPlayer prismPlayer;

    public ServerChangeEvent(String message) {
        String[] split = message.split("\\|");
        originServer = PrismServer.getByName(split[0]);
        destinationServer = PrismServer.getByName(split[1]);
        prismPlayer = new PrismPlayer(UUID.fromString(split[2]), split[3]);
    }

    public PrismServer getOriginServer() {
        return originServer;
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
