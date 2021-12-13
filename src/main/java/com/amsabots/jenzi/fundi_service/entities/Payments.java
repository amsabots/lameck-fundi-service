package com.amsabots.jenzi.fundi_service.entities;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

@Entity
@Table(name = "fundi_payments")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Payments extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private double paid;
    private String source;
    @OneToOne
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private Projects project;
}
