package HMS.src.ui;

import HMS.src.authorisation.PasswordManager;
import HMS.src.io.PasswordCsvHelper;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The PickerUI class serves as the entry point for user authentication and 
 * role-based navigation. It manages user login credentials, verifies roles, 
 * and redirects users to their respective interfaces based on their role.
 */
public class PickerUI {
    /**
     * Scanner for user input.
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Helper class for handling password-related CSV operations.
     */
    private PasswordCsvHelper passwordCsvHelper = new PasswordCsvHelper();

    /**
     * Manager for password hashing and authentication.
     */
    private PasswordManager passwordManager = new PasswordManager();

    /**
     * Set to store all valid user IDs for authentication.
     */
    private Set<String> allIDs = new HashSet<>();

    /**
     * Loads all user IDs from the password CSV into a set for quick lookup.
     * @return A set of all user IDs.
     */
    private Set<String> loadAllIDs() {
        List<String[]> userData = passwordCsvHelper.readCSV();
        for (String[] entry : userData) {
            if (entry.length > 0) {
                allIDs.add(entry[0].toUpperCase());
            }
        }
        return allIDs;
    }

    /**
     * Prompts the user to select their domain (role) and returns the choice.
     * @return An integer representing the user's role choice (1-4).
     */
    private int getUserDomain() {
        System.out.println("Roles");
        System.out.println("=====");
        System.out.println("1. Doctor\n2. Patient\n3. Pharmacist\n4. Admin");
        return validateIntRange("Please select your role (1-4): ", 1, 4);
    }

    /**
     * Prompts the user to enter their ID.
     * @return A string representing the user's ID in uppercase.
     */
    private String getUserID() {
        System.out.print("Please enter your ID: ");
        return scanner.nextLine().toUpperCase();
    }

    /**
     * Prompts the user to enter their password and hashes it.
     * @return A hashed password string.
     */
    private String getUserPassword() {
        return passwordManager.hashPassword(passwordManager.getPassword("Please enter your password: "));
    }

    /**
     * Authenticates the user by verifying their ID and hashed password against stored credentials.
     * @param id The user's ID.
     * @param password The hashed password entered by the user.
     * @return true if authentication is successful; false otherwise.
     */
    private boolean authenticateUser(String id, String password) {
        allIDs = loadAllIDs();
        return allIDs.contains(id) && passwordManager.authenticate(id, password);
    }

    /**
     * Displays login options to the user, handles role selection, and initiates authentication.
     * If authentication succeeds, redirects the user to their role-based interface.
     */
    public void displayLoginOptions() {
        int domain = getUserDomain();
        String id = getUserID();
        String password = getUserPassword();

        if (authenticateUser(id, password)) {
            handleLogin(domain, id);
        } else {
            System.out.println("Invalid credentials, please try again.");
            displayLoginOptions();
        }
    }

    /**
     * Redirects the user to their role-based interface after successful login.
     * @param choice The role chosen by the user.
     * @param id The user's ID.
     */
    private void handleLogin(int choice, String id) {
        String role = "";
        switch (choice) {
            case 1 -> {
                role = "Doctor";
                System.out.println("\nLogging in as Doctor...");
                SessionManager.loginUser(role, id);
                try {
                    DoctorUI.displayOptions();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case 2 -> {
                role = "Patient";
                System.out.println("\nLogging in as Patient...");
                SessionManager.loginUser(role, id);
                try {
                    PatientUI.displayOptions();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            case 3 -> {
                role = "Pharmacist";
                System.out.println("\nLogging in as Pharmacist...");
                SessionManager.loginUser(role, id);
                PharmacistUI.displayOptions();
            }
            case 4 -> {
                role = "Administrator";
                System.out.println("\nLogging in as Admin...");
                SessionManager.loginUser(role, id);
                AdminUI.displayOptions();
            }
            default -> {
                System.out.println("Invalid option. Please try again.");
                displayLoginOptions();
            }
        }
        String userID = SessionManager.getCurrentUserID();
        SessionManager.loginUser(userID, role);
    }
}
