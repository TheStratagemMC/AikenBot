package com.thestratagemmc.aikenbot.event;

import com.thestratagemmc.aikenbot.chat.ChatMessage;

/**
 * Created by axel on 11/29/15.
 */
public class ChatPersonalMessageEvent extends ChatMessageEvent {
    public ChatPersonalMessageEvent(ChatMessage message) {
        super(message);
    }
}
