package com.amsabots.jenzi.fundi_service.entities;

import com.amsabots.jenzi.fundi_service.enumUtils.AccountProviders;
import com.amsabots.jenzi.fundi_service.utils.Commons;
import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/**
 * @author andrew mititi on Date 12/2/21
 * @Project lameck-fundi-service
 */
@Entity
@Table(name = "fundi_details")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Account extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    @Column(unique = true)
    private String email;
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private AccountProviders accountProviders;
    private String password;
    private String secondaryEmail;
    private boolean isEnabled = true;
    private boolean isVerified = false;
    private boolean newPassword = false;

    private String userBackgroundColor;
    private String userForeGroundColor;
    private String accountId;
    private boolean isPremium = false;
    private String photoUrl;
    private boolean isEngaged = false;
    private String longitude ;
    private String latitude;
    private boolean isDeleted = false;
    private String NcaNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Projects> projects;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<RatesAndReviews> ratesAndReviews;

    @JsonIgnore
    @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Fundi_Account_Overall_Perfomance accountOverallPerfomance;

    @PrePersist
    public void setUserDefaults() {
        if (null == accountId) {
            setAccountId(UUID.randomUUID().toString().replaceAll("-", ""));
            setAccountProviders(AccountProviders.EMAILPASSWORD);
        }
        setUserBackgroundColor(Commons.createRandomColor());
        setUserForeGroundColor(Commons.createRandomDeepColor());
    }


}
