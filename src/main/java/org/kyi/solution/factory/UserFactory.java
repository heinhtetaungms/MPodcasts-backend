package org.kyi.solution.factory;

import org.kyi.solution.dto.UserDTO;
import org.kyi.solution.model.Podcast;
import org.kyi.solution.model.User;

import java.util.List;

public class UserFactory {
    public static UserDTO convertToDTO(User user, List<Podcast> podcasts) {
        new UserDTO();
        return UserDTO
                .builder()
                .id(user.getId())
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .lastLoginDate(user.getLastLoginDate())
                .joinDate(user.getJoinDate())
                .role(user.getRole())
                .authorities(user.getAuthorities())
                .isActive(user.isActive())
                .isNotLocked(user.isNotLocked())
                .podcasts(podcasts)
                .build();
    }
}
