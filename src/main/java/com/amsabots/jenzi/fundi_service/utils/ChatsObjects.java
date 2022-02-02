package com.amsabots.jenzi.fundi_service.utils;

import com.amsabots.jenzi.fundi_service.entities.Chats;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Data
@AllArgsConstructor
public class ChatsObjects {
    private String destinationId;
    private List<Chats> chatsList;
}
