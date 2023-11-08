package org.kyi.solution.oauth2.user;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo{
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.getOrDefault("sub", "").toString();
    }

    @Override
    public String getFirstName() {
        return attributes.getOrDefault("given_name", "").toString();
    }

    @Override
    public String getLastName() {
        return attributes.getOrDefault("family_name", "").toString();
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
        return attributes.getOrDefault("picture", "").toString();
    }


}
