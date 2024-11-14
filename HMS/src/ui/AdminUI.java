// package HMS.src.ui;

// import static HMS.src.utils.ValidationHelper.validateIntRange;
// // import HMS.src.medication.MedicationManager;
// // import HMS.src.io_new.MedicationCsvHelper;

// public class AdminUI {

//     // private static MedicationManager medicationManager = new MedicationManager();
//     // private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();
    
//     public static void displayOptions(){
//         System.out.println("=====================================");
//         System.out.println("|                Menu                |");
//         System.out.println("|            Welcome Admin!          |");    
//         System.out.println("=====================================");

//         // String medFilePath =  medicationCsvHelper.getFilePath();

//         boolean quit = false;
//         do {
//             int adminChoice = validateIntRange("Please select option: \n1. View and Manage Hospital Staff\n2. View Appointments details\n3. View and Manage Medication Inventory \n4. Approve Replenishment Request \n5. Logout \n", 1, 5);
//             System.out.println();

//             switch(adminChoice) {
//                 case 1:
//                     System.out.println("View and Manage Hospital Staff");
//                     break; //tbd
//                 case 2:
//                     System.out.println("View Appointments details"); //tbd
//                     break;
//                 case 3:
//                     System.out.println("View and Manage Medication Inventory"); //tbd
//                     break;
//                 case 4:
//                     System.out.println("Approve Replenishment Request"); //tbd
//                     break;
//                 case 5:
//                     System.out.println("Logging out...\nRedirecting to Main Menu...\n");
//                     quit = true;
//                     break;
//                 default:
//                     System.out.println("Invalid choice. Please try again.");
//             }
//         } while (!quit);
//     }

//     public static void main(String[] args) {
//         displayOptions();
//     }
// }

package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.*;

import HMS.src.medication.Medication;
import HMS.src.database.Database;
// import HMS.src.medication.ReplenishmentRequest;
import HMS.src.management.Administrator;
import HMS.src.management.Doctor;
import HMS.src.management.Gender;
import HMS.src.management.Pharmacist;
import HMS.src.management.Role;
import HMS.src.management.User;

public class AdminUI {
    private static Administrator admin = (Administrator) Database.getCurrentUser();

    public static void displayOptions() {
        System.out.println("=====================================");
        System.out.println("|                Menu               |");
        System.out.println("|           Welcome Admin!          |");
        System.out.println("=====================================");

        boolean quit = false;
        do {
            int adminChoice = validateIntRange("Please select an option: \n1. Manage Hospital Staff\n2. View Appointments\n3. Manage Medication Inventory\n4. Approve Replenishment Requests\n5. Logout\n", 1, 5);
            System.out.println();

            switch (adminChoice) {
                case 1 -> manageStaff();
                case 2 -> admin.viewAppointments();
                case 3 -> manageMedicationInventory();
                case 4 -> approveReplenishmentRequests();
                case 5 -> {
                    System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!quit);
    }

    // Method to manage hospital staff (add, remove, or view)
    private static void manageStaff() {
        boolean back = false;
        do {
            int staffChoice = validateIntRange("\nStaff Management:\n1. Add Staff\n2. Remove Staff\n3. View All Staff\n4. Back to Main Menu\n", 1, 4);

            switch (staffChoice) {
                case 1 -> addStaff();
                case 2 -> removeStaff();
                case 3 -> viewAllStaff();
                case 4 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!back);
    }

    // Method to add a new staff member
    private static void addStaff() {
        System.out.println("\nAdding a New Staff Member");
        
        String staffID = validateString("Enter Staff ID: ");
        String name = validateString("Enter Name: ");
        String email = validateString("Enter Email: ");
        int age = validateInt("Enter Age: ");
        
        Gender gender = null;
        while (gender == null) {
            try {
                gender = Gender.valueOf(validateString("Enter Gender (MALE, FEMALE, etc.): ").toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid gender. Please try again.");
            }
        }

        Role role = null;
        while (role == null) {
            try {
                role = Role.valueOf(validateString("Enter Role (DOCTOR, PHARMACIST, ADMINISTRATOR): ").toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Please try again.");
            }
        }

        User staff;
        if (role == Role.DOCTOR) {
            staff = new Doctor(staffID, name, email, age, gender);
        } else if (role == Role.PHARMACIST) {
            staff = new Pharmacist(staffID, name, email, age, gender);
        } else {
            staff = new Administrator(staffID, name, email, age, gender);
        }

        admin.addStaff(staff);
    }

    // Method to remove a staff member
    private static void removeStaff() {
        System.out.println("\nRemoving a Staff Member");
        
        String staffID = validateString("Enter the ID of the Staff Member to remove: ");
        admin.removeStaff(staffID);
    }

    // Method to view all staff members
    private static void viewAllStaff() {
        System.out.println("\nAll Staff Members:");
        Database.getUserData().values().forEach(System.out::println);
    }

    // Method to manage medication inventory (add, update, or view)
    private static void manageMedicationInventory() {
        boolean back = false;
        do {
            int inventoryChoice = validateIntRange("\nMedication Inventory:\n1. Add Medication\n2. Update Medication Stock\n3. View Inventory\n4. Back to Main Menu\n", 1, 4);

            switch (inventoryChoice) {
                case 1 -> addMedication();
                case 2 -> updateMedicationStock();
                case 3 -> admin.viewMedicationInventory();
                case 4 -> back = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!back);
    }

    // Method to add a new medication to inventory
    private static void addMedication() {
        System.out.println("\nAdding a New Medication");

        String name = validateString("Enter Medication Name: ");
        int initialStock = validateInt("Enter Initial Stock: ");
        int lowStockThreshold = validateInt("Enter Low Stock Threshold: ");
        
        Medication medication = new Medication(name, initialStock, lowStockThreshold);
        admin.addMedication(medication);
    }

    // Method to update stock of an existing medication
    private static void updateMedicationStock() {
        System.out.println("\nUpdating Medication Stock");

        String name = validateString("Enter Medication Name to Update: ");
        int newStock = validateInt("Enter New Stock Level: ");
        
        admin.updateMedicationStock(name, newStock);
    }

    // Method to approve or reject replenishment requests
    private static void approveReplenishmentRequests() {
        System.out.println("\nProcessing Replenishment Requests");

        Database.getReplenishmentRequests().forEach(request -> {
            System.out.println("\nReplenishment Request:");
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Medication ID: " + request.getMedicationID());
            System.out.println("Quantity: " + request.getQuantity());
            System.out.println("Request Date: " + request.getDate());
            System.out.println("Status: " + request.getStatus());

            boolean approve = validateBoolean("Approve this request? (y/n): ");
            admin.approveReplenishmentRequest(request, approve);
        });
    }
}