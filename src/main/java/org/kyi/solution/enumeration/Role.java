package org.kyi.solution.enumeration;


import org.kyi.solution.constant.Authority;

public enum Role {
    ROLE_USER(Authority.USER_AUTHORITIES),
    ROLE_CREATOR(Authority.CREATOR_AUTHORITIES),
    ROLE_ADMIN(Authority.ADMIN_AUTHORITIES);

    private String[] authorities;

    Role(String ... authorities) {
        this.authorities = authorities;
    }

    public String [] getAuthorities() {
        return authorities;
    }

}
