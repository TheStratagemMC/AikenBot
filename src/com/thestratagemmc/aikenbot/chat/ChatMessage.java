package com.thestratagemmc.aikenbot.chat;

/**
 * Created by axel on 11/29/15.
 */
public interface ChatMessage {
    public String getSender();
    public String getMessage();
    public void reply(String message);
    public void replySender(String message);
}
