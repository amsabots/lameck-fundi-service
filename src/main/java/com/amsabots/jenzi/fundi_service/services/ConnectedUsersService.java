package com.amsabots.jenzi.fundi_service.services;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.entities.ConnectedUsers;
import com.amsabots.jenzi.fundi_service.repos.ConnectedUsersRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Service
public class ConnectedUsersService {
    @Autowired
    private ConnectedUsersRepo repo;

    @RabbitListener(queues = ConfigConstants.CONNECT_USERS_QUEUE)
    private void createConnectionIfNotExists(ConnectedUsers connectedUsers) {
        ConnectedUsers c = repo.findConnectedUsersBySourceIdAndDestinationId(
                connectedUsers.getSourceId(), connectedUsers.getDestinationId()
        );
        if (null == c) repo.save(connectedUsers);
        //insert for the other way round
        ConnectedUsers c1 = repo.findConnectedUsersBySourceIdAndDestinationId(
                connectedUsers.getDestinationId(), connectedUsers.getSourceId()
        );
        if(null == c1){
            ConnectedUsers connectedUsers1 = new ConnectedUsers();
            connectedUsers1.setSourceId(connectedUsers.getDestinationId());
            connectedUsers1.setDestinationId(connectedUsers.getSourceId());
            repo.save(connectedUsers1);
        }

    }

}
