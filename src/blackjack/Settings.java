package blackjack;

public final class Settings {
    private static String username;

    public static boolean setUsername(String username) {
        if (username == null || username.isEmpty() || username.isBlank()) {
            return false;
        }
        Settings.username = username;
        return true;
    }

    public static String getUsername() {
        return username;
    }

    private Settings() {

    }
}
