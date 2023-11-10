package org.kyi.solution.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.kyi.solution.configuration.UserPrincipal;
import org.kyi.solution.constant.SecurityConstant;
import org.kyi.solution.enumeration.AuthProvider;
import org.kyi.solution.enumeration.Role;
import org.kyi.solution.exception.domain.EmailNotFoundException;
import org.kyi.solution.model.User;
import org.kyi.solution.oauth2.user.OAuth2UserInfo;
import org.kyi.solution.oauth2.user.OAuth2UserInfoFactory;
import org.kyi.solution.service.UserService;
import org.kyi.solution.utility.JWTTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.kyi.solution.constant.UserImplConstant.EMAIL_NOT_FOUND;
import static org.kyi.solution.constant.UserImplConstant.NO_USER_FOUND_BY_EMAIL;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;

    private final JWTTokenProvider jwtTokenProvider;

    @Value("${frontend.redirect_url}")
    private String redirectUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        processOAuth2User(authentication);

        // Generate JWT token
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String jwtToken = jwtTokenProvider.generateJwtToken(userPrincipal);

        String redirectWithToken = redirectUrl + "?accessToken=" + jwtToken;

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(redirectWithToken);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private void processOAuth2User(Authentication authentication) {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = principal.getAttributes();

        //TODO fix email null case
        String email = attributes.get("email") == null ? null : attributes.get("email").toString();

        try {
            if (email == null) {
                throw new EmailNotFoundException(EMAIL_NOT_FOUND);
            }
            userService.findUserByEmail(email)
                    .ifPresentOrElse(user -> {
                        update(user, oAuth2AuthenticationToken, principal);
                    }, () -> {
                        register(oAuth2AuthenticationToken, principal);
                    });

        } catch (EmailNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(User user, OAuth2AuthenticationToken oAuth2AuthenticationToken, DefaultOAuth2User principal) {
        Map<String, Object> attributes = principal.getAttributes();

        UserPrincipal userPrincipal = UserPrincipal.create(user, attributes);
        Authentication securityAuth = new OAuth2AuthenticationToken(userPrincipal, List.of(new SimpleGrantedAuthority(user.getRole())), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }

    private void register(OAuth2AuthenticationToken oAuth2AuthenticationToken, DefaultOAuth2User principal) {
        Map<String, Object> attributes = principal.getAttributes();
        OAuth2UserInfo oAuth2User =OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId(), principal.getAttributes());

        User user = new User();
        user.setUserId(generateUserId());
        user.setFirstName(oAuth2User.getFirstName());
        user.setLastName(oAuth2User.getLastName());
        user.setUserName(oAuth2User.getName());
        user.setEmail(oAuth2User.getEmail());
        user.setJoinDate(new Date());
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(oAuth2User.getPicture());
        user.setProvider(AuthProvider.valueOfLabel(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId()));
        user.setProviderId(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        userService.save(user);

        UserPrincipal userPrincipal = UserPrincipal.create(user, attributes);
        Authentication securityAuth = new OAuth2AuthenticationToken(userPrincipal, List.of(new SimpleGrantedAuthority(user.getRole())), oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }
}
