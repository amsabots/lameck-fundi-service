package com.amsabots.jenzi.fundi_service.entities;

import com.amsabots.jenzi.fundi_service.enumUtils.ProjectStatus;
import com.amsabots.jenzi.fundi_service.utils.Commons;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "fundi_projects")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Projects extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String projectId;
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
    private boolean isAccepted = true;

    private String taskId;

    private String backgroundIdColor;
    private String foregroundIdColor;

    @ManyToOne
    @JoinColumn
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "projects", targetEntity = RatesAndReviews.class)
    private List<RatesAndReviews> ratesAndReviews;

    @JsonIgnore
    @OneToOne(mappedBy = "project")
    private Payments payments;

    @PrePersist
    public void setCreationDefaults() {
        setProjectStatus(ProjectStatus.ONGOING);
        setBackgroundIdColor(Commons.createRandomColor());
        setForegroundIdColor(Commons.createRandomDeepColor());
        setProjectId(UUID.randomUUID().toString().replaceAll("-", ""));
    }
}
