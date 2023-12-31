package io.github.wamel04.prism.util;

import com.google.gson.Gson;
import io.github.wamel04.prism.DbManager;
import io.github.wamel04.prism.PRISM;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface PrismSerializable {

    String getNameKey();

    default String serialize() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    default CompletableFuture<Void> load(String key, long timeout, TimeUnit unit) {
        Gson gson = new Gson();
        String redisKey = "prism_data:" + getNameKey() + ":" + key;

        return getFromRedisAsync(redisKey)
                .thenCompose(data -> {
                    if (data != null) {
                        try {
                            populateFromData(data, gson);
                            return CompletableFuture.completedFuture(null);
                        } catch (Exception e) {
                            return CompletableFuture.failedFuture(e);
                        }
                    } else {
                        return loadFromMysqlAndCache(key, gson);
                    }
                })
                .orTimeout(timeout, unit);
    }

    private CompletableFuture<String> getFromRedisAsync(String key) {
        return CompletableFuture.supplyAsync(() -> {
            try (Jedis jedis = PRISM.getJedis()) {
                return jedis.get(key);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private CompletableFuture<Void> loadFromMysqlAndCache(String key, Gson gson) {
        return DbManager.getAsync(key, getNameKey().replace(":", "ㅣ")).thenCompose(data -> {
            String redisKey = "prism_data:" + getNameKey() + ":" + key;

            if (data != null) {
                try {
                    populateFromData(data, gson);

                    try (Jedis jedis = PRISM.getJedis()) {
                        jedis.set(redisKey, data);
                    }

                    return CompletableFuture.completedFuture(null);
                } catch (Exception e) {
                    e.printStackTrace();
                    return CompletableFuture.failedFuture(e);
                }
            } else {
                System.err.println("[PRISM] " + getNameKey() + ":" + key + " 로드에 실패하였습니다.");
                return CompletableFuture.failedFuture(new Exception("Failed to load data"));
            }
        });
    }

    private void populateFromData(String data, Gson gson) throws IllegalAccessException {
        PrismSerializable loadedObject = gson.fromJson(data, getClass());

        Field[] fields = getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            field.set(this, field.get(loadedObject));
        }
    }

    default void save(String key) {
        CompletableFuture.runAsync(() -> {
            String data = serialize();
            String redisKey = "prism_data:" + getNameKey() + ":" + key;

            try (Jedis jedis = PRISM.getJedis()) {
                jedis.set(redisKey, data);
            } catch (Exception e) {
                e.printStackTrace();
            }

            DbManager.save(key, data, getNameKey().replace(":", "ㅣ"));
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

}

