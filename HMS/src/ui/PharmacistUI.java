package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.validateIntRange;

import HMS.src.appointment.AppointmentManager;
import HMS.src.medication.MedicationManager;
import HMS.src.prescription.PrescriptionManager;
import HMS.src.user.Gender;
import HMS.src.user.Pharmacist;
import HMS.src.utils.SessionManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.io.ApptCsvHelper;
import HMS.src.io.MedicationCsvHelper;

public class PharmacistUI {

    private static MedicationManager medicationManager = new MedicationManager();
    private static PrescriptionManager prescriptionManager = new PrescriptionManager();
    private static Pharmacist pharmacist = new Pharmacist("P001", "Mark Lee", "ml@hospital", 30, Gender.MALE);
    private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();
    private static ApptCsvHelper apptCsvHelper = new ApptCsvHelper();
    private static PasswordManager passwordManager = new PasswordManager();

    public static void displayOptions() {
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|         Welcome Pharmacist!        |");
        System.out.println("=====================================");

        String medFilePath = medicationCsvHelper.getFilePath();
        String apptFilePath = apptCsvHelper.getFilePath();

        boolean quit = false;
        do {
            int pharmacistChoice = validateIntRange(
                    "Please select option: \n1. View Appointment Outcome Record\n2. Update Prescription Status\n3. View Medication Inventory \n4. Submit Replenishment Request \n5. Reset Password \n6. Logout\n",
                    1, 6);
            System.out.println();

            switch (pharmacistChoice) {
                case 1:
                    pharmacist.viewApptOutcomeRecord();
                    break; // tbd
                case 2:
                    pharmacist.updatePrescriptionStatus();
                    break;
                case 3:
                    pharmacist.viewMedicationInventory();
                    break;
                case 4:
                    medicationManager.submitReplenishmentRequest();
                    break;
                case 5:
                    passwordManager.changePassword();
                    break;
                case 6:
                    System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
                    SessionManager.logoutUser();
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