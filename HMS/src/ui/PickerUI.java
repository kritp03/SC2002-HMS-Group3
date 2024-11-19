package HMS.src.ui;

import HMS.src.authorisation.PasswordManager;
import HMS.src.io.PasswordCsvHelper;
import HMS.src.io.PatientCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
     * Helper class for handling patient-related CSV operations.
     */
    private PatientCsvHelper patientCsvHelper = new PatientCsvHelper();

    /**
     * Helper class for handling staff-related CSV operations.
     */
    private StaffCsvHelper staffCsvHelper = new StaffCsvHelper();

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
     * 
     * @return A set of all user IDs.
     */
    private List<String[]> loadAllUserDetails() {
        PasswordCsvHelper passwordHelper = new PasswordCsvHelper();
        StaffCsvHelper staffHelper = new StaffCsvHelper();
        PatientCsvHelper patientHelper = new PatientCsvHelper();
    
        List<String[]> passwordData = passwordHelper.readCSV();
        List<String[]> staffData = staffHelper.readCSV();
        List<String[]> patientData = patientHelper.readCSV();
    
        Map<String, String[]> userMap = new HashMap<>();
    
        // Start from index 1 to skip the header
        for (int i = 1; i < staffData.size(); i++) {
            String[] user = staffData.get(i);
            if (user.length > 5) {
                userMap.put(user[0], new String[] { user[0], user[2], "" }); // user[2] is the Role
            }
        }
        for (int i = 1; i < patientData.size(); i++) {
            String[] user = patientData.get(i);
            if (user.length > 1) {
                userMap.put(user[0], new String[] { user[0], "Patient", "" });
            }
        }
    
        for (int i = 1; i < passwordData.size(); i++) {
            String[] entry = passwordData.get(i);
            if (entry.length > 1 && userMap.containsKey(entry[0])) {
                userMap.get(entry[0])[2] = entry[1]; // password
            }
        }
    
        List<String[]> userDetails = new ArrayList<>(userMap.values());
        
        return userDetails;
    }

    /**
     * Prompts the user to select their domain (role) and returns the choice.
     * 
     * @return An integer representing the user's role choice (1-4).
     */
    private int getUserDomain() {
        System.out.println("\nRoles");
        System.out.println("=====");
        System.out.println("1. Doctor\n2. Patient\n3. Pharmacist\n4. Admin");
        return validateIntRange("Please select your role (1-4): ", 1, 4);
    }

    /**
     * Prompts the user to enter their ID.
     * 
     * @return A string representing the user's ID in uppercase.
     */
    private String getUserID() {
        System.out.print("Please enter your ID: ");
        return scanner.nextLine().toUpperCase();
    }

    /**
     * Prompts the user to enter their password and hashes it.
     * 
     * @return A hashed password string.
     */
    private String getUserPassword() {
        return passwordManager.hashPassword(passwordManager.getPassword("Please enter your password: "));
    }

    /**
     * Authenticates the user by verifying their ID and hashed password against
     * stored credentials.
     * 
     * @param id       The user's ID.
     * @param password The hashed password entered by the user.
     * @return true if authentication is successful; false otherwise.
     */

    private boolean authenticateUser(String id, String password, String expectedRole) {
        List<String[]> allUsers = loadAllUserDetails();
        for (String[] user : allUsers) {
            if (user[0].equalsIgnoreCase(id) && passwordManager.authenticate(id, user[2])) {
                return user[1].equalsIgnoreCase(expectedRole);
            }
        }
        return false; // Return false if no matching ID is found or role does not match.
    }

    /**
     * Displays login options to the user, handles role selection, and initiates
     * authentication.
     * If authentication succeeds, redirects the user to their role-based interface.
     */
    public void displayLoginOptions() {
        int domain = getUserDomain();
        String id = getUserID();
        String password = getUserPassword();
        String expectedRole = getRoleFromDomain(domain);
    
        if (authenticateUser(id, password, expectedRole)) {
            handleLogin(domain, id);
        } else {
            System.out.println("Invalid credentials or role mismatch, please try again.");
            displayLoginOptions();
        }
    }

    /**
     * Returns the role corresponding to the domain selected by the user.
     * 
     * @param domain The domain selected by the user.
     * @return A string representing the user's role.
     */
    private String getRoleFromDomain(int domain) {
        return switch (domain) {
            case 1 -> "Doctor";
            case 2 -> "Patient";
            case 3 -> "Pharmacist";
            case 4 -> "Administrator";
            default -> null;
        };
    }

    /**
     * Redirects the user to their role-based interface after successful login.
     * 
     * @param choice The role chosen by the user.
     * @param id     The user's ID.
     */
    private void handleLogin(int choice, String id) {
        String role = getRoleFromDomain(choice);
        if (role == null) {
            System.out.println("Invalid option. Please try again.");
            displayLoginOptions();
            return;
        }
    
        System.out.println("\nLogging in as " + role + "...");
        SessionManager.loginUser(role, id);
    
        try {
            switch (choice) {
                case 1 -> DoctorUI.displayOptions();
                case 2 -> PatientUI.displayOptions();
                case 3 -> PharmacistUI.displayOptions();
                case 4 -> AdminUI.displayOptions();
                default -> throw new IllegalStateException("Unexpected role: " + role);
            }
        } catch (Exception e) {
            System.out.println("Error accessing role-specific interface. Please contact support.");
            e.printStackTrace();
        }
    }

    /**
     * Entry point for the PickerUI.
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        PickerUI pickerUI = new PickerUI();
        pickerUI.displayLoginOptions();
    }
}
