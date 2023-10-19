package org.kyi.solution.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.kyi.solution.enumeration.AuthProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date joinDate;
    private String role;
    private String [] authorities;
    private boolean isActive;
    private boolean isNotLocked;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    private Date effectiveDate;
    private Date expireDate;
    @Column(nullable = true)
    private boolean subscriptionActive;

    @ManyToMany
    @JoinTable(
            name = "USER_FOLLOWED_TAG",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> followedTags;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserPodcastInteraction> podcastInteractions;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<UserPodcastViewHist> userPodcastViewHists;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Podcast> podcasts = new ArrayList<>();



}
