package io.github.wamel04.prism.bukkit.receiver_register;

import io.github.wamel04.prism.bukkit.BukkitInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReceiverRegister {

    private static BukkitInitializer plugin = BukkitInitializer.getInstance();

    public static void start() {
        Set<Class<?>> classes = getClasses("io.github.wamel04.prism.bukkit.receiver");

        for (Class clazz : classes) {
            register(clazz);
        }
    }

    private static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();

        try {
            JavaPlugin pluginObject = (JavaPlugin) Bukkit.getServer().getPluginManager().getPlugin(plugin.getName());
            Method getFileMethod = JavaPlugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File) getFileMethod.invoke(pluginObject);

            try (JarFile jarFile = new JarFile(file)) {
                Enumeration<JarEntry> entries = jarFile.entries();

                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName().replace("/", ".");

                    if (entryName.endsWith(".class") && entryName.startsWith(packageName + ".") && !entryName.contains("$")) {
                        String className = entryName.substring(0, entryName.length() - 6); // ".class" 제거
                        Class<?> clazz = Class.forName(className);
                        classes.add(clazz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static void register(Class<? extends Subscriber> clazz) {
        try {
            Constructor constructor = clazz.getConstructors()[0];
            Object[] args = new Object[constructor.getParameterCount()];
            Object instance = constructor.newInstance(args);

            if (instance instanceof Subscriber) {
                Subscriber subscriber = (Subscriber) instance;
                subscriber.register();
            }
            if (instance instanceof Listener) {
                Listener listener = (Listener) instance;
                Bukkit.getServer().getPluginManager().registerEvents(listener, BukkitInitializer.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
