package HMS.src.ui;

import HMS.src.app.App;
import HMS.src.authorisation.PasswordManager;
import HMS.src.io.ApptCsvHelper;
import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.medication.MedicationManager;
import HMS.src.prescription.PrescriptionManager;
import HMS.src.user.Gender;
import HMS.src.user.Pharmacist;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.util.List;

/**
 * The PharmacistUI class provides the user interface for pharmacists,
 * allowing them to perform actions such as viewing appointment outcomes,
 * updating prescriptions, managing medication inventory, and resetting passwords.
 */
public class PharmacistUI {

    /**
     * Manages the inventory of medications.
     */
    private static MedicationManager medicationManager = new MedicationManager();

    /**
     * Manages the prescriptions and their statuses.
     */
    private static PrescriptionManager prescriptionManager = new PrescriptionManager();

    /**
     * Represents the pharmacist user.
     */
    private static Pharmacist pharmacist = new Pharmacist("P001", "Mark Lee", "ml@hospital", 30, Gender.MALE);

    /**
     * Helper for accessing medication CSV data.
     */
    private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();

    /**
     * Helper for accessing appointment CSV data.
     */
    private static ApptCsvHelper apptCsvHelper = new ApptCsvHelper();

    /**
     * Manages password changes for the pharmacist.
     */
    private static PasswordManager passwordManager = new PasswordManager();

    /**
     * Displays the main menu for the pharmacist and processes user input.
     * Options include viewing appointment outcomes, updating prescription statuses,
     * managing medication inventory, and submitting replenishment requests.
     */
    public static void displayOptions() throws Exception{
        String pharmacistName = getLoggedInPharmacistName(); // Retrieve the logged-in pharmacist's name

        System.out.println("=====================================");
        System.out.printf("|         Welcome %s!         |\n", pharmacistName); // Display name
        System.out.println("=====================================");

        boolean quit = false;
        do {
            int pharmacistChoice = validateIntRange(
                """
                Please select option:
                1. View Appointment Outcome Record
                2. Update Prescription Status
                3. View Medication Inventory
                4. Submit Replenishment Request
                5. Reset Password
                6. Logout
                """,
                1, 6);
            System.out.println();

            switch (pharmacistChoice) {
                case 1 -> pharmacist.viewApptOutcomeRecord(); // View appointment outcomes
                case 2 -> pharmacist.updatePrescriptionStatus(); // Update prescription statuses
                case 3 -> pharmacist.viewMedicationInventory(); // View the medication inventory
                case 4 -> medicationManager.submitReplenishmentRequest(); // Submit a replenishment request
                case 5 -> passwordManager.changePassword(); // Reset password
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
    * Retrieves the logged-in pharmacist's name.
    * @return the pharmacist's name if logged in, or "Pharmacist" if not logged in.
    */
    private static String getLoggedInPharmacistName() {
        if (!SessionManager.isUserLoggedIn() || !"Pharmacist".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            return "Pharmacist"; // Default to "Pharmacist" if no one is logged in
        }

        String pharmacistID = SessionManager.getCurrentUserID(); // Retrieve logged-in pharmacist's ID
        List<String[]> staff = new StaffCsvHelper().readCSV(); // Read from Staff_List.csv

        for (String[] staffMember : staff) {
            if (staffMember.length > 1 && staffMember[0].equalsIgnoreCase(pharmacistID)) {
                return staffMember[1]; // Return the pharmacist's name
            }
        }

        return "Pharmacist"; // Fallback if no match is found
    }


    /**
     * Entry point for the PharmacistUI.
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
