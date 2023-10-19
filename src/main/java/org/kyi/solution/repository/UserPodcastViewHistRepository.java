package org.kyi.solution.repository;

import org.kyi.solution.model.Podcast;
import org.kyi.solution.model.UserPodcastViewHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserPodcastViewHistRepository extends JpaRepository<UserPodcastViewHist, Long> {

    @Query(value = "SELECT * FROM user_podcast_view_hist WHERE user_id = :userId AND podcast_id = :podcastId",
            nativeQuery = true)
    UserPodcastViewHist existsByUserAndPodcast(@Param("userId") long userId,
                                                  @Param("podcastId") long podcastId);

    @Query("SELECT up.podcast " +
            "FROM UserPodcastViewHist up " +
            "WHERE up.user.id = :userId")
    List<Podcast> findViewPodcastsByUserId(@Param("userId") Long userId);

}

