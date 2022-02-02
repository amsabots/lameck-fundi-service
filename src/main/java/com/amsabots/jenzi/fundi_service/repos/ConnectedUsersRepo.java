package com.amsabots.jenzi.fundi_service.repos;

import com.amsabots.jenzi.fundi_service.entities.ConnectedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
public interface ConnectedUsersRepo extends JpaRepository<ConnectedUsers, Long> {
    public ConnectedUsers findConnectedUsersBySourceIdAndDestinationId(String sourceId,
                                                                       String destinationId);

    public List<ConnectedUsers> findAllBySourceId(String sourceId);
}
