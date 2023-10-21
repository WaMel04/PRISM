package io.github.wamel04.prism.proxy.listener;

import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.util.ProtocolMessageConvertor;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyBukkitEventListener implements Listener {

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.isCancelled())
            return;

        ProxiedPlayer player = event.getPlayer();

        if (event.getTarget() == null)
            return;

        String destinationServerName = event.getTarget().getName();

        if (event.getPlayer().getServer() == null) {
            // destinationServerName | uuid | nickname
            Subscriber.getSubscriber("proxy_connect_subscriber").publish(ProtocolMessageConvertor.convert(destinationServerName, player.getUniqueId().toString(), player.getName()));
        } else {
            String originServerName = event.getPlayer().getServer().getInfo().getName();

            if (originServerName.equals(destinationServerName))
                return;

            // originServerName | destinationServerName | uuid | nickname
            Subscriber.getSubscriber("server_change_subscriber").publish(ProtocolMessageConvertor.convert(originServerName, destinationServerName, player.getUniqueId().toString(), player.getName()));
        }
    }

    @EventHandler
    public void onProxyDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.getServer() == null)
            return;

        String originServerName = event.getPlayer().getServer().getInfo().getName();

        // originServerName | uuid | nickname
        Subscriber.getSubscriber("proxy_disconnect_subscriber").publish(ProtocolMessageConvertor.convert(originServerName, player.getUniqueId().toString(), player.getName()));
    }


}
