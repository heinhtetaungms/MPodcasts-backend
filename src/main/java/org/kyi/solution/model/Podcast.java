package org.kyi.solution.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.kyi.solution.enumeration.PremiumType;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PODCAST")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Podcast {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    private String fileUrl;
    private String imageUrl;
    private long viewCount;
    private long likeCount;
    private String ago;
    @Enumerated(EnumType.STRING)
    private PremiumType premiumType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Writer writer;

    @JsonIgnore
    @OneToMany(mappedBy = "podcast")
    private List<UserPodcastInteraction> userInteractions;

    @JsonIgnore
    @OneToMany(mappedBy = "podcast")
    private List<UserPodcastViewHist> userPodcastViewHists;

    @ManyToMany
    @JoinTable(
            name = "PODCAST_TAGS",
            joinColumns = @JoinColumn(name = "podcast_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
