package org.kyi.solution.service.impl;

import lombok.AllArgsConstructor;
import org.kyi.solution.model.Podcast;
import org.kyi.solution.model.User;
import org.kyi.solution.model.UserPodcastInteraction;
import org.kyi.solution.model.UserPodcastViewHist;
import org.kyi.solution.repository.UserPodcastInteractionRepository;
import org.kyi.solution.repository.PodcastRepository;
import org.kyi.solution.repository.UserPodcastViewHistRepository;
import org.kyi.solution.repository.UserRepository;
import org.kyi.solution.service.PodcastService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PodcastServiceImpl implements PodcastService {
    private final PodcastRepository podcastRepository;
    private final UserRepository userRepository;
    private final UserPodcastInteractionRepository userPodcastInteractionRepository;
    private final UserPodcastViewHistRepository userPodcastViewHistRepository;

    @Override
    public Podcast save(Podcast podcast) {
        return podcastRepository.save(podcast);
    }

    @Override
    public List<Podcast> findAll() {
        return podcastRepository.findAll();
    }

    @Override
    public Podcast findById(long id) {
        return podcastRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Failed to find Podcast by id " + id));
    }

    @Override
    public void delete(long id) {
        if (podcastRepository.existsById(id)) {
            podcastRepository.deleteById(id);
        }
    }

    @Override
    public Podcast likePodcast(long podcastId, long userId, boolean liked) {
        Podcast podcast = findById(podcastId);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Failed to find User by id" + userId));
        UserPodcastInteraction isExist = userPodcastInteractionRepository.existsByUserAndPodcast(userId, podcastId);

        if(isExist == null) {
            UserPodcastInteraction interaction = new UserPodcastInteraction();
            interaction.setUser(user);
            interaction.setPodcast(podcast);
            interaction.setLiked(liked);
            userPodcastInteractionRepository.save(interaction);
            podcast.setLikeCount(podcast.getLikeCount() + 1);
        }else{
            isExist.setLiked(liked);
            userPodcastInteractionRepository.save(isExist);

            if (liked) {
                podcast.setLikeCount(podcast.getLikeCount() + 1);
            }else{
                podcast.setLikeCount(podcast.getLikeCount() - 1);
            }
        }
        return podcastRepository.save(podcast);
    }

    @Override
    public List<Podcast> findPodcastsByUserId(Long userId) {
        return podcastRepository.findPodcastsByUserId(userId);
    }

    @Override
    public List<Podcast> findPodcastsOrderByLikeCountDesc() {
        return podcastRepository.findAllByOrderByLikeCountDesc();
    }

    @Override
    public List<Podcast> findPodcastsOrderByViewCountDesc() {
        return podcastRepository.findAllByOrderByViewCountDesc();
    }

    @Override
    public List<Podcast> favouritePodcasts(long userId) {
        return userPodcastInteractionRepository.findLikedPodcastsByUserId(userId);
    }

    @Override
    public List<Podcast> podcastPlayListByUser(long userId) {
        return userPodcastViewHistRepository.findViewPodcastsByUserId(userId);
    }

    @Override
    public Podcast viewPodcast(long podcastId, long userId) {
        Podcast podcast = findById(podcastId);
        podcast.setViewCount(podcast.getViewCount() + 1);
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Failed to find User by id" + userId));
        UserPodcastViewHist isExist = userPodcastViewHistRepository.existsByUserAndPodcast(userId, podcastId);

        if(isExist == null) {
            UserPodcastViewHist viewHist = new UserPodcastViewHist();
            viewHist.setUser(user);
            viewHist.setPodcast(podcast);
            userPodcastViewHistRepository.save(viewHist);
        }
        return podcastRepository.save(podcast);
    }

    @Override
    public List<Podcast> findAllPodcastsOrderedByCreatedAtDesc() {
        return podcastRepository.findAllByOrderByCreatedAtDesc();
    }
}
