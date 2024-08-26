package blackjack;

public final class Settings {
    private static String userName;

    public static boolean setUserName(String userName) {
        if (userName == null || userName.isEmpty() || userName.isBlank()) {
            return false;
        }
        Settings.userName = userName;
        return true;
    }

    public static String getUserName() {
        return userName;
    }

    private Settings() {

    }
}
