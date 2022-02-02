package com.amsabots.jenzi.fundi_service.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Entity
@Table(name="chat_attachments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatAttachments extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String file_url;
    private String type;

    @ManyToOne(targetEntity = Chats.class)
    private Chats chats;
}
