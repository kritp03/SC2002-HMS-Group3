package HMS.src.app;

import HMS.src.ui.PickerUI;
import HMS.src.utils.InputScanner;
import HMS.src.authorisation.IDRecoveryHelper;
import HMS.src.authorisation.PasswordManager;
import HMS.src.authorisation.IPasswordManager;
import HMS.src.utils.ValidationHelper;
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
        IDRecoveryHelper idRecovery = new IDRecoveryHelper();
        IPasswordManager passwordManager = new PasswordManager();
        ValidationHelper validationHelper = new ValidationHelper();

        int choice = 0;
        do {
            try {
                System.out.println("==================================");
                System.out.println("|      Welcome to ClinicPal!      |");
                System.out.println("==================================");
                
                // Prompt the user to select an option
                choice = validationHelper.validateIntRange("Please select an option: \n1. Login\n2. Forget ID\n3. Exit\n", 1, 3);
                System.out.println();

                // Process the user's choice
                switch (choice) {
                    case 1:
                        pickerUI.displayLoginOptions();
                        break;
                    case 2:
                        handleForgotID(sc, idRecovery, passwordManager);
                        break;
                    case 3:
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
        } while (choice != 3);

        // Close the scanner before exiting
        sc.close();
    }

    /**
     * Handles the forgot ID functionality.
     * Prompts user for their full name and password to recover their ID.
     * 
     * @param sc The scanner instance for input
     * @param idRecovery The ID recovery helper instance
     * @param passwordManager The password manager instance for secure password input
     */
    private static void handleForgotID(InputScanner sc, IDRecoveryHelper idRecovery, IPasswordManager passwordManager) {
        System.out.println("=== ID Recovery ===");
        
        // Clear any leftover newline characters
        sc.nextLine();
        
        System.out.print("Enter your full name: ");
        String fullName = sc.nextLine().trim();
        
        // Use secure password input
        String password = passwordManager.getPassword("Enter your password: ");

        String recoveredID = idRecovery.recoverID(fullName, password);
        
        if (recoveredID != null) {
            System.out.println("\nID Recovery Successful!");
            System.out.println("Your ID is: " + recoveredID);
        } else {
            System.out.println("\nID Recovery Failed!");
            System.out.println("Invalid name or password. Please try again or contact an administrator.");
        }
        System.out.println();
    }
}
