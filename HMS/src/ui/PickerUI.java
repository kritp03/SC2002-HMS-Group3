// package HMS.src.ui;

// import java.util.HashSet;
// import java.util.List;
// import java.util.Scanner;
// import java.util.Set;

// import HMS.src.io_new.PasswordCsvHelper;
// // import HMS.src.io_new.PatientCsvHelper;
// import HMS.src.utils.SessionManager;
// import static HMS.src.utils.ValidationHelper.validateIntRange;

// import HMS.src.authorisation.PasswordManager;

// public class PickerUI {
//     private Scanner scanner = new Scanner(System.in);
//     private PasswordCsvHelper passwordCsvHelper = new PasswordCsvHelper();
//     // private PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
//     private Set<String> allIDs = new HashSet<>();


//     private Set<String> loadAllIDs() {
//         List<String[]> userID = passwordCsvHelper.readCSV();
//         for (String[] entry : userID) {
//             if (entry.length > 0) {
//                 allIDs.add(entry[0].toUpperCase());
//             }
//         }
//         return allIDs;  
//     }

//     private int getUserDomain() {
//         System.out.println("Roles");
//         System.out.println("=====");
//         System.out.println("1. Doctor\n2. Patient\n3. Pharmacist\n4. Admin");
//         int domain = validateIntRange("Please select your domain (1-4): ", 1, 4);
//         return domain;
//     }

//     private String getUserID() {
//         System.out.print("Please enter your ID: ");
//         return scanner.nextLine().toUpperCase();
//     }

//     private String getUserPassword() {
//         // Check if the console is available
//         java.io.Console console = System.console();
//         if (console == null) {
//             System.out.println("No console available");
//             return scanner.nextLine(); // Fallback to scanner if running from an IDE without console
//         } else {
//             char[] passwordArray = console.readPassword("Please enter your password: ");
//             return new String(passwordArray); 
//         }
//     }

//     private boolean authenticateUser(String id, String password) {
//         allIDs = loadAllIDs();
//         return allIDs.contains(id) && DEFAULT_PASSWORD.equals(password);
//     }

//     public void displayLoginOptions() {
//         int domain = getUserDomain();
//         String id = getUserID();
//         String password = getUserPassword();

//         if (authenticateUser(id, password)) {
//             handleLogin(domain, id);
//         } else {
//             System.out.println("Invalid credentials, please try again.");
//             displayLoginOptions();
//         }
//     }

//     private void handleLogin(int choice, String id) {
//         String role = "";
//         switch (choice) {
//             case 1:
//                 role = "Doctor";
//                 System.out.println("\nLogging in as Doctor...");
//                 SessionManager.loginUser(role, id);
//                 DoctorUI.displayOptions();
//                 break;
//             case 2:
//                 role = "Patient";
//                 System.out.println("\nLogging in as Patient...");
//                 SessionManager.loginUser(role, id);
//                 PatientUI.displayOptions();
//                 break;
//             case 3:
//                 role = "Pharmacist";
//                 System.out.println("\nLogging in as Pharmacist...");
//                 SessionManager.loginUser(role, id);
//                 PharmacistUI.displayOptions();
//                 break;
//             case 4:
//                 role = "Admin";
//                 System.out.println("\nLogging in as Admin...");
//                 SessionManager.loginUser(role, getUserID());
//                 AdminUI.displayOptions();
//                 break;
//             default:
//                 System.out.println("Invalid option. Please try again.");
//                 displayLoginOptions();
//                 break;
//         }
//         String userID = SessionManager.getCurrentUserID();
//         SessionManager.loginUser(userID, role);
//         System.out.println("Welcome, " + userID + "!");
        
//     }

    
// }

package HMS.src.ui;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import HMS.src.io_new.PasswordCsvHelper;
import HMS.src.utils.SessionManager;
import HMS.src.authorisation.PasswordManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;

public class PickerUI {
    private Scanner scanner = new Scanner(System.in);
    private PasswordCsvHelper passwordCsvHelper = new PasswordCsvHelper();
    private PasswordManager passwordManager = new PasswordManager();
    private Set<String> allIDs = new HashSet<>();

    private Set<String> loadAllIDs() {
        List<String[]> userData = passwordCsvHelper.readCSV();
        for (String[] entry : userData) {
            if (entry.length > 0) {
                allIDs.add(entry[0].toUpperCase());
            }
        }
        return allIDs;  
    }

    private int getUserDomain() {
        System.out.println("Roles");
        System.out.println("=====");
        System.out.println("1. Doctor\n2. Patient\n3. Pharmacist\n4. Admin");
        int domain = validateIntRange("Please select your domain (1-4): ", 1, 4);
        return domain;
    }

    private String getUserID() {
        System.out.print("Please enter your ID: ");
        return scanner.nextLine().toUpperCase();
    }

    private String getUserPassword() {
        return passwordManager.hashPassword(passwordManager.getPassword("Please enter your password: "));
    }

    private boolean authenticateUser(String id, String password) {
        allIDs = loadAllIDs();
        return allIDs.contains(id) && passwordManager.authenticate(id, password);
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

    // public static void main(String[] args) {
    //     PickerUI ui = new PickerUI();
    //     ui.displayLoginOptions();
    // }
}