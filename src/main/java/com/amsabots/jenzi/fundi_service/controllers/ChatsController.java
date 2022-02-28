package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.config.ConfigConstants;
import com.amsabots.jenzi.fundi_service.entities.ChatRoomConnections;
import com.amsabots.jenzi.fundi_service.entities.Chats;
import com.amsabots.jenzi.fundi_service.repos.ChatRepo;
import com.amsabots.jenzi.fundi_service.repos.ChatRoomsRepo;
import com.amsabots.jenzi.fundi_service.utils.ChatsObjects;
import com.amsabots.jenzi.fundi_service.utils.LastChatItemObj;
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
    private ChatRoomsRepo roomsRepo;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatsObjects> findFirstConversationMessages(
            @RequestParam(required = true) String chatRoomId,
            @RequestParam Optional<Integer> limit,
            @RequestParam Optional<Integer> page) {
        int l = limit.orElse(30);
        int pa = page.orElse(0);
        Pageable pageable = PageRequest.of(pa, l);
        List<Chats> chats = repo.findAllByChatRoomId(chatRoomId, pageable).toList();
        return ResponseEntity.ok().body(new ChatsObjects(chats, pa, l));

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{messageId}")
    public ResponseEntity<Chats> getMessageById(@PathVariable String messageId) {
        return ResponseEntity.ok(repo.findChatsByMessageId(messageId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chats> createChat(@RequestBody Chats chats) {
        Chats c = repo.save(chats);
        return ResponseEntity.ok(c);
    }

    @PutMapping(path = "/dlr/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDLrReport(@PathVariable String messageId) {
        Chats chat = repo.findChatsByMessageId(messageId);
        chat.setDelivered(true);
        repo.save(chat);
        return ResponseEntity.ok("{\"message\":\"DLR delivered\"}");
    }

    @GetMapping(path = "/dlr/{messageId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteDLRReport(@PathVariable String messageId) {
        rabbitTemplate.convertAndSend(ConfigConstants.MESSAGE_EXCHANGE, ConfigConstants.REMOVE_DLR_KEY, messageId);
        return ResponseEntity.ok("done");
    }

    @GetMapping("/lastItem/{partyA}")
    public ResponseEntity<List<LastChatItemObj>> getLastMessageForUserChatRooms(@PathVariable long partyA) {
        List<ChatRoomConnections> connections = roomsRepo.findAllByPartyA(partyA);
        List<LastChatItemObj> o = new ArrayList<>();
        connections.forEach(el -> {
            LastChatItemObj lastChatItemObj = new LastChatItemObj();
            Chats lastItem = repo.findTopByOrderOrderByChatRoomIdDesc(el.getChatRoomId());
            lastChatItemObj.setLastMessage(lastItem);
            lastChatItemObj.setConnection(el);
            o.add(lastChatItemObj);
        });
        return ResponseEntity.ok(o);
    }
}
