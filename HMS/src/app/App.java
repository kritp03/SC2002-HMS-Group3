package HMS.src.app;

import HMS.src.ui.PickerUI;
import HMS.src.utils.InputScanner;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.util.InputMismatchException;

/**
 * The main entry point for the ClinicPal application.
 * Provides options to log in or exit the system.
 */
public class App {

    /**
     * The main method to launch the application.
     * Displays a welcome menu for the user to select login or exit options.
     * 
     * @param args Command-line arguments (not used).
     * @throws Exception If any unexpected errors occur during execution.
     */
    public static void main(String[] args) throws Exception {
        InputScanner sc = InputScanner.getInstance();
        PickerUI pickerUI = new PickerUI();

        int choice = 0;
        do {
            try {
                System.out.println("==================================");
                System.out.println("|      Welcome to ClinicPal!      |");
                System.out.println("==================================");
                
                // Prompt the user to select an option
                choice = validateIntRange("Please select an option: \n1. Login\n2. Exit\n", 1, 2);
                System.out.println();

                // Process the user's choice
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
            } catch (InputMismatchException e) {
                System.out.println("Only integers are accepted! Please try again.");
                sc.nextLine(); // Clear invalid input
            }
        } while (choice != 2);

        // Close the scanner before exiting
        sc.close();
    }
}
