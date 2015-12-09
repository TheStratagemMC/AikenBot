package com.thestratagemmc.aikenbot.event;

import com.thestratagemmc.aikenbot.chat.ChatMessage;

/**
 * Created by axel on 11/29/15.
 */
public class ChatMessageEvent extends Event{
    private ChatMessage message;

    public ChatMessageEvent(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }
}
