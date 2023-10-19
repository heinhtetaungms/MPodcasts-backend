package org.kyi.solution.service;

import org.kyi.solution.model.Podcast;

import java.util.List;

public interface PodcastService {
    Podcast save(Podcast podcast);
    List<Podcast> findAll();
    Podcast findById(long id);
    void delete(long id);
    Podcast likePodcast(long podcastId, long userId, boolean liked);
    List<Podcast> findPodcastsByUserId(Long userId);
    List<Podcast> findPodcastsOrderByLikeCountDesc();
    List<Podcast> findPodcastsOrderByViewCountDesc();
    List<Podcast> favouritePodcasts(long userId);
    List<Podcast> podcastPlayListByUser(long userId);
    Podcast viewPodcast(long podcastId, long userId);
    List<Podcast> findAllPodcastsOrderedByCreatedAtDesc();

}
