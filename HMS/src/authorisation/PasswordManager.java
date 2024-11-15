package HMS.src.authorisation;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import HMS.src.io_new.PasswordCsvHelper;

import HMS.src.utils.SessionManager;

public class PasswordManager {
    private Scanner scanner;
    private PasswordCsvHelper passwordCsvHelper = new PasswordCsvHelper();
    String userId = SessionManager.getCurrentUserID();

    public PasswordManager() {
        this.scanner = new Scanner(System.in);
    }

    public void changePassword() {
        if (userId != null) {
            boolean authenticated = false;
            while (!authenticated) {
                String currentPassword = getPassword("Enter your current password: ");
                if (authenticate(userId, hashPassword(currentPassword))) {
                    authenticated = true;
                    String newPassword = getNewPassword();
                    if (newPassword != null) {
                        updatePassword(userId, hashPassword(newPassword));
                    } else {
                        System.out.println("Password update cancelled or failed.");
                    }
                } else {
                    System.out.println("Authentication failed. Incorrect password. Please try again.");
                }
            }
        }
        else{
            System.out.println("No user logged in. Please log in to change password.");
        }

    }

    public boolean authenticate(String userId, String hashedPassword) {
        String[] userData = passwordCsvHelper.getCredsById(userId);
        return userData != null && hashedPassword.equals(userData[1]);
    }

    private String getNewPassword() {
        String newPassword1 = getPassword("Enter your new password: ");
        String newPassword2 = getPassword("Confirm your new password: ");

        while (!newPassword1.equals(newPassword2)) {
            System.out.println("Passwords do not match. Please try again.");
            newPassword1 = getPassword("Enter your new password: ");
            newPassword2 = getPassword("Confirm your new password: ");
        }
        return newPassword1;
    }

    public String getPassword(String prompt) {
        java.io.Console console = System.console();
        if (console == null) {
            System.out.println("No console available, using scanner as fallback.");
            return scanner.nextLine();
        } else {
            char[] passwordArray = console.readPassword(prompt);
            return new String(passwordArray);
        }
    }

    private void updatePassword(String userId, String hashedPassword) {
        String[] userData = passwordCsvHelper.getEntryById(userId);
        if (userData != null) {
            userData[1] = hashedPassword; // Update the password field with hashed password
            passwordCsvHelper.updateEntry(userId, userData); // Update the entry in the CSV
            System.out.println("\nPassword updated successfully.");
        } else {
            System.out.println("User not found.");
        }
    }

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    public static void main(String[] args) {
        PasswordManager manager = new PasswordManager();
        manager.changePassword();
    }
}