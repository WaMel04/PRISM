package io.github.wamel04.redismanager.subscriber;

public interface SubscriberRunnable {

    void run(String channelName, String message);

}
