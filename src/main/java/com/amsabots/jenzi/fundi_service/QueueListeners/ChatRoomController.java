package com.amsabots.jenzi.fundi_service.QueueListeners;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.repos.ChatRoomsRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Service
public class ChatRoomController {
    @Autowired
    private ChatRoomsRepo repo;

    @RabbitListener(queues = ConfigConstants.CONNECT_USERS_QUEUE)
    private void createConnectionIfNotExists(ChatRooms chatRooms) {
        ChatRooms c = repo.findConnectedUsersBySourceIdAndDestinationId(
                chatRooms.getSourceId(), chatRooms.getDestinationId()
        );
        ChatRooms c1 = repo.findConnectedUsersBySourceIdAndDestinationId(
                chatRooms.getDestinationId(), chatRooms.getSourceId()
        );
        if (null != c && null != c1) return;
        //insert for the other way round
        repo.save(chatRooms);
        ChatRooms chatRooms1 = new ChatRooms();
        chatRooms1.setSourceId(chatRooms.getDestinationId());
        chatRooms1.setDestinationId(chatRooms.getSourceId());
        repo.save(chatRooms1);

    }

}
