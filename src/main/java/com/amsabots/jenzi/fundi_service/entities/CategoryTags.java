package com.amsabots.jenzi.fundi_service.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "fundi_tag_id")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryTags extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long tagId;
    private long accountId;
}
