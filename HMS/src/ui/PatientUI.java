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
            System.out.println("Debug: User not logged in as Patient. Redirecting to login.");
            PickerUI pickerUI = new PickerUI();
            pickerUI.displayLoginOptions(); // Force login
        }

        String patientID = SessionManager.getCurrentUserID();
        System.out.println("Debug: Retrieved Patient ID: " + patientID);

        if (patientID == null || patientID.isEmpty()) {
            System.out.println("Error: Unable to retrieve Patient ID. Please log in again.");
            PickerUI pickerUI = new PickerUI();
            pickerUI.displayLoginOptions(); // Re-prompt login
        }

        return patientID;
    }

    private static void viewMedicalRecords() {
        System.out.println("View Medical Records");
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot display medical records. Invalid Patient ID.");
            return;
        }

        System.out.println("Debug: Displaying records for Patient ID: " + patientID);
        patientManager.showPatientAndRecords();
    }

    private static void updatePersonalInformation() {
        System.out.println("Update Personal Information");
        String patientID = getPatientID();

        if (patientID == null) {
            System.out.println("Error: Cannot update information. Invalid Patient ID.");
            return;
        }

        System.out.println("Debug: Updating contact info for Patient ID: " + patientID);
        patientManager.updatePatientContactInfo();
    }

    private static void viewAvailableSlots() 
    {
        System.out.println("View Available Appointment Slots");
        patientManager.viewAvailableSlots();
    }

    private static void scheduleAppointment() 
    {
        System.out.println("Schedule an Appointment");
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot schedule an appointment. Invalid Patient ID.");
            return;
        }
    
        System.out.println("Debug: Scheduling appointment for Patient ID: " + patientID);
        patientManager.scheduleAppointment(patientID);
    }

    private static void viewScheduledAppointments() {
        System.out.println("View Scheduled Appointments");
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot view appointments. Invalid Patient ID.");
            return;
        }
    
        System.out.println("Debug: Viewing scheduled appointments for Patient ID: " + patientID);
        patientManager.viewScheduledAppointments(patientID);
    }

    private static void cancelAppointment() {
        System.out.println("Cancel an Appointment");
    
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot cancel appointment. Invalid Patient ID.");
            return;
        }
    
        // Cancel an appointment interactively (slot-based selection)
        patientManager.cancelAppointment();
    }

    private static void rescheduleAppointment() {
        System.out.println("Reschedule an Appointment");
    
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot reschedule appointment. Invalid Patient ID.");
            return;
        }
    
        // Display scheduled appointments for the patient with a selection number
        int appointmentToReschedule = patientManager.getAppointmentToReschedule(patientID);
    
        if (appointmentToReschedule == -1) {
            System.out.println("Error: No appointment selected to reschedule.");
            return;
        }
    
        // Remove the selected appointment from Appt_List and add back to Availability_List
        boolean rescheduled = patientManager.rescheduleAppointment(appointmentToReschedule);
    
        if (rescheduled) {
            System.out.println("Appointment successfully canceled and added back to available slots.");
            // Call scheduleAppointment function to reschedule a new appointment
            patientManager.scheduleAppointment(patientID);
        } else {
            System.out.println("Error: Unable to reschedule appointment. Please try again.");
        }
    }

    private static void viewPastAppointmentOutcomeRecords() {
        System.out.println("View Past Appointment Outcome Records");
    
        String patientID = getPatientID();
    
        if (patientID == null) {
            System.out.println("Error: Cannot view appointment outcomes. Invalid Patient ID.");
            return;
        }
    
        // Display the past appointment outcomes for the patient
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
