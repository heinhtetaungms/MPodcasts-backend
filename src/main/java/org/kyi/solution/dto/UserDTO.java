package org.kyi.solution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kyi.solution.model.Podcast;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date joinDate;
    private String role;
    private String [] authorities;
    private boolean isActive;
    private boolean isNotLocked;
    private List<Podcast> podcasts;
}
