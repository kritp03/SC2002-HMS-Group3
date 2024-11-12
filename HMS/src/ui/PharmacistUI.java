package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.validateIntRange;
import HMS.src.medication.MedicationManager;
import HMS.src.io_new.MedicationCsvHelper;

public class PharmacistUI {

    private static MedicationManager medicationManager = new MedicationManager();
    private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();
    
    public static void displayOptions(){
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|         Welcome Pharmacist!        |");    
        System.out.println("=====================================");

        String medFilePath =  medicationCsvHelper.getFilePath();

        boolean quit = false;
        do {
            int pharmacistChoice = validateIntRange("Please select option: \n1. View Appointment Outcome Record\n2. Update Prescription Status\n3. View Medication Inventory \n4. Submit Replenishment Request \n5. Logout \n", 1, 5);
            System.out.println();

            switch(pharmacistChoice) {
                case 1:
                    System.out.println("View Appointment Outcome Record");
                    break; //tbd
                case 2:
                    System.out.println("Update Prescription Status"); //tbd
                    break;
                case 3:
                    medicationManager.viewMedicationInventory(medFilePath);
                    break;
                case 4:
                    medicationManager.submitReplenishmentRequest(medFilePath);
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