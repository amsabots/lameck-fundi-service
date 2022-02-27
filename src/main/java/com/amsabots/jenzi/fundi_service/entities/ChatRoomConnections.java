package com.amsabots.jenzi.fundi_service.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "chat_room_connections")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomConnections extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long partyA;
    private long partyB;
    private String chatRoomId;
    private boolean isDeleted = false;
    
    @PrePersist
    private void setDefaultsOnPersist(){
        setChatRoomId(UUID.randomUUID().toString().replaceAll("-",""));
    }
}
