package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.validateIntRange;
// import HMS.src.io_new.MedicationCsvHelper;

import HMS.src.utils.SessionManager;
import HMS.src.authorisation.PasswordManager;

public class DoctorUI {
    private static PasswordManager passwordManager = new PasswordManager();
    // private static MedicationCsvHelper medicationCsvHelper = new
    // MedicationCsvHelper();

    public static void displayOptions() {
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|           Welcome Doctor!          |");
        System.out.println("=====================================");

        // String medFilePath = medicationCsvHelper.getFilePath();

        boolean quit = false;
        do {
            int doctorChoice = validateIntRange(
                    "Please select option: \n1. View Patient Medical Record\n2. Update Patient Medical Records\n3. View Personal Schedule \n4. Set Availability for Appointments \n5. Accept or Decline Appointment Requests \n6. View Upcoming Appointments \n7. Record AppointmentOutcome \n8. Reset Password \n9. Logout\n",
                    1, 9);
            System.out.println();

            switch (doctorChoice) {
                case 1:
                    System.out.println("View Patient Medical Records");
                    break; // tbd
                case 2:
                    System.out.println("Update Patient Medical Records"); // tbd
                    break;
                case 3:
                    System.out.println("View Personal Schedule"); // tbd
                    break;
                case 4:
                    System.out.println("Set Availability for Appointments"); // tbd
                    break;
                case 5:
                    System.out.println("Accept or Decline Appointment Requests"); // tbd
                    break;
                case 6:
                    System.out.println("View Upcoming Appointments"); // tbd
                    break;
                case 7:
                    System.out.println("Record AppointmentOutcome"); // tbd
                    break;
                case 8:
                    passwordManager.changePassword();
                    break;
                case 9:
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