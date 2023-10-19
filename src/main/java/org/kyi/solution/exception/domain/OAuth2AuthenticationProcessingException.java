package org.kyi.solution.exception.domain;
import org.springframework.security.core.AuthenticationException;

public class OAuth2AuthenticationProcessingException extends AuthenticationException{

    public OAuth2AuthenticationProcessingException(String msg) {
        super(msg);
    }
}
