package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.validateIntRange;

public class PharmacistActions {

    public static void displayOptions(){
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|         Welcome Pharmacist!        |");    
        System.out.println("=====================================");

        boolean quit = false;

        do{
            int pharmacistChoice = validateIntRange("Please select option: \n1. View Appointment Outcome Record\n2. Update Prescription Status\n3. View Medication Inventory \n4. Submit Replenishment Request \n5. Logout \n", 1, 5);
            System.out.println();

            switch(pharmacistChoice){
                case 1:
                    System.out.println("View Appointment Outcome Record");
                    break;
                case 2:
                    System.out.println("Update Prescription Status");
                    break;
                case 3:
                    System.out.println("View Medication Inventory");
                    break;
                case 4:
                    System.out.println("Submit Replenishment Request");
                    break;
                case 5:
                    System.out.println("Logging out...");
                    quit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while(!quit);
    }
    
}
