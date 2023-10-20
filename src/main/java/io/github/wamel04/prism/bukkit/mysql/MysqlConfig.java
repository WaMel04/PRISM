package io.github.wamel04.prism.bukkit.mysql;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MysqlConfig {

    private static BukkitInitializer plugin = BukkitInitializer.getInstance();

    private static final String CONFIG_NAME = "bukkit_mysql_config.yml";

    public static String ip;
    public static String port;
    public static String database;
    public static String username;
    public static String password;
    public static Integer poolsize;

    public static void create() {
        try {
            File dataFolder = plugin.getDataFolder();

            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }

            File configFile = new File(dataFolder, CONFIG_NAME);

            if (!configFile.exists()) {
                FileOutputStream outputStream = new FileOutputStream(configFile);
                InputStream in = plugin.getResource(CONFIG_NAME);
                in.transferTo(outputStream);

                plugin.getLogger().info(CONFIG_NAME + "을 생성했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object get(String key) {
        File file = new File(plugin.getDataFolder(), CONFIG_NAME);
        try {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            return configuration.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void load() {
        create();

        ip = (String) get("ip");
        port = (String) get("port");
        database = (String) get("database");
        username = (String) get("username");
        password = (String) get("password");
        poolsize = (Integer) get("poolsize");
    }

}
