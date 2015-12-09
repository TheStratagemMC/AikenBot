package com.thestratagemmc.aikenbot;

import com.google.common.eventbus.EventBus;
import com.thestratagemmc.aikenbot.event.Event;
import com.thestratagemmc.aikenbot.plugins.Plugin;
import com.thestratagemmc.aikenbot.provider.ServiceProvider;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by axel on 11/29/15.
 */
public class AikenBot {

    private static ServiceProvider provider;

    private static ClassLoader cl;
    private static String CLASSPATH_DIR = "lib";
    private static String JAR_EXT = ".jar";
    private static String PLUGIN_DIR = "bot-plugins";
    private static List<String> pluginMainClassFiles = new ArrayList<>();
    private static List<Plugin> plugins = new ArrayList<>();
    private static EventBus eventBus;

    static{
        setupFiles();
    }

    public static void setupClassLoader(ClassLoader loader){
        try {
            System.out.println("Loading libraries...");

            cl = getPathClassLoader(
                    loader, new File(PLUGIN_DIR), new File(CLASSPATH_DIR)
            );
            Thread.currentThread().setContextClassLoader(cl);

            System.out.println("Class loader set!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void setupFiles(){
        File pluginDir = new File(PLUGIN_DIR);
        if (!pluginDir.exists()){
            pluginDir.mkdir();
        }
        File libDir = new File(CLASSPATH_DIR);
        if (!libDir.exists()){
            libDir.mkdir();
        }
    }

    // Returns a ClassLoader that for the provided path.
    private static ClassLoader getPathClassLoader(ClassLoader parent, File pluginDir, File libDir) throws Exception {
        // get jar files from jarPath
        File[] libJarFiles = libDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                System.out.println("Found: "+file);
                return file.getName().endsWith(JAR_EXT);
            }
        });

        File[] pluginJarFiles = pluginDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(JAR_EXT);
            }
        });

        pluginMainClassFiles = new ArrayList<>();
        for (File file : pluginJarFiles){ //extra step
            InputStream is = new FileInputStream(file);
            ZipInputStream zs = new ZipInputStream(is);

            ZipEntry entry;

            while((entry = zs.getNextEntry())!=null)
            {
                if (entry.getName().toLowerCase().equals("main.txt")){
                    Scanner sc = new Scanner(zs);
                    pluginMainClassFiles.add(sc.nextLine());
                }
            }
        }


        URL[] classpath = new URL[pluginJarFiles.length];
        /*for (int j = 0; j < libJarFiles.length; j++) {
            classpath[j] = libJarFiles[j].toURI().toURL();
            System.out.println("Adding: "+libJarFiles[j]);
        }*/
        for (int h = 0; h < pluginJarFiles.length; h++){
            classpath[h] = pluginJarFiles[h].toURI().toURL();
        }

        return new URLClassLoader(classpath, parent);
    }

    public static URL[] loadPluginJars(File pluginDir) throws Exception{
        File[] pluginJarFiles = pluginDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(JAR_EXT);
            }
        });

        pluginMainClassFiles = new ArrayList<>();
        for (File file : pluginJarFiles){ //extra step
            InputStream is = new FileInputStream(file);
            ZipInputStream zs = new ZipInputStream(is);

            ZipEntry entry;

            while((entry = zs.getNextEntry())!=null)
            {
                if (entry.getName().toLowerCase().equals("main.txt")){
                    Scanner sc = new Scanner(zs);
                    pluginMainClassFiles.add(sc.nextLine());
                }
            }
        }


        URL[] classpath = new URL[pluginJarFiles.length];
        /*for (int j = 0; j < libJarFiles.length; j++) {
            classpath[j] = libJarFiles[j].toURI().toURL();
            System.out.println("Adding: "+libJarFiles[j]);
        }*/
        for (int h = 0; h < pluginJarFiles.length; h++){
            classpath[h] = pluginJarFiles[h].toURI().toURL();
        }

        return classpath;
    }

    private static void enablePlugins() throws Exception{
        for (String mainClass : pluginMainClassFiles){
            Plugin plugin = Plugin.class.cast(Class.forName(mainClass, true, cl).newInstance());
            plugins.add(plugin);
        }

        for (Plugin plugin : plugins){
            plugin.onEnable();
        }
    }
    public static void setProvider(ServiceProvider provider){
        AikenBot.provider = provider;
    }

    public static void start(){
        eventBus = new EventBus();



        provider.start();

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
            public void run(){
                disable();
            }
        }));




        System.out.println("Using provider: "+provider+", alternate plugin loader: "+provider.useAlternatePluginLoader());
        try{
            if (provider.useAlternatePluginLoader()){
                provider.loadPlugins(loadPluginJars(new File(PLUGIN_DIR)));
            }
            else{
                setupClassLoader(Thread.currentThread().getContextClassLoader());
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            enablePlugins();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void registerListener(Object listener){
        eventBus.register(listener);
    }
    public static void unregisterListener(Object listener){
        eventBus.unregister(listener);
    }

    public static void callEvent(Event event){
        eventBus.post(event);
    }

    public static void disable(){
        provider.disable();
        for (Plugin plugin : plugins){
            plugin.onDisable();
        }
    }
}
