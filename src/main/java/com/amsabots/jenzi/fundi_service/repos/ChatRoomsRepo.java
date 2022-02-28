package com.amsabots.jenzi.fundi_service.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
public interface ChatRoomsRepo extends JpaRepository<ChatRooms, Long> {
    public ChatRooms findConnectedUsersBySourceIdAndDestinationId(String sourceId,
                                                                  String destinationId);
    public List<ChatRooms> findAllBySourceId(String sourceId);
}
