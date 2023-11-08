package org.kyi.solution.oauth2.user;

import java.util.LinkedHashMap;
import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.getOrDefault("id", "").toString();
    }

    @Override
    public String getFirstName() {
        return attributes.getOrDefault("first_name", "").toString();
    }

    @Override
    public String getLastName() {
        return attributes.getOrDefault("last_name", "").toString();
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
        return getUrl();
    }

    private String getUrl() {
        LinkedHashMap<String, Object> pictureMap = (LinkedHashMap<String, Object>) attributes.get("picture");

        if (pictureMap != null) {
            // Get the nested "data" map from the "picture" map
            LinkedHashMap<String, Object> dataMap = (LinkedHashMap<String, Object>) pictureMap.get("data");

            // Check if the "data" attribute exists
            if (dataMap != null) {
                // Get the "url" value
                String pictureUrl = (String) dataMap.get("url");
                if (pictureUrl != null) {
                    return pictureUrl;
                }
            }
        }
        return "";
    }

}
