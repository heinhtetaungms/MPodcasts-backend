package org.kyi.solution.oauth2;

import org.kyi.solution.configuration.UserPrincipal;
import org.kyi.solution.enumeration.AuthProvider;
import org.kyi.solution.enumeration.Role;
import org.kyi.solution.exception.domain.OAuth2AuthenticationProcessingException;
import org.kyi.solution.model.User;
import org.kyi.solution.oauth2.user.OAuth2UserInfo;
import org.kyi.solution.oauth2.user.OAuth2UserInfoFactory;
import org.kyi.solution.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if( oAuth2UserInfo == null && !StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = Optional.ofNullable(userRepository.findUserByEmail(oAuth2UserInfo.getEmail()));
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setUserId(generateUserId());
        user.setFirstName((String)oAuth2UserInfo.getAttributes().get("given_name"));
        user.setLastName((String)oAuth2UserInfo.getAttributes().get("family_name"));
        user.setUserName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setJoinDate(new Date());
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(oAuth2UserInfo.getImageUrl());
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setFirstName((String)oAuth2UserInfo.getAttributes().get("given_name"));
        existingUser.setLastName((String)oAuth2UserInfo.getAttributes().get("family_name"));
        existingUser.setUserName(oAuth2UserInfo.getName());
        existingUser.setEmail(oAuth2UserInfo.getEmail());
        existingUser.setProfileImageUrl(oAuth2UserInfo.getImageUrl());

        return userRepository.save(existingUser);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

}
