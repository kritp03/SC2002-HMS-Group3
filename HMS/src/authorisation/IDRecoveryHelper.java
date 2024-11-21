package HMS.src.authorisation;

import HMS.src.io.StaffCsvHelper;
import HMS.src.io.PasswordCsvHelper;

/**
 * Helper class for handling user ID recovery functionality.
 */
public class IDRecoveryHelper {
    private StaffCsvHelper staffHelper;
    private IPasswordManager passwordManager;
    private PasswordCsvHelper passwordHelper;

    public IDRecoveryHelper() {
        this.staffHelper = new StaffCsvHelper();
        this.passwordManager = new PasswordManager();
        this.passwordHelper = new PasswordCsvHelper();
    }

    /**
     * Attempts to recover a user's ID using their full name and password.
     * 
     * @param fullName The full name of the user (case insensitive)
     * @param password The password associated with the account
     * @return The user's ID if found and password matches, null otherwise
     */
    public String recoverID(String fullName, String password) {
        // Find staff by name
        String[] staffEntry = staffHelper.findStaffByName(fullName);
        if (staffEntry == null || staffEntry.length == 0) {
            return null;
        }

        String staffID = staffEntry[0];
        
        // Get password entry for the staff ID
        String[] passwordEntry = passwordHelper.getCredsById(staffID);
        if (passwordEntry == null || passwordEntry.length < 2) {
            return null;
        }

        // Verify password
        String hashedPassword = passwordManager.hashPassword(password);
        if (hashedPassword.equals(passwordEntry[1])) {
            return staffID;
        }

        return null;
    }
}
