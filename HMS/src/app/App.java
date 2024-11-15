package HMS.src.app;

import HMS.src.ui.PickerUI;
import HMS.src.utils.InputScanner;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.util.InputMismatchException;

public class App
{
    public static void main(String[] args) throws Exception {
        InputScanner sc = InputScanner.getInstance();
        PickerUI pickerUI = new PickerUI();

        int choice = 0;
        do{
            try{
                System.out.println("==================================");
                System.out.println("|      Welcome to ClinicPal!      |");
                System.out.println("==================================");
                choice = validateIntRange("Please select an option: \n1. Login\n2. Forget UserID\n3. Exit\n", 1, 3);
                System.out.println();

                switch (choice) {
                    case 1:
                        pickerUI.displayLoginOptions();
                        break;
                    case 2:
                        // PatientUI.displayOptions();
                        break;
                    case 3:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e){
                System.out.println("Only integers are accepted! Please try again.");
                sc.nextLine();
            }
        } while(choice != 3);

        sc.close();
    }
}