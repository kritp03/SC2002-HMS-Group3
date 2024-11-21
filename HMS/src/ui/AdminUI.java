package HMS.src.ui;

import HMS.src.app.App;
import HMS.src.authorisation.PasswordManager;
import HMS.src.authorisation.IPasswordManager;
import HMS.src.io.StaffCsvHelper;
import HMS.src.medication.DosageForm;
import HMS.src.medication.Medication;
import HMS.src.user.*;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import HMS.src.utils.ValidationHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * The AdminUI class provides an interface for administrators to manage staff,
 * appointments, and medication inventory, and to approve replenishment requests.
 */
public class AdminUI {
    /**
     * Default administrator instance for testing and demo purposes.
     */
    private static Administrator admin = new Administrator("A001", "Sarah Lee", "admin@hospital.com", 40, Gender.FEMALE);

    /**
     * Instance of PasswordManager for resetting administrator passwords.
     */
    private static IPasswordManager passwordManager = new PasswordManager();

    
    /**
     * Helper class for validating user input.
     */
    private static ValidationHelper validationHelper = new ValidationHelper();

    /**
     * Displays the main menu for the administrator.
     */
    public static void displayOptions() throws Exception{
        String adminName = getLoggedInAdminName(); // Retrieve the logged-in admin's name

        System.out.println("=====================================");
        System.out.printf("|           Welcome %s!          |\n", adminName); // Display name
        System.out.println("=====================================");

        boolean quit = false;
        do {
            int adminChoice = validationHelper.validateIntRange(
                """
                Please select an option:
                1. Manage Hospital Staff
                2. View Appointments
                3. Manage Medication Inventory
                4. Approve Replenishment Requests
                5. Reset Password
                6. Logout
                """,
                1, 6);
            InputScanner.getInstance().nextLine();
            System.out.println();

            switch (adminChoice) {
                case 1 -> manageStaff();
                case 2 -> admin.viewAppointments();
                case 3 -> manageMedicationInventory();
                case 4 -> approveReplenishmentRequests();
                case 5 -> passwordManager.changePassword();
                case 6 -> {
                    System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
                    SessionManager.logoutUser();
                    App.main(null); // Redirect to main menu
                    return; // Explicitly exit the method after redirection
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!quit);
            }

    /**
    * Retrieves the logged-in admin's name.
    * @return the admin's name if logged in, or "Admin" if not logged in.
    */
    private static String getLoggedInAdminName() {
        if (!SessionManager.isUserLoggedIn() || !"Administrator".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            return "Admin"; // Default to "Admin" if no one is logged in
        }

        String adminID = SessionManager.getCurrentUserID(); // Retrieve logged-in admin's ID
        List<String[]> staff = new StaffCsvHelper().readCSV(); // Read from Staff_List.csv

        for (String[] staffMember : staff) {
            if (staffMember.length > 1 && staffMember[0].equalsIgnoreCase(adminID)) {
                return staffMember[1]; // Return the admin's name
            }
        }

        return "Admin"; // Fallback if no match is found
    }


    /**
     * Manages staff-related operations such as adding, removing, and viewing staff members.
     */
    private static void manageStaff() {
        boolean back = false;
        do {
            int staffChoice = validationHelper.validateIntRange("\nStaff Management:\n1. Add Staff\n2. Remove Staff\n3. View All Staff\n4. Back to Main Menu\n", 1, 4);
            InputScanner.getInstance().nextLine();

            switch (staffChoice) {
                case 1 -> addStaff();
                case 2 -> removeStaff();
                case 3 -> admin.viewStaff();
                case 4 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!back);
    }

    /**
     * Adds a new staff member to the hospital staff list.
     */
    private static void addStaff() {
        System.out.println("\nAdding a New Staff Member");
        
        String name = validationHelper.validateString("Enter Name: ");
        String email = validationHelper.validateEmail("Enter Email: ");
        int age = validationHelper.validateAge("Enter Age: ");
        InputScanner.getInstance().nextLine();
        
        Gender gender = null;
        while (gender == null) {
            try {
                gender = Gender.valueOf(validationHelper.validateString("Enter Gender (MALE, FEMALE, OTHERS): ").toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid gender. Please try again.");
            }
        }

        Role role = null;
        while (role == null) {
            try {
                String roleInput = validationHelper.validateString("Enter Role (DOCTOR, PHARMACIST): ").toUpperCase();
                if (roleInput.equals("DOCTOR") || roleInput.equals("PHARMACIST")) {
                    role = Role.valueOf(roleInput);
                } else {
                    System.out.println("Invalid role. Please enter either DOCTOR or PHARMACIST.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please enter either DOCTOR or PHARMACIST.");
            }
        }

        User staff;
        if (role == Role.DOCTOR) {
            staff = new Doctor("", name, email, age, gender);
        } else {
            staff = new Pharmacist("", name, email, age, gender);
        }

        admin.addStaff(staff);
    }

    /**
     * Removes a staff member based on their staff ID.
     */
    private static void removeStaff() {
        System.out.println("\nRemoving a Staff Member");
        String staffID = validationHelper.validateString("Enter the ID of the Staff Member to remove: ");
        admin.removeStaff(staffID);
    }

    /**
     * Manages medication inventory, including adding, updating stock, and viewing inventory.
     */
    private static void manageMedicationInventory() {
        boolean back = false;
        do {
            int inventoryChoice = validationHelper.validateIntRange("\nMedication Inventory:\n1. View Inventory\n2. Add Medication\n3. Remove Medication\n4. Update Stock Level\n5. Update Low Stock Alert\n6. Back to Main Menu\n", 1, 6);
            InputScanner.getInstance().nextLine();

            switch (inventoryChoice) {
                case 1 -> admin.viewMedicationInventory();
                case 2 -> addMedication();
                case 3 -> removeMedication();
                case 4 -> updateMedicationStock();
                case 5 -> updateLowStockAlert();
                case 6 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!back);
    }

    /**
     * Adds a new medication to the inventory.
     */
    private static void addMedication() {
        System.out.println("\nAdding a New Medication");

        String rawName = validationHelper.validateString("Enter Medication Name: ");
        
        String[] words = rawName.trim().split("\\s+");
        StringBuilder nameBuilder = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (words[i].length() > 0) {
                nameBuilder.append(words[i].substring(0, 1).toUpperCase())
                         .append(words[i].substring(1).toLowerCase());
                
                if (i < words.length - 1) {
                    nameBuilder.append(" ");
                }
            }
        }
        String name = nameBuilder.toString();
        
        DosageForm dosageForm = null;
        while (dosageForm == null) {
            try {
                dosageForm = DosageForm.valueOf(validationHelper.validateString("Enter Dosage Form (TABLET, CAPSULE, LIQUID, INJECTION, CREAM, INHALER, SUPPOSITORY): ").toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid dosage form. Please try again.");
            }
        }

        int concentration = validationHelper.validateInt("Enter Concentration (mg): ");
        int initialStock = validationHelper.validateInt("Enter Initial Stock: ");
        int lowStockThreshold = validationHelper.validateInt("Enter Low Stock Threshold: ");
        InputScanner.getInstance().nextLine();

        Medication medication = new Medication("", name, dosageForm, concentration, initialStock, lowStockThreshold);
        admin.addMedication(medication);
    }

    /**
     * Removes a medication from the inventory based on its ID.
     */
    private static void removeMedication() {
        System.out.println("\nRemoving a Medication");
        
        String medicineID = validationHelper.validateString("\nEnter ID of Medication to remove: ").toUpperCase();
        admin.removeMedication(medicineID);
    }

    /**
     * Updates the stock level for an existing medication.
     */
    private static void updateMedicationStock() {
        System.out.println("\nUpdating Medication Stock");

        String medicineID = validationHelper.validateString("Enter ID of Medication to Update: ");
        int newStock = validationHelper.validateInt("Enter New Stock Level: ");
        InputScanner.getInstance().nextLine();

        admin.updateMedicationStock(medicineID, newStock);
    }
    
    /**
     * Updates the low stock alert threshold for an existing medication.
     */
    private static void updateLowStockAlert() {
        System.out.println("\nUpdating Low Stock Alert");

        String medicineID = validationHelper.validateString("Enter ID of Medication to Update: ");
        int newThreshold = validationHelper.validateInt("Enter New Low Stock Alert Threshold: ");
        InputScanner.getInstance().nextLine();

        admin.updateLowStockAlert(medicineID, newThreshold);
    }

    /**
     * Approves or rejects pending replenishment requests for medication.
     */
    private static void approveReplenishmentRequests() {
        System.out.println("\nProcessing Replenishment Requests");
        List<String[]> requests = admin.getPendingReplenishmentRequests();
        
        if (requests.isEmpty()) {
            System.out.println("No pending replenishment requests.");
            return;
        }

        System.out.println("\nPending Replenishment Requests:");
        String line = "+------------+------------------+----------+------------------+";
        System.out.println(line);
        System.out.format("| %-10s | %-16s | %-8s | %-16s |%n", 
            "Request ID", "Medicine Name", "Amount", "Date Requested");
        System.out.println(line);

        List<String> validRequestIds = new ArrayList<>();
        for (String[] request : requests) {
            System.out.format("| %-10s | %-16s | %-8s | %-16s |%n",
                request[0], request[1], request[2], request[3]);
            validRequestIds.add(request[0]);
        }
        System.out.println(line);

        boolean validRequest = false;
        String requestID;
        do {
            requestID = validationHelper.validateString("\nEnter Request ID to process: ").toUpperCase();
            if (requestID.trim().isEmpty()) {
                System.out.println("Request ID cannot be empty. Please try again.");
                continue;
            }
            
            if (!validRequestIds.contains(requestID)) {
                System.out.println("Invalid Request ID. Please enter a Request ID from the list above.");
                continue;
            }
            
            validRequest = true;
        } while (!validRequest);

        String choice;
        do {
            choice = validationHelper.validateString("Approve or Reject? (A/R): ").toUpperCase();
            if (!choice.equals("A") && !choice.equals("R")) {
                System.out.println("Invalid choice. Please enter 'A' to approve or 'R' to reject.");
            }
        } while (!choice.equals("A") && !choice.equals("R"));

        boolean approve = choice.equals("A");
        String adminID = SessionManager.getCurrentUserID();
        admin.approveReplenishmentRequest(requestID, approve, adminID);
    }

    /**
     * Entry point for the AdminUI to start the application.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        try {
            displayOptions();
        } catch (Exception e) {
            System.out.println("An error occurred in PatientUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
