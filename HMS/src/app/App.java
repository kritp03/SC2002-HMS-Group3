package HMS.src.app;

import java.util.InputMismatchException;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import HMS.src.ui.PickerUI;
import HMS.src.utils.InputScanner;

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
                choice = validateIntRange("Please select an option: \n1. Login\n2. Exit\n", 1, 2);
                System.out.println();

                switch (choice) {
                    case 1:
                        pickerUI.displayLoginOptions();
                        break;
                    case 2:
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
        } while(choice != 2);

        sc.close();
    }
}