package io.github.wamel04.prism.prism_object.subscriber;

import io.github.wamel04.prism.bukkit.event.EventCaller;
import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;

public class ServerChangeSubscriber extends Subscriber {

    public ServerChangeSubscriber() {
        super("server_change_subscriber", "server_change", new SubscriberRunnable() {
            @Override
            public void run(String channelName, String message) {
                EventCaller.call("server_change_event", message);
            }
        });
    }

}
