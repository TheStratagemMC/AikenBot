package com.thestratagemmc.aikenbot.plugins;

/**
 * Created by axel on 11/29/15.
 */
public abstract class Plugin {

    public abstract String getName();
    public abstract String getAuthor();
    public abstract String getVersion();

    public void onEnable(){}

    public void onDisable(){}


}
