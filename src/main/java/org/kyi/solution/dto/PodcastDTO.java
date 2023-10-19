package org.kyi.solution.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kyi.solution.enumeration.PremiumType;
import org.kyi.solution.model.User;
import org.kyi.solution.model.Writer;

import java.util.Date;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PodcastDTO {
    private Long id;
    private String title;
    private String body;
    private String fileUrl;
    private String imageUrl;
    private long viewCount;
    private long likeCount;
    private boolean liked; //like status for current podcast by current user
    private String ago;
    @Enumerated(EnumType.STRING)
    private PremiumType premiumType;
    private long userId;
    private long writerId;
    private User user;
    private Writer writer;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
}
