package HMS.src.app;

import java.util.InputMismatchException;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import HMS.src.ui.PharmacistActions;
import HMS.src.utils.InputScanner;

// import ui.PharmacistActions;
// import utils.InputScanner;

public class App
{
    public static void main(String[] args) throws Exception {
        InputScanner sc = InputScanner.getInstance();

        int choice = 0;
        do{
            try{
                System.out.println("==================================");
                System.out.println("|      Welcome to ClinicPal!      |");
                System.out.println("==================================");
                choice = validateIntRange("Please select an option: \n1. Login as Doctor\n2. Login as Patient\n3. Login as Pharmacist\n4. Login as Admin\n5. Exit\n", 1, 5);
                System.out.println();

                switch (choice) {
                    case 1:
                        System.out.println("Logging in as Doctor...");
                        break;
                    case 2:
                        System.out.println("Logging in as Patient...");
                        break;
                    case 3:
                        PharmacistActions.displayOptions();
                        break;
                    case 4:
                        System.out.println("Logging in as Admin...");
                        break;
                    case 5:
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
        } while(choice != 5);

        sc.close();
    }
}
