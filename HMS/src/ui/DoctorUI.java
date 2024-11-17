package HMS.src.ui;

import HMS.src.appointment.SlotManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.user.DoctorManager;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DoctorUI {

    private static final PasswordManager passwordManager = new PasswordManager();

    public static void displayOptions() {
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|           Welcome Doctor!          |");
        System.out.println("=====================================");

        boolean quit = false;

        // Main loop for doctor actions
        do {
            int doctorChoice = validateIntRange("""
                                                Please select an option:
                                                1. View Patient Medical Record
                                                2. Update Patient Medical Records
                                                3. View Personal Schedule
                                                4. Set Availability for Appointments
                                                5. Accept Appointment Requests
                                                6. Decline Appointment Requests
                                                7. View Upcoming Appointments
                                                8. Record Appointment Outcome
                                                9. Reset Password
                                                10. Logout
                                                Enter your choice: """,
                    1, 10);
            // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
            System.out.println();

            switch (doctorChoice) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecords();
                case 3 -> viewPersonalSchedule();
                case 4 -> setUnavailablityForAppointments();
                case 5 ->acceptAppointmentRequests();
                case 6 -> declineAppointmentRequests();
                case 7 -> viewUpcomingAppointments();
                case 8 -> recordAppointmentOutcome();
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

    private static String getDoctorID() {
        if (!SessionManager.isUserLoggedIn() || !"Doctor".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            System.out.println("You must log in as a Doctor to access this feature.");
            PickerUI pickerUI = new PickerUI();
            pickerUI.displayLoginOptions(); // Force login
        }
        return SessionManager.getCurrentUserID();
    }

    private static void viewPatientMedicalRecords()
    {
        InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("View Patient Medical Records");
        String patientID = validateString("Enter Patient ID: ");
        DoctorManager.viewPatientMedicalRecords(patientID);
    }

    private static void updatePatientMedicalRecords()
    {
        InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Update Patient Medical Records");
        String patientID = validateString("Enter Patient ID: ");
        String diagnosis = validateString("Enter the diagnosis for Patient " + patientID +" : ");
        String treatment = validateString("Enter the treatment for Patient " + patientID + " : ");
        System.out.print("Enter the prescription for Patient " + patientID + " : ");
        String prescription = InputScanner.getInstance().nextLine().trim();
        if (prescription.isEmpty()) 
        {
            prescription = null; // Set prescription to null if input is empty
        }
        DoctorManager.updatePatientMedicalRecord(patientID,diagnosis,treatment, prescription);
    }
    
    private static void viewPersonalSchedule()
    {
        System.out.println("View Personal Schedule");
        String doctorID = getDoctorID();
        SlotManager.printSlots(doctorID); // Reuse doctorID
    }
    private static void setUnavailablityForAppointments()
    {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Set Unavailability for Appointments");
        String timeInput = validateString("Enter Timing Unavailable (DD-MM_YYYY): ");
        LocalDateTime time = validateDateTime(timeInput);
        String doctorID = getDoctorID();
        DoctorManager.setUnavailable(doctorID, time); // Reuse doctorID
        System.out.println("Doctor " + doctorID + " set as unavailable for " + time);
    }

    private static void acceptAppointmentRequests()
    {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Accept Appointment Requests");
        String apptID = validateString("Enter Appointment ID: ");
        String doctorID = getDoctorID();
        DoctorManager.acceptAppointment(apptID, doctorID); // Reuse doctorID
    }

    private static void declineAppointmentRequests()
    {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Decline Appointment Requests");
        String apptID = validateString("Enter Appointment ID: ");
        String doctorID = getDoctorID();
        DoctorManager.declineAppointment(apptID, doctorID); // Reuse doctorID
    }
    private static void recordAppointmentOutcome()
    {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Record Appointment Outcome");
        String apptID = validateString("Enter Appointment ID: ");
        DoctorManager.recordAppointmentOutcome(apptID);
    }

    private static void viewUpcomingAppointments()
    {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("View Upcoming Appointments");
        LocalDate date = validateDate("Enter the date (DD-MM-YYYY): ");
        String doctorID = getDoctorID();
        DoctorManager.viewScheduleForDay(date, doctorID); // Reuse doctorID
    }

    public static void main(String[] args) {
        displayOptions();
    }
}
