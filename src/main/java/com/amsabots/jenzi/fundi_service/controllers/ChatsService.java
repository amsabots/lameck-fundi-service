package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.entities.Chats;
import com.amsabots.jenzi.fundi_service.entities.ConnectedUsers;
import com.amsabots.jenzi.fundi_service.repos.ChatRepo;
import com.amsabots.jenzi.fundi_service.repos.ConnectedUsersRepo;
import com.amsabots.jenzi.fundi_service.utils.ChatsObjects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@RestController
@RequestMapping("/chats")
@AllArgsConstructor
@Slf4j
public class ChatsService {
    private ChatRepo repo;
    private RabbitTemplate rabbitTemplate;
    private ConnectedUsersRepo c_repo;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatsObjects>> findFirstConversationMessages(
            @RequestParam(required = true) String sourceId,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page) {
        List<ChatsObjects> objectsList = new ArrayList<>();

        int l = limit.orElse(30);
        int pa = page.orElse(0);
        List<ConnectedUsers> cu = c_repo.findAllBySourceId(sourceId);
        if (cu.size() > 0) {
            Pageable p = PageRequest.of(pa, l);
            cu.forEach(e -> {
                List<Chats> c = repo
                        .findAllBySourceIdAndDestinationId(sourceId, e.getDestinationId(), p)
                        .getContent();
                ChatsObjects chatsObjects = new ChatsObjects(e.getDestinationId(),
                        c);
                objectsList.add(chatsObjects);
            });
        }
        return ResponseEntity.ok().body(objectsList);

    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chats> createChat(@RequestBody Chats chats) {
        ConnectedUsers c = new ConnectedUsers();
        c.setDestinationId(chats.getDestinationId());
        c.setSourceId(chats.getSourceId());
        rabbitTemplate.convertAndSend(ConfigConstants.CONNECT_USERS_KEY, c);
        rabbitTemplate.convertAndSend(ConfigConstants.OUT_GOING_MESSAGE_KEY, c);
        return ResponseEntity.ok(repo.save(chats));
    }
}
