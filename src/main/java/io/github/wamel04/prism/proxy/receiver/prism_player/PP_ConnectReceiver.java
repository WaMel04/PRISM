package io.github.wamel04.prism.proxy.receiver.prism_player;

import io.github.wamel04.prism.proxy.ProxyInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class PP_ConnectReceiver extends Subscriber {

    public PP_ConnectReceiver() {
        super("pp_connect_receiver", "pp_connect_request_proxy", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                String[] split = message.split("\\|");

                ServerInfo serverInfo = ProxyInitializer.getInstance().getProxy().getServerInfo(split[0]);
                ProxiedPlayer player = ProxyInitializer.getInstance().getProxy().getPlayer(UUID.fromString(split[1]));

                if (serverInfo != null && player != null) {
                    player.connect(serverInfo);
                }
            }
        });
    }

}
