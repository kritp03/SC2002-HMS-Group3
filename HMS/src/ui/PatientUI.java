package HMS.src.ui;

import HMS.src.app.App;
import HMS.src.authorisation.PasswordManager;
import HMS.src.user.PatientManager;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;

/**
 * The PatientUI class provides an interface for patients to manage their appointments,
 * view medical records, and update personal information.
 */
public class PatientUI {

    /**
     * Instance of PatientManager to handle patient-related operations.
     */
    private static final PatientManager patientManager = new PatientManager();

    /**
     * Instance of PasswordManager for resetting patient passwords.
     */
    private static final PasswordManager passwordManager = new PasswordManager();

    /**
     * Displays the main menu for the patient and handles user inputs for various options.
     */
    public static void displayOptions() throws Exception{
    System.out.println("=====================================");
    System.out.println("|                Menu                |");
    System.out.println("|          Welcome Patient!          |");
    System.out.println("=====================================");

    boolean quit = false;
    do {
        int patientChoice = validateIntRange(
                """
                Please select an option:
                1. View Medical Record
                2. Update Personal Information
                3. View Available Appointment Slots
                4. Schedule an Appointment
                5. Reschedule an Appointment
                6. Cancel an Appointment
                7. View Scheduled Appointments
                8. View Past Appointment Outcome Records
                9. Reset Password
                10. Logout
                """,
                1, 10);
        System.out.println();

        switch (patientChoice) {
            case 1 -> viewMedicalRecords();
            case 2 -> updatePersonalInformation();
            case 3 -> viewAvailableSlots();
            case 4 -> scheduleAppointment();
            case 5 -> rescheduleAppointment();
            case 6 -> cancelAppointment();
            case 7 -> viewScheduledAppointments();
            case 8 -> viewPastAppointmentOutcomeRecords();
            case 9 -> passwordManager.changePassword();
            case 10 -> {
                System.out.println("Logging out...\nRedirecting to Main Menu...");
                quit = true; // Exit loop
                SessionManager.logoutUser();
                App.main(null); // Redirect to main menu
                return; // Explicitly exit the method after redirection
            }
            default -> System.out.println("Invalid choice. Please try again.");
        }
    } while (!quit);
}


    /**
     * Retrieves the current patient's ID from the session.
     * @return the patient ID if logged in
     */
    private static String getPatientID() {
        if (!SessionManager.isUserLoggedIn() || !"Patient".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            System.out.println("User not logged in as Patient. Redirecting to login.");
            PickerUI pickerUI = new PickerUI();
            pickerUI.displayLoginOptions(); // Force login
        }

        String patientID = SessionManager.getCurrentUserID();

        if (patientID == null || patientID.isEmpty()) {
            System.out.println("Error: Unable to retrieve Patient ID. Please log in again.");
            PickerUI pickerUI = new PickerUI();
            pickerUI.displayLoginOptions(); // Re-prompt login
        }

        return patientID;
    }

    /**
     * Allows the patient to view their medical records.
     */
    private static void viewMedicalRecords() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot display medical records. Invalid Patient ID.");
            return;
        }

        System.out.println("Viewing Medical Records for Patient" + patientID);

        patientManager.showPatientAndRecords();
    }

    /**
     * Allows the patient to update their personal information.
     */
    private static void updatePersonalInformation() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot update information. Invalid Patient ID.");
            return;
        }

        System.out.println("Updating " + patientID +"'s Personal Information");
        patientManager.updatePatientContactInfo();
    }

    /**
     * Displays available appointment slots for the patient.
     */
    private static void viewAvailableSlots() {
        System.out.println("Displaying Available Slots");
        patientManager.viewAvailableSlots();
    }

    /**
     * Allows the patient to schedule a new appointment.
     */
    private static void scheduleAppointment() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot schedule an appointment. Invalid Patient ID.");
            return;
        }

        System.out.println("Scheduling an appointment:");
        patientManager.scheduleAppointment(patientID);
    }

    /**
     * Allows the patient to view their scheduled appointments.
     */
    private static void viewScheduledAppointments() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot view appointments. Invalid Patient ID.");
            return;
        }

        System.out.println("Viewing Scheduled appointments for " + patientID);
        patientManager.viewScheduledAppointments(patientID);
    }

    /**
     * Allows the patient to cancel one of their scheduled appointments.
     */
    private static void cancelAppointment() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot cancel appointment. Invalid Patient ID.");
            return;
        }
        System.out.println("Choose which appointment to cancel: ");
        patientManager.cancelAppointment();
    }

    /**
     * Allows the patient to reschedule an existing appointment by canceling
     * and rebooking a new one.
     */
    private static void rescheduleAppointment() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot reschedule appointment. Invalid Patient ID.");
            return;
        }

        System.out.println("Choose which appointment to reschedule: ");
        int appointmentToReschedule = patientManager.getAppointmentToReschedule(patientID);

        if (appointmentToReschedule == -1) {
            System.out.println("Error: No appointment selected to reschedule.");
            return;
        }
        boolean rescheduled = patientManager.rescheduleAppointment(patientID, appointmentToReschedule);

        if (rescheduled) {
            System.out.println("Appointment successfully canceled and added back to available slots.");
            patientManager.scheduleAppointment(patientID);
        } else {
            System.out.println("Error: Unable to reschedule appointment. Please try again.");
        }
    }

    /**
     * Allows the patient to view past appointment outcome records.
     */
    private static void viewPastAppointmentOutcomeRecords() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot view appointment outcomes. Invalid Patient ID.");
            return;
        }
        System.out.println("View Appointment Records: ");
        patientManager.viewPastAppointmentOutcomes(patientID);
    }

    /**
     * Entry point for the PatientUI.
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

