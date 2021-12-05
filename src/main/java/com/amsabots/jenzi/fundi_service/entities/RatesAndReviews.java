package com.amsabots.jenzi.fundi_service.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "fund_rate_reviews")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RatesAndReviews extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 1000)
    private String review;
    private boolean isApproved = true;
    private float rating;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "accountId", referencedColumnName = "id")
    private Account account;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "projectId", referencedColumnName = "id")
    private Projects projects;
}
