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
import java.time.LocalTime;
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
                    5. Accept/Decline Appointment Requests
                    6. View Upcoming Appointments
                    7. Record Appointment Outcome
                    8. Reset Password
                    9. Logout
                    Enter your choice: """,
                    1, 10);
            System.out.println();

            switch (doctorChoice) {
                case 1 -> viewPatientMedicalRecords();
                case 2 -> updatePatientMedicalRecords();
                case 3 -> viewPersonalSchedule();
                case 4 -> setAvailabilityForAppointments();
                case 5 -> acceptDeclineAppointment();
                case 6 -> viewUpcomingAppointments();
                case 7 -> recordAppointmentOutcome();
                case 8 -> passwordManager.changePassword();
                case 9 -> {
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

    private static void setAvailabilityForAppointments() {

        Scanner scanner = new Scanner(System.in);
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        System.out.println("Set Availability for Appointments");

        String doctorID = getDoctorID();
        LocalDate date = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        boolean validInput = false;

        // Get date
        System.out.print("Enter the date in 'DD-MM-YYYY' format: ");
        while (date == null) {
            try {
                String dateInput = scanner.nextLine().trim();
                date = LocalDate.parse(dateInput, dateFormatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use 'DD-MM-YYYY' format.");
            }
        }

        // Get start and end times
        do {
            try {
                System.out.print("Enter start of availability in 'HH:MM' format: ");
                String startInput = scanner.nextLine().trim();
                startTime = LocalTime.parse(startInput, timeFormatter);

                System.out.print("Enter end of availability in 'HH:MM' format: ");
                String endInput = scanner.nextLine().trim();
                endTime = LocalTime.parse(endInput, timeFormatter);

                if (endTime.isAfter(startTime)) {
                    validInput = true;
                } else {
                    System.out.println("End time must be after the start time. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Invalid time format. Please use 'HH:MM' format.");
            }
        } while (!validInput);

        // Store each hour slot between start and end times
        AvailabilityCsvHelper availabilityHelper = new AvailabilityCsvHelper();
        while (startTime.isBefore(endTime)) {
            String[] availabilityEntry = {
                    doctorID,
                    date.toString(),
                    startTime.toString()
            };
            availabilityHelper.addAvailability(availabilityEntry);
            System.out.println("Slot added for " + date.toString() + " at " + startTime.toString());
            startTime = startTime.plusHours(1);
        }

        System.out.println("Availability set for Dr. " + doctorID);
    }

    // private static void acceptAppointmentRequests() {
    // DoctorManager.viewAllAppts(getDoctorID());
    // // InputScanner.getInstance().nextLine(); // Clear the buffer after int input
    // System.out.println("Accept Appointment Requests");
    // String apptID = validateString("Enter Appointment ID: ");
    // String doctorID = getDoctorID();
    // DoctorManager.acceptAppointment(apptID, doctorID); // Reuse doctorID
    // }

    private static Scanner scanner = new Scanner(System.in);

    public static void acceptDeclineAppointment() {
        String doctorID = getDoctorID();
        boolean hasPending = DoctorManager.viewAllPending(doctorID); // This will return true if there are pending
                                                                     // appointments

        if (hasPending) {
            System.out.print("Enter Appointment ID to proceed: ");
            String apptID = scanner.nextLine().trim();
            if (apptID.isEmpty()) {
                System.out.println("No Appointment ID entered. Exiting...");
                return;
            }

            System.out.print("Do you want to Accept (A) or Decline (D) the appointment? Enter A or D: ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if ("A".equals(choice)) {
                DoctorManager.acceptAppointment(apptID, doctorID);
            } else if ("D".equals(choice)) {
                DoctorManager.declineAppointment(apptID, doctorID);
            } else {
                System.out.println("Invalid choice entered. Please enter only A (Accept) or D (Decline).");
            }
        } else {
            System.out.println("\n");
        }
    }

    private static void recordAppointmentOutcome() {
        DoctorManager.viewAllConfirmed(getDoctorID());
        System.out.print("Enter Appointment ID to proceed: ");
        String apptID = scanner.nextLine().trim().toUpperCase();
        if (apptID.isEmpty()) {
            System.out.println("No Appointment ID entered. Exiting...");
            return;
        }
        DoctorManager.recordAppointmentOutcome(apptID);
    }

    private static void viewUpcomingAppointments() {
        DoctorManager.viewAllConfirmedAndPending(getDoctorID());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            displayOptions();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        scanner.close();
    }
}
