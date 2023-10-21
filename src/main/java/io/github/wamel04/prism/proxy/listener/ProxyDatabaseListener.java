package io.github.wamel04.prism.proxy.listener;

import io.github.wamel04.prism.DbManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyDatabaseListener implements Listener {

    @EventHandler
    public void onProxyConnect(ServerConnectEvent event) {
        if (event.isCancelled())
            return;

        ProxiedPlayer player = event.getPlayer();

        if (player.getServer() != null)
            return;

        DbManager.loadAll(player.getUniqueId().toString());
    }

    @EventHandler
    public void onProxyDisconnect(PlayerDisconnectEvent event) {
        DbManager.clearAll(event.getPlayer().getUniqueId().toString());
    }


}
