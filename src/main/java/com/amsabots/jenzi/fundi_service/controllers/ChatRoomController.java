package com.amsabots.jenzi.fundi_service.controllers;

import com.amsabots.jenzi.fundi_service.entities.ChatRoomConnections;
import com.amsabots.jenzi.fundi_service.repos.ChatRoomsRepo;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author andrew mititi on Date 2/28/22
 * @Project abc_ex_jnz_fnd
 */
@RestController
@RequestMapping("/chat-room")
@AllArgsConstructor
public class ChatRoomController {
    private ChatRoomsRepo repo;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomConnections> createChatRoom(@RequestBody ChatRoomConnections connections) {
        ChatRoomConnections connection = null;
        connection = repo.getChatRoomConnectionsByPartyAAndPartyB(connections.getPartyA(), connections.getPartyB());
        if (null == connection) connection = repo.save(connections);
        ChatRoomConnections partyB = repo.getChatRoomConnectionsByPartyAAndPartyB(connections.getPartyB(), connections.getPartyA());
        if (null == partyB) {
            ChatRoomConnections c = new ChatRoomConnections();
            c.setPartyA(connections.getPartyB());
            c.setPartyB(connection.getPartyA());
            c.setChatRoomId(connection.getChatRoomId());
            connection = repo.save(c);
        }
        return ResponseEntity.ok(connection);
    }

    @GetMapping(path = "/{partyA}/{partyB}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatRoomConnections> getUserChatRoom(@PathVariable long partyA, @PathVariable long partyB) {
        ChatRoomConnections chr = repo.getChatRoomConnectionsByPartyAAndPartyB(partyA, partyB);
        if (null != chr) if (chr.isDeleted()) chr = null;
        return ResponseEntity.ok(chr);
    }

    @PutMapping(path = "/{entryId}")
    public ResponseEntity<String> deleteChatRoomBySettingDeleteValue(@PathVariable long entryId) {
        ChatRoomConnections mcon = repo.findById(entryId).orElse(null);
        if (null != mcon) {
            mcon.setDeleted(true);
            repo.save(mcon);
        }
        return ResponseEntity.ok("Done deleting");

    }

    @GetMapping(path = "/{partyA}")
    public ResponseEntity<List<ChatRoomConnections>> getRoomByPartyA(@PathVariable long partyA) {
        List<ChatRoomConnections> connections = repo.findAllByPartyA(partyA);
        return ResponseEntity.ok(connections);
    }


}
