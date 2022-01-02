package com.amsabots.jenzi.fundi_service.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Fundi_Account_Overall_Perfomance extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private float stars = 0;
    private long client_rated = 0;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "accountId", foreignKey = @ForeignKey(name = "FK_ACCOUNT_ACCOUNT_PERFOMANCE"))
    private Account account;
    private int completedProjects = 0;
    private int ongoingProjects = 0;
    private int disputesRaised = 0;
    private long reviewsGiven = 0;
    private float responseTime = 1;

}
