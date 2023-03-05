package ar.com.auth.enums;

public enum Roles {
    ROLE_ADMIN_USER,
    ROLE_NORMAL_USER,
    ROLE_GUESS_USER,
    ROLE_UNKNOWN;

    public static Roles parse(String val) {
        try { return Roles.valueOf(val); }
        catch (Exception e) {/* do nothing */}
        return Roles.ROLE_UNKNOWN;
    }
}
