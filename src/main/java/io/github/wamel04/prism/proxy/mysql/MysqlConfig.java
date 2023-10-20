package io.github.wamel04.prism.proxy.mysql;

import io.github.wamel04.prism.proxy.ProxyInitializer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MysqlConfig {

    private static ProxyInitializer plugin = ProxyInitializer.getInstance();

    private static final String CONFIG_NAME = "proxy_mysql_config.yml";

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

                try (InputStream in = plugin.getResourceAsStream(CONFIG_NAME)) {
                    in.transferTo(outputStream);
                }

                plugin.getLogger().info(CONFIG_NAME + "을 생성했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object get(String key) {
        File file = new File(plugin.getDataFolder(), CONFIG_NAME);

        try {
            Configuration configuration = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(file);

            return configuration.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void load() {
        create();

        port = (String) get("port");
        database = (String) get("database");
        username = (String) get("username");
        password = (String) get("password");
        poolsize = (Integer) get("poolsize");
    }

}
