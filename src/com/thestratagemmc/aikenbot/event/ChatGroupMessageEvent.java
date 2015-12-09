package com.thestratagemmc.aikenbot.event;

import com.thestratagemmc.aikenbot.chat.ChatMessage;

/**
 * Created by axel on 11/29/15.
 */
public class ChatGroupMessageEvent extends ChatMessageEvent {

    private String chatName;

    public ChatGroupMessageEvent(ChatMessage message, String chatName) {
        super(message);
        this.chatName = chatName;
    }

    public String getChatName() {
        return chatName;
    }
}
