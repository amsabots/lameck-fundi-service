package com.amsabots.jenzi.fundi_service.entities;

import lombok.*;

import javax.persistence.*;

/**
 * @author andrew mititi on Date 2/2/22
 * @Project lameck-fundi-service
 */
@Entity
@Table(name="connected_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConnectedUsers extends AbstractEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private long id;
    private String sourceId;
    private String destinationId;
    private long number_of_messages;


}
