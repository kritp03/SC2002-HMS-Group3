package HMS.src.utils;

/**
 * Manages user sessions by storing the current user's ID and role.
 */
public class SessionManager {
    private static String currentUserID;
    private static String currentUserRole;

    /**
     * Logs in the user and sets the session details.
     * @param userID The user's ID.
     * @param role The user's role.
     */
    public static void loginUser(String role, String userID) {
        currentUserRole = role;
        currentUserID = userID.toUpperCase();
    }

    /**
     * Logs out the current user.
     */
    public static void logoutUser() {
        System.out.println("User " + currentUserID + " logged out.");
        currentUserID = null;
        currentUserRole = null;
    }

    /**
     * Checks if a user is logged in.
     * @return true if a user is logged in, false otherwise.
     */
    public static boolean isUserLoggedIn() {
        return currentUserID != null;
    }

    /**
     * Gets the ID of the currently logged-in user.
     * @return the logged-in user's ID.
     */
    public static String getCurrentUserID() {
        return currentUserID;
    }

    /**
     * Gets the role of the currently logged-in user.
     * @return the logged-in user's role.
     */
    public static String getCurrentUserRole() {
        return currentUserRole;
    }
}