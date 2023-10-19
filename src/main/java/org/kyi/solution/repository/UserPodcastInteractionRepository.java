package org.kyi.solution.repository;

import org.kyi.solution.model.Podcast;
import org.kyi.solution.model.UserPodcastInteraction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPodcastInteractionRepository extends JpaRepository<UserPodcastInteraction, Long> {

    @Query(value = "SELECT * FROM user_podcast_interaction WHERE user_id = :userId AND podcast_id = :podcastId",
            nativeQuery = true)
    UserPodcastInteraction existsByUserAndPodcast(@Param("userId") long userId,
                                                  @Param("podcastId") long podcastId);

    @Query("SELECT up.podcast " +
            "FROM UserPodcastInteraction up " +
            "WHERE up.user.id = :userId AND up.liked = true")
    List<Podcast> findLikedPodcastsByUserId(@Param("userId") Long userId);
}

