package org.kyi.solution.oauth2.user;

import java.util.Arrays;
import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {
    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.getOrDefault("id", "").toString();
    }

    @Override
    public String getFirstName() {
        String name = attributes.getOrDefault("name", "").toString();
        if (name == ""){
            return name;
        }
        String[] nameParts = name.split(" ");
        String firstName = nameParts[0];
        return firstName;
    }

    @Override
    public String getLastName() {
        String name = attributes.getOrDefault("name", "").toString();
        if (name == ""){
            return name;
        }
        String[] nameParts = name.split(" ");
        String lastName = nameParts.length > 1 ? String.join(" ", Arrays.copyOfRange(nameParts, 1, nameParts.length)) : "";
        return lastName;
    }

    @Override
    public String getName() {
        return attributes.getOrDefault("name", "").toString();
    }

    @Override
    public String getEmail() {
        return attributes.getOrDefault("email", "").toString();
    }

    @Override
    public String getPicture() {
        return attributes.getOrDefault("avatar_url", "").toString();
    }

}
