package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.entities.Chats;
import com.amsabots.jenzi.fundi_service.repos.ChatRepo;
import com.amsabots.jenzi.fundi_service.repos.ChatRoomsRepo;
import com.amsabots.jenzi.fundi_service.utils.ChatsObjects;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
public class ChatsController {
    private ChatRepo repo;
    private RabbitTemplate rabbitTemplate;
    private ChatRoomsRepo c_repo;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatsObjects>> findFirstConversationMessages(
            @RequestParam(required = true) String sourceId,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page) {
        List<ChatsObjects> objectsList = new ArrayList<>();

        int l = limit.orElse(30);
        int pa = page.orElse(0);
        List<ChatRooms> cu = c_repo.findAllBySourceId(sourceId);
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
        ChatRooms c = new ChatRooms();
        c.setDestinationId(chats.getDestinationId());
        c.setSourceId(chats.getSourceId());
        rabbitTemplate.convertAndSend(ConfigConstants.MESSAGE_EXCHANGE, ConfigConstants.CONNECT_USERS_KEY, c);
        Chats new_chat = repo.save(chats);
        // save the same to the destination id but change source id to destination id
        chats.setSourceId(chats.getDestinationId());
        chats.setDestinationId(chats.getSourceId());
        chats.setSignature("destination");
        repo.save(chats);
        rabbitTemplate.convertAndSend(ConfigConstants.MESSAGE_EXCHANGE, ConfigConstants.OUT_GOING_MESSAGE_KEY, new_chat);
        return ResponseEntity.ok(new_chat);
    }

    @PutMapping(path = "/dlr/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDLrReport(@PathVariable String messageId) {
        Chats chat = repo.findChatsByMessageId(messageId);
        chat.setDelivered(true);
        repo.save(chat);
        rabbitTemplate.convertAndSend(ConfigConstants.MESSAGE_EXCHANGE, ConfigConstants.DLR_MESSAGE_KEY, chat);
        return ResponseEntity.ok("{\"message\":\"DLR delivered\"}");
    }

    @GetMapping(path = "/dlr/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDLRReport(@PathVariable String messageId) {
        rabbitTemplate.convertAndSend(ConfigConstants.MESSAGE_EXCHANGE, ConfigConstants.REMOVE_DLR_KEY, messageId);
        return ResponseEntity.ok("done");
    }
}
