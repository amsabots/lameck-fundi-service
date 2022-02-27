package com.amsabots.jenzi.fundi_service.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Entity
@Table(name="chat_rooms")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRooms extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    private long number_of_messages;
    @OneToMany(mappedBy = "chatRooms", cascade = CascadeType.REMOVE)
    private Set<Chats> chats;
    private String chatRoomId;

    @PrePersist
    private void setDefaultsOnPersist(){
        setChatRoomId(UUID.randomUUID().toString().replaceAll("-",""));
    }


}
