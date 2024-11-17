package HMS.src.ui;

import HMS.src.app.App;
import HMS.src.appointment.SlotManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.io.AvailabilityCsvHelper;
import HMS.src.user.DoctorManager;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DoctorUI {

    private static final PasswordManager passwordManager = new PasswordManager();

    public static void displayOptions() throws Exception {
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|           Welcome Doctor!          |");
        System.out.println("=====================================");

        boolean quit = false;

        do {
            // Check if the user is still logged in
            if (!SessionManager.isUserLoggedIn() || !"Doctor".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
                System.out.println("You are not logged in. Redirecting to Main Menu...");
                quit = true;
                App.main(null); // Redirect to main menu
                return; // Exit the method to stop further execution
            }

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
            System.out.println();

            switch (doctorChoice) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecords();
                case 3 -> viewPersonalSchedule();
                case 4 -> setAvailablityForAppointments();
                case 5 -> acceptAppointmentRequests();
                case 6 -> declineAppointmentRequests();
                case 7 -> viewUpcomingAppointments();
                case 8 -> recordAppointmentOutcome();
                case 9 -> passwordManager.changePassword();
                case 10 -> {
                    System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
                    SessionManager.logoutUser();
                    App.main(null); // Redirect to main menu
                    return; // Exit the method after logging out
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

    private static void viewPatientMedicalRecords() {
        InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("View Patient Medical Records");
        String patientID = validateString("Enter Patient ID: ");
        DoctorManager.viewPatientMedicalRecords(patientID);
    }

    private static void updatePatientMedicalRecords() {
        InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Update Patient Medical Records");
        String patientID = validateString("Enter Patient ID: ");
        String diagnosis = validateString("Enter the diagnosis for Patient " + patientID + " : ");
        String treatment = validateString("Enter the treatment for Patient " + patientID + " : ");
        System.out.print("Enter the prescription for Patient " + patientID + " : ");
        String prescription = InputScanner.getInstance().nextLine().trim();
        if (prescription.isEmpty()) {
            prescription = null; // Set prescription to null if input is empty
        }
        DoctorManager.updatePatientMedicalRecord(patientID, diagnosis, treatment, prescription);
    }

    private static void viewPersonalSchedule() {
        System.out.println("View Personal Schedule");
        String doctorID = getDoctorID();
        SlotManager.initializeDoctorSlotsFromCSV(doctorID);
        SlotManager.printFullSchedule(doctorID); // Reuse doctorID
    }

    // private static void setAvailablityForAppointments() {
    // System.out.println("Set Availability for Appointments");

    // String doctorID = getDoctorID();
    // LocalDateTime startTime = null;
    // LocalDateTime endTime = null;
    // boolean validInput = false;

    // do {
    // try {
    // // Prompt for start time
    // System.out.print("Enter start of availability in 'DD-MM-YYYY HH:MM' format:
    // ");
    // String startInput = InputScanner.getInstance().nextLine().trim(); // Read and
    // trim input
    // System.out.println("Debug: Start input received: '" + startInput + "'");
    // startTime = LocalDateTime.parse(startInput,
    // DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

    // // Prompt for end time
    // System.out.print("Enter end of availability in 'DD-MM-YYYY HH:MM' format: ");
    // String endInput = InputScanner.getInstance().nextLine().trim(); // Read and
    // trim input
    // System.out.println("Debug: End input received: '" + endInput + "'");
    // endTime = LocalDateTime.parse(endInput,
    // DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

    // // Validate time range
    // if (endTime.isAfter(startTime)) {
    // validInput = true; // Input is valid, exit loop
    // } else {
    // System.out.println("Invalid time range: End time must be after the start
    // time. Please try again.");
    // }
    // } catch (Exception e) {
    // System.out.println("Invalid format. Please use 'DD-MM-YYYY HH:MM' format.");
    // System.out.println("Debug: Error details: " + e.getMessage());
    // }
    // } while (!validInput);

    // // Format the data for writing into the CSV
    // String[] availabilityEntry = {
    // doctorID,
    // startTime.toLocalDate().toString(), // Extract date part
    // startTime.toLocalTime() + "-" + endTime.toLocalTime() // Combine time range
    // };

    // // Write the entry to the CSV file using UnavailabilityCsvHelper
    // AvailabilityCsvHelper unavailabilityHelper = new AvailabilityCsvHelper();
    // unavailabilityHelper.addUnavailability(availabilityEntry);

    // System.out.println("Availability set for Dr. " + doctorID + " on " +
    // startTime.toLocalDate() +
    // " from " + startTime.toLocalTime() + " to " + endTime.toLocalTime());
    // }

    private static Scanner scanner = new Scanner(System.in);

    private static void setAvailablityForAppointments() {
        System.out.println("Set Availability for Appointments");

        String doctorID = getDoctorID();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        boolean validInput = false;

        do {
            try {
                System.out.print("Enter start of availability in 'DD-MM-YYYY HH:MM' format: ");
                String startInput = scanner.nextLine().trim();
                startTime = LocalDateTime.parse(startInput, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                System.out.print("Enter end of availability in 'DD-MM-YYYY HH:MM' format: ");
                String endInput = scanner.nextLine().trim();
                endTime = LocalDateTime.parse(endInput, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                if (endTime.isAfter(startTime)) {
                    validInput = true;
                } else {
                    System.out.println("End time must be after the start time. Please try again.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use 'DD-MM-YYYY HH:MM' format.");
            }
        } while (!validInput);

        String[] availabilityEntry = {
                doctorID,
                startTime.toLocalDate().toString(),
                startTime.toLocalTime() + "-" + endTime.toLocalTime()
        };

        AvailabilityCsvHelper availabilityHelper = new AvailabilityCsvHelper();
        availabilityHelper.addAvailability(availabilityEntry);

        System.out.println("Availability set for Dr. " + doctorID + " on " + startTime.toLocalDate() +
                " from " + startTime.toLocalTime() + " to " + endTime.toLocalTime());

    }

    private static void acceptAppointmentRequests() {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Accept Appointment Requests");
        String apptID = validateString("Enter Appointment ID: ");
        String doctorID = getDoctorID();
        DoctorManager.acceptAppointment(apptID, doctorID); // Reuse doctorID
    }

    private static void declineAppointmentRequests() {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Decline Appointment Requests");
        String apptID = validateString("Enter Appointment ID: ");
        String doctorID = getDoctorID();
        DoctorManager.declineAppointment(apptID, doctorID); // Reuse doctorID
    }

    private static void recordAppointmentOutcome() {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Record Appointment Outcome");
        String apptID = validateString("Enter Appointment ID: ");
        DoctorManager.recordAppointmentOutcome(apptID);
    }

    private static void viewUpcomingAppointments() {
        // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("View Upcoming Appointments");
        LocalDate date = validateDate("Enter the date (DD-MM-YYYY): ");
        String doctorID = getDoctorID();
        DoctorManager.viewScheduleForDay(date, doctorID); // Reuse doctorID
    }

    public static void main(String[] args) {
        try {
            displayOptions();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
