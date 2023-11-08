package org.kyi.solution.enumeration;

public enum AuthProvider {
    LOCAL("local"),
    GOOGLE("google"),
    FACEBOOK("facebook"),
    GITHUB("github");

    private String label;
    private AuthProvider(String label) {
        this.label = label;
    }
    public static AuthProvider valueOfLabel(String label) {
        for (AuthProvider e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return AuthProvider.LOCAL;
    }
}
