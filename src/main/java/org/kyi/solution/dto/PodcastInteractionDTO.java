package org.kyi.solution.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PodcastInteractionDTO {
    private long podcastId;
    private long userId;
    private boolean liked;
}
