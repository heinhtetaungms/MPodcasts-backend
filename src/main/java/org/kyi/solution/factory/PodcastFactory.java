package org.kyi.solution.factory;


import org.kyi.solution.dto.PodcastDTO;
import org.kyi.solution.enumeration.PremiumType;
import org.kyi.solution.model.Podcast;
import org.kyi.solution.model.User;
import org.kyi.solution.model.Writer;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.kyi.solution.utility.DateUtils.toLocalDate;


public class PodcastFactory {
    public static List<PodcastDTO> convertListToDTOList(List<Podcast> podcastList) {
        return podcastList.stream()
                .map(podcast -> convertToDTO(podcast))
                .collect(Collectors.toList());
    }

    public static Podcast convertToEntity(PodcastDTO podcastDTO, Writer writer, User user) {
        new Podcast();
        return Podcast
                .builder()
                .id(podcastDTO.getId())
                .title(podcastDTO.getTitle())
                .body(podcastDTO.getBody())
                .fileUrl(podcastDTO.getFileUrl())
                .imageUrl(podcastDTO.getImageUrl())
                .viewCount(podcastDTO.getViewCount())
                .likeCount(podcastDTO.getLikeCount())
                .ago(podcastDTO.getAgo())
                .premiumType(PremiumType.FREE)
                .writer(writer)
                .user(user)
                .createdAt(new Date())
                .updatedAt(podcastDTO.getUpdatedAt())
                .build();
    }
    public static PodcastDTO convertToDTO(Podcast podcast) {
        new PodcastDTO();
        return PodcastDTO
                .builder()
                .id(podcast.getId())
                .title(podcast.getTitle())
                .body(podcast.getBody())
                .fileUrl(podcast.getFileUrl())
                .imageUrl(podcast.getImageUrl())
                .viewCount(podcast.getViewCount())
                .likeCount(podcast.getLikeCount())
                .liked(podcast.getUserInteractions().isEmpty() ? false : podcast.getUserInteractions().stream().findFirst().get().isLiked())
                .ago(podcast.getAgo())
                .premiumType(podcast.getPremiumType())
                .writer(podcast.getWriter())
                .user(podcast.getUser())
                .createdAt(podcast.getCreatedAt())
                .updatedAt(podcast.getUpdatedAt())
                .build();
    }

    public static List<PodcastDTO> concatAgo(List<PodcastDTO> podcasts) {
        podcasts.stream().forEach(podcast -> {
            String daysAgo = formatDaysAgo(toLocalDate(podcast.getCreatedAt()));
            podcast.setAgo(daysAgo);
        });
        return podcasts;
    }
    public static List<Podcast> ago(List<Podcast> podcasts) {
        podcasts.stream().forEach(podcast -> {
            String daysAgo = formatDaysAgo(toLocalDate(podcast.getCreatedAt()));
            podcast.setAgo(daysAgo);
        });
        return podcasts;
    }
    public static String formatDaysAgo(LocalDate created) {
        long daysBetween = ChronoUnit.DAYS.between(created, LocalDate.now());

        if (daysBetween == 0) {
            return "Today";
        } else if (daysBetween <= 6) {
            return daysBetween + " days ago";
        } else if (daysBetween <= 27) {
            long weeksAgo = daysBetween / 7;
            return weeksAgo + " weeks ago";
        } else {
            long monthsAgo = daysBetween / 30; // Approximate calculation for months
            if (monthsAgo <= 12) {
                return monthsAgo + " months ago";
            } else {
                long yearsAgo = monthsAgo / 12;
                return yearsAgo + " year" + (yearsAgo > 1 ? "s" : "") + " ago";
            }
        }
    }
}
