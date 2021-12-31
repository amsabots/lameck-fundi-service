package com.amsabots.jenzi.fundi_service.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "fundi_rate_reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class RatesAndReviews extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 1000)
    private String review;
    private boolean isApproved = true;
    private float rating;
    private boolean hideSource = false;
    @Column(updatable = false)
    private String source;
    @Column(updatable = false)
    private String reviewId;


    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;


    @ManyToOne
    @JoinColumn(name = "projectId")
    private Projects projects;

    @PrePersist
    public void setReviewDefaults() {
        setReviewId(UUID.randomUUID().toString().replaceAll("-", ""));
    }
}
