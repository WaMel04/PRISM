package io.github.wamel04.prism;

import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.concurrent.CompletableFuture;

public class DbManager {

    public static void loadAll(String uuid) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = PRISM.getConnection()) {
                DatabaseMetaData dbMetaData = connection.getMetaData();

                try (ResultSet tables = dbMetaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"})) {
                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME");
                        String query = "SELECT data FROM " + tableName + " WHERE uuid = ?";

                        try (PreparedStatement stmt = connection.prepareStatement(query)) {
                            stmt.setString(1, uuid);

                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    String data = rs.getString("data");

                                    try (Jedis jedis = PRISM.getJedis()) {
                                        jedis.publish("prism:" + tableName.replace("ㅣ", ":") + ":" + uuid, data);
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    public static CompletableFuture<String> getAsync(String uuid, String tableName) {
        return CompletableFuture.supplyAsync(() -> {
            String value = null;

            try (Connection connection = PRISM.getConnection()) {
                String selectSQL = "SELECT data FROM " + tableName + " WHERE uuid = ?";

                try (PreparedStatement stmt = connection.prepareStatement(selectSQL)) {
                    stmt.setString(1, uuid);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            value = rs.getString("data");
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return value;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    public static void save(String uuid, String data, String tableName) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = PRISM.getConnection()) {
                String createTableSQL = "CREATE TABLE if not exists " + tableName + " (uuid VARCHAR(36) PRIMARY KEY, data TEXT)";
                PreparedStatement stmt1 = connection.prepareStatement(createTableSQL);
                stmt1.executeUpdate();
                stmt1.close();

                String checkUUIDSQL = "SELECT uuid FROM " + tableName + " WHERE uuid = ?";

                try (PreparedStatement stmt2 = connection.prepareStatement(checkUUIDSQL)) {
                    stmt2.setString(1, uuid);

                    try (ResultSet rs = stmt2.executeQuery()) {
                        if (rs.next()) {
                            String updateSQL = "UPDATE " + tableName + " SET data = ? WHERE uuid = ?";

                            try (PreparedStatement updateStmt = connection.prepareStatement(updateSQL)) {
                                updateStmt.setString(1, data);
                                updateStmt.setString(2, uuid);
                                updateStmt.executeUpdate();
                            }
                        } else {
                            String insertSQL = "INSERT INTO " + tableName + " (uuid, data) VALUES (?, ?)";

                            try (PreparedStatement insertStmt = connection.prepareStatement(insertSQL)) {
                                insertStmt.setString(1, uuid);
                                insertStmt.setString(2, data);
                                insertStmt.executeUpdate();
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

    public static void clearAll(String uuid) {
        CompletableFuture.runAsync(() -> {
            try (Connection connection = PRISM.getConnection()) {
                DatabaseMetaData dbMetaData = connection.getMetaData();

                try (ResultSet tables = dbMetaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"})) {
                    while (tables.next()) {
                        String tableName = tables.getString("TABLE_NAME").replace("ㅣ", ":");
                        String redisKey = "prism_data:" + tableName + ":" + uuid;

                        try (Jedis jedis = PRISM.getJedis()) {
                            jedis.del(redisKey);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

}
