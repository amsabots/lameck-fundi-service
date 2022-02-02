package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.entities.Chats;
import com.amsabots.jenzi.fundi_service.entities.ConnectedUsers;
import com.amsabots.jenzi.fundi_service.repos.ChatRepo;
import com.amsabots.jenzi.fundi_service.repos.ConnectedUsersRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
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
    public ResponseEntity<List<Chats>> findAllChartsConversation(
            @RequestParam(required = true) String sourceId,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page) {
        int l = limit.orElse(30);
        int pa = page.orElse(0);
        Pageable p = PageRequest.of(pa, l);
        List<Chats> c = repo.findAllBySourceId(sourceId, p)
                .getContent();
        return ResponseEntity.ok().body(c);

    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chats> createChat(@RequestBody Chats chats) {
        ConnectedUsers c = new ConnectedUsers();
        c.setDestinationId(chats.getDestinationId());
        c.setSourceId(chats.getSourceId());
        rabbitTemplate.convertAndSend(ConfigConstants.CONNECT_USERS_KEY, c);
        return ResponseEntity.ok(repo.save(chats));
    }
}
