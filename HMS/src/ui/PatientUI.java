package HMS.src.ui;

import HMS.src.authorisation.PasswordManager;
import HMS.src.user.PatientManager;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;

public class PatientUI {
    private static final PatientManager patientManager = new PatientManager();
    private static final PasswordManager passwordManager = new PasswordManager();

    public static void displayOptions() {
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
                    System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
                    SessionManager.logoutUser();
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (!quit);
    }

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

    private static void viewMedicalRecords() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot display medical records. Invalid Patient ID.");
            return;
        }

        patientManager.showPatientAndRecords();
    }

    private static void updatePersonalInformation() {
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot update information. Invalid Patient ID.");
            return;
        }
        patientManager.updatePatientContactInfo();
    }

    private static void viewAvailableSlots() 
    {
        patientManager.viewAvailableSlots();
    }

    private static void scheduleAppointment() 
    {
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot schedule an appointment. Invalid Patient ID.");
            return;
        }
        patientManager.scheduleAppointment(patientID);
    }

    private static void viewScheduledAppointments() 
    {
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot view appointments. Invalid Patient ID.");
            return;
        }
        patientManager.viewScheduledAppointments(patientID);
    }

    private static void cancelAppointment() 
    {
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot cancel appointment. Invalid Patient ID.");
            return;
        }
        patientManager.cancelAppointment();
    }

    private static void rescheduleAppointment()
    {
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot reschedule appointment. Invalid Patient ID.");
            return;
        }
        int appointmentToReschedule = patientManager.getAppointmentToReschedule(patientID);
    
        if (appointmentToReschedule == -1) {
            System.out.println("Error: No appointment selected to reschedule.");
            return;
        }
        boolean rescheduled = patientManager.rescheduleAppointment(appointmentToReschedule);
    
        if (rescheduled) {
            System.out.println("Appointment successfully canceled and added back to available slots.");
            patientManager.scheduleAppointment(patientID);
        } else {
            System.out.println("Error: Unable to reschedule appointment. Please try again.");
        }
    }

    private static void viewPastAppointmentOutcomeRecords() {
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot view appointment outcomes. Invalid Patient ID.");
            return;
        }
        patientManager.viewPastAppointmentOutcomes(patientID);
    }
    
    
    
    public static void main(String[] args) {
        try {
            displayOptions();
        } catch (Exception e) {
            System.out.println("An error occurred in PatientUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
