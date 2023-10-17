package io.github.wamel04.redismanager.rediobject;

import com.google.gson.Gson;
import io.github.wamel04.redismanager.RedisManager;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;

public interface RediObject {

    String getNameKey();

    default String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    default void load(String key) {
        Gson gson = new Gson();

        try (Jedis jedis = RedisManager.getJedis()) {
            String data = jedis.get("redis_manager:" + getNameKey() + ":" + key);
            RediObject loadedObject = gson.fromJson(data, getClass());

            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(this, field.get(loadedObject));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default void save(String key) {
        try (Jedis jedis = RedisManager.getJedis()) {
            jedis.set("redis_manager:" + getNameKey() + ":" + key, serialize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

