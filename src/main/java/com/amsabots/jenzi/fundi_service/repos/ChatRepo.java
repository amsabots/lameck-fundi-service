package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.Chats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Repository
public interface ChatRepo extends JpaRepository<Chats, Long> {

    Page<Chats> findAllByChatRoomId(String chatRoomId, Pageable pageable);
    public Chats findChatsByMessageId(String messageId);
    Chats findTopByOrderByChatRoomIdDesc(String roomId);

}
