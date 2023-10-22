package io.github.wamel04.prism.prism_object.subscriber;

import io.github.wamel04.prism.bukkit.event.EventCaller;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;

public class ProxyConnectSubscriber extends Subscriber {

    public ProxyConnectSubscriber() {
        super("proxy_connect_subscriber", "proxy_connect", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                try {
                    Class.forName("org.bukkit.plugin.java.JavaPlugin");
                    EventCaller.call("proxy_connect_event", message);
                } catch (ClassNotFoundException e) {
                }
            }
        });
    }

}
