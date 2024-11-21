package HMS.src.ui;

import HMS.src.app.App;
import HMS.src.appointment.SlotManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.authorisation.IPasswordManager;
import HMS.src.io.AvailabilityCsvHelper;
import HMS.src.io.MedicalRecordCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.user.DoctorManager;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * The DoctorUI class provides an interface for doctors to manage their schedule,
 * handle patient appointments, and update medical records.
 */
public class DoctorUI {
    /**
     * Instance of PasswordManager for resetting doctor passwords.
     */
    private static final IPasswordManager passwordManager = new PasswordManager();
    /**
     * Displays the main menu for the doctor and handles user inputs for various options.
     * @throws Exception if an error occurs while executing options
     */

    public static void displayOptions() throws Exception {
        String doctorName = getLoggedInDoctorName(); // Retrieve the logged-in doctor's name

        System.out.println("=====================================");
        System.out.printf("|          Hello %s!          |\n", doctorName); // Display name
        System.out.println("=====================================");

        boolean quit = false;
        do {
            int doctorChoice = validateIntRange(
                """
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
                """,
                1, 9);
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
    * Retrieves the logged-in doctor's name.
    * @return the doctor's name if logged in, or "Doctor" if not logged in.
    */
    private static String getLoggedInDoctorName() {
        if (!SessionManager.isUserLoggedIn() || !"Doctor".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            return "Doctor"; // Default to "Doctor" if no one is logged in
        }

        String doctorID = SessionManager.getCurrentUserID(); // Retrieve logged-in doctor's ID
        List<String[]> staff = new StaffCsvHelper().readCSV(); // Read from Staff_List.csv

        for (String[] staffMember : staff) {
            if (staffMember.length > 1 && staffMember[0].equalsIgnoreCase(doctorID)) {
            return staffMember[1]; // Return the doctor's name
            }
        }
        return "Doctor"; // Fallback if no match is found
    }

    /**
     * Retrieves the current doctor's ID from the session.
     * @return the doctor ID if logged in
     */

    private static String getDoctorID() {
        if (!SessionManager.isUserLoggedIn() || !"Doctor".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            System.out.println("You must log in as a Doctor to access this feature.");
            PickerUI pickerUI = new PickerUI();
            pickerUI.displayLoginOptions(); // Force login
        }
        return SessionManager.getCurrentUserID();
    }

    /**
     * Allows the doctor to view the medical records of a specific patient.
     */

    private static void viewPatientMedicalRecords() {
        InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("View Patient Medical Records");
        String patientID = validateString("Enter Patient ID: ");
        String doctorID = getDoctorID();
        DoctorManager.viewPatientMedicalRecords(patientID,doctorID);
    }

    /**
     * Allows the doctor to update a patient's medical records with diagnosis, treatment, and prescription.
     */

     private static void updatePatientMedicalRecords() {
        InputScanner.getInstance().nextLine(); // Clear the buffer after int input
        System.out.println("Update Patient Medical Records");
    
        // Get the doctor ID from the session
        String doctorID = getDoctorID();
    
        // Prompt for the patient ID
        String patientID = validateString("Enter Patient ID: ");
    
        // Check if the doctor has access to the patient's records
        if (!DoctorManager.isPatientAssignedToDoctor(patientID, doctorID)) {
            System.out.println("Error: You do not have access to update medical records for this patient.");
        } else {
            // Display the patient's current medical records (this shows the history of records)
            DoctorManager.viewPatientMedicalRecords(patientID, doctorID);
            System.err.println("These are the current medical records of Patient " + patientID + ". You can now add a new entry to the patient's medical record.");
    
            // Gather input for the new medical record
            String diagnosis = validateString("Enter the diagnosis for Patient " + patientID + ": ");
            String treatmentPlan = validateString("Enter the treatment for Patient " + patientID + ": ");
            MedicalRecordCsvHelper medicalrecCsvHelper = new MedicalRecordCsvHelper();
            List<String[]> medicalRecords = medicalrecCsvHelper.readCSV();
            String RecordID = DoctorManager.getNextRecordID(medicalRecords);

            String[] newRecord = new String[] {
                RecordID,        // Generate new Record ID
                diagnosis,          // Diagnosis
                treatmentPlan,      // Treatment Plan
                patientID,    // Patient ID
                doctorID
            };       
            medicalRecords.add(newRecord);
            medicalrecCsvHelper.updateMedicalRecords(medicalRecords);
            System.out.println("New medical record added successfully.");
        
        }
    }
    

    /**
     * Displays the doctor's personal schedule based on available and booked slots.
     */
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
    List<String[]> existingRecords = availabilityHelper.readCSV(); // Fetch all existing records

    while (startTime.isBefore(endTime)) {
        String dateStr = date.toString();
        String timeStr = startTime.toString();

        // Check if the slot already exists in the CSV
        boolean slotExists = existingRecords.stream()
            .anyMatch(record -> record[0].equals(doctorID) && record[1].equals(dateStr) && record[2].equals(timeStr));

        if (slotExists) {
            System.out.println("Appointment already available for " + dateStr + " at " + timeStr);
        } else {
            String[] availabilityEntry = {
                    doctorID,
                    dateStr,
                    timeStr
            };
            availabilityHelper.addAvailability(availabilityEntry);
            System.out.println("Slot added for " + dateStr + " at " + timeStr);
        }

        startTime = startTime.plusHours(1);
    }

    System.out.println("Availability set for Dr. " + doctorID);
}


    private static Scanner scanner = new Scanner(System.in);

    /**
     * Handles accepting or declining appointment requests from patients.
     */
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

    /**
     * Records the outcome of an appointment by entering details about diagnosis, treatment, and notes.
     */

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

    /**
     * Displays all confirmed and pending appointments for the doctor.
     */

    private static void viewUpcomingAppointments() {
        DoctorManager.viewAllConfirmedAndPending(getDoctorID());
    }

    /**
     * Displays all confirmed and pending appointments for the doctor.
     */

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
