package io.github.wamel04.prism.prism_object.subscriber;

import io.github.wamel04.prism.bukkit.event.EventCaller;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;

public class ProxyDisconnectSubscriber extends Subscriber {

    public ProxyDisconnectSubscriber() {
        super("proxy_disconnect_subscriber", "proxy_disconnect", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                try {
                    Class.forName("org.bukkit.plugin.java.JavaPlugin");
                    EventCaller.call("proxy_disconnect_event", message);
                } catch (ClassNotFoundException e) {
                }
            }
        });
    }

}
