package org.kyi.solution.repository;

import org.kyi.solution.model.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Long> {
    @Query("SELECT p FROM Podcast p WHERE p.user.id = :userId")
    List<Podcast> findPodcastsByUserId(@Param("userId") long userId);

    List<Podcast> findAllByOrderByLikeCountDesc();

    List<Podcast> findAllByOrderByViewCountDesc();

    List<Podcast> findAllByOrderByCreatedAtDesc();

}
