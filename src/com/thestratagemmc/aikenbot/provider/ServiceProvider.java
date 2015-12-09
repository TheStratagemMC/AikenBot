package com.thestratagemmc.aikenbot.provider;

import java.net.URL;

/**
 * Created by axel on 11/29/15.
 */
public abstract class ServiceProvider {
    public void start(){}

    public void disable(){}

    public boolean useAlternatePluginLoader(){
        return false;
    }


    public void loadPlugins(URL[] plugins){}
}
