package org.kyi.solution.constant;

public class Authority {
    public static final String [] USER_AUTHORITIES = { "user:read", "user:like", "user:subscribe" };
    public static final String [] CREATOR_AUTHORITIES = { "creator:read", "creator:create", "creator:subscribe", "creator:delete" };
    public static final String [] ADMIN_AUTHORITIES = { "admin:read", "admin:create", "admin:update", "admin:delete" };

}
