package io.github.wamel04.prism.prism_object.subscriber_register;

import io.github.wamel04.prism.proxy.ProxyInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ProxyPrismSubscriberRegister {

    private static ProxyInitializer plugin = ProxyInitializer.getInstance();

    public static void start() {
        Set<Class<?>> classes = getClasses("io.github.wamel04.prism.prism_object.subscriber");

        for (Class clazz : classes) {
            register(clazz);
        }
    }

    private static Set<Class<?>> getClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();

        File file = plugin.getFile();

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

            Subscriber subscriber = (Subscriber) instance;
            subscriber.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
