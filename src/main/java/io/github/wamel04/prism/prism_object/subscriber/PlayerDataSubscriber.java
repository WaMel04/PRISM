package io.github.wamel04.prism.prism_object.subscriber;

import io.github.wamel04.prism.subscriber.Subscriber;
import io.github.wamel04.prism.subscriber.SubscriberRunnable;

public class PlayerDataSubscriber extends Subscriber {

    public PlayerDataSubscriber(String id, String channelName, SubscriberRunnable subscriberRunnable) {
        super("player_data_subscriber", "player_data",
                new SubscriberRunnable() {
                    @Override
                    public void run(String channelName, String message) {
                        String split[] = message.split("\\|");

                    }
                });
    }

}
