package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.validateIntRange;
// import HMS.src.medication.MedicationManager;
// import HMS.src.io_new.MedicationCsvHelper;

public class AdminUI {

    // private static MedicationManager medicationManager = new MedicationManager();
    // private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();
    
    public static void displayOptions(){
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|            Welcome Admin!          |");    
        System.out.println("=====================================");

        // String medFilePath =  medicationCsvHelper.getFilePath();

        boolean quit = false;
        do {
            int adminChoice = validateIntRange("Please select option: \n1. View and Manage Hospital Staff\n2. View Appointments details\n3. View and Manage Medication Inventory \n4. Approve Replenishment Request \n5. Logout \n", 1, 5);
            System.out.println();

            switch(adminChoice) {
                case 1:
                    System.out.println("View and Manage Hospital Staff");
                    break; //tbd
                case 2:
                    System.out.println("View Appointments details"); //tbd
                    break;
                case 3:
                    System.out.println("View and Manage Medication Inventory"); //tbd
                    break;
                case 4:
                    System.out.println("Approve Replenishment Request"); //tbd
                    break;
                case 5:
                    System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (!quit);
    }

    public static void main(String[] args) {
        displayOptions();
    }
}