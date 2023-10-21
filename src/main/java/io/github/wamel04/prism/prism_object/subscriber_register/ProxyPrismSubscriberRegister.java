package io.github.wamel04.prism.prism_object.subscriber_register;

import io.github.wamel04.prism.proxy.ProxyInitializer;
import io.github.wamel04.prism.subscriber.Subscriber;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

        try {
            Plugin pluginObject = plugin.getProxy().getPluginManager().getPlugin(plugin.getProxy().getName());
            Method getFileMethod = Plugin.class.getDeclaredMethod("getFile");
            getFileMethod.setAccessible(true);
            File file = (File) getFileMethod.invoke(pluginObject);
            JarFile jarFile = new JarFile(file);

            for (Enumeration<JarEntry> entry = jarFile.entries(); entry.hasMoreElements();) {
                JarEntry jarEntry = entry.nextElement();
                String name = jarEntry.getName().replace("/", ".");

                if (name.startsWith(packageName)) {
                    if (name.endsWith(".class") && !name.contains("$")) { // 내부 클래스 포함 X
                        classes.add(Class.forName(name.substring(0, name.length() - 6)));
                        continue;
                    } if (!name.endsWith(".class")) { // 재귀적으로 해당 패키지에 위치한 모든 클래스를 불러 옴
                        classes.addAll(getClasses(name));
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

            Subscriber subscriber = (Subscriber) instance;
            subscriber.register();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
