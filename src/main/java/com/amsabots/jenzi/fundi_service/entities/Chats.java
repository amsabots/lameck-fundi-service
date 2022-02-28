package com.amsabots.jenzi.fundi_service.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */

@Entity
@Table(name = "chats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chats extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String messageId;
    private String message;
    private Date date_sent;
    private boolean delivered = false;
    private boolean sent;
    private String sourceId;
    private String destinationId;
    private String signature;
    private String tag;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "chats")
    @JsonIgnore
    private List<ChatAttachments> chatAttachments;
    private String chatRoomId;

    @PrePersist
    public void setDefaults() {
        setMessageId(UUID.randomUUID().toString().replaceAll("-", ""));
        setSent(true);
        // tags can be either "message" "info" "danger" "success"
        setTag("message");
    }
}
