package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.ChatRoomConnections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
public interface ChatRoomsRepo extends JpaRepository<ChatRoomConnections, Long> {
    ChatRoomConnections getChatRoomConnectionsByPartyAAndPartyB(long partyA, long partyB);
    ChatRoomConnections getChatRoomConnectionsByPartyA(long partyA);
}
