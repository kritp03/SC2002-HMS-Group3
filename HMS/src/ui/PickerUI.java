package HMS.src.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import HMS.src.io_new.StaffCsvHelper;
import HMS.src.io_new.PatientCsvHelper;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;

public class PickerUI {
    private static final String DEFAULT_PASSWORD = "password";
    private Scanner scanner = new Scanner(System.in);
    private StaffCsvHelper staffCsvHelper = new StaffCsvHelper();
    private PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
    private Set<String> allIDs = new HashSet<>();


    private Set<String> loadAllIDs() {
        // Collect IDs from staff
        List<String[]> staffData = staffCsvHelper.readCSV();
        for (String[] entry : staffData) {
            if (entry.length > 0) {
                allIDs.add(entry[0].toUpperCase());
            }
        }
        // Collect IDs from patients
        List<String[]> patientData = patientCsvHelper.readCSV();
        for (String[] entry : patientData) {
            if (entry.length > 0) {
                allIDs.add(entry[0].toUpperCase());
            }
        }
        return allIDs;  
    }

    private int getUserDomain() {
        System.out.println("1. Doctor\n2. Patient\n3. Pharmacist\n4. Admin");
        int domain = validateIntRange("Please select your domain (1-4): ", 1, 4);
        return domain;
    }

    private String getUserID() {
        System.out.print("Please enter your ID: ");
        return scanner.nextLine().toUpperCase();
    }

    private String getUserPassword() {
        // Check if the console is available
        java.io.Console console = System.console();
        if (console == null) {
            System.out.println("No console available");
            return scanner.nextLine(); // Fallback to scanner if running from an IDE without console
        } else {
            char[] passwordArray = console.readPassword("Please enter your password: ");
            return new String(passwordArray); 
        }
    }

    private boolean authenticateUser(String id, String password) {
        allIDs = loadAllIDs();
        return allIDs.contains(id) && DEFAULT_PASSWORD.equals(password);
    }

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

    private void handleLogin(int choice, String id) {
        String role = "";
        switch (choice) {
            case 1:
                role = "Doctor";
                System.out.println("\nLogging in as Doctor...");
                SessionManager.loginUser(role, id);
                DoctorUI.displayOptions();
                break;
            case 2:
                role = "Patient";
                System.out.println("\nLogging in as Patient...");
                SessionManager.loginUser(role, id);
                PatientUI.displayOptions();
                break;
            case 3:
                role = "Pharmacist";
                System.out.println("\nLogging in as Pharmacist...");
                SessionManager.loginUser(role, id);
                PharmacistUI.displayOptions();
                break;
            case 4:
                role = "Admin";
                System.out.println("\nLogging in as Admin...");
                SessionManager.loginUser(role, getUserID());
                AdminUI.displayOptions();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                displayLoginOptions();
                break;
        }
        String userID = SessionManager.getCurrentUserID();
        SessionManager.loginUser(userID, role);
        System.out.println("Welcome, " + userID + "!");
        
    }

    
}