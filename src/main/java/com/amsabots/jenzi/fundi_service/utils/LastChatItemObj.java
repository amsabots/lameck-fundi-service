package com.amsabots.jenzi.fundi_service.utils;

import com.amsabots.jenzi.fundi_service.entities.ChatRoomConnections;
import com.amsabots.jenzi.fundi_service.entities.Chats;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class LastChatItemObj {
    private ChatRoomConnections connection;
    private Chats lastMessage;
}
