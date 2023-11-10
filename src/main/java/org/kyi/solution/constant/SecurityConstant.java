package org.kyi.solution.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 1*(60*60*24*1000);  //1 day expressed in milliseconds
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Jwt-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String KYI_LLC = "KYI, LLC";
    public static final String KYI_ADMINISTRATION = "M Podcasts";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to login to access this page";
    public static final String ACCESS_DENIED_MESSAGE = "You do not have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    public static final String [] PUBLIC_URLS = {"/user/login", "/user/register", "/user/resetPassword/**", "/user/image/**",
                                                "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html"
                                                };

}
