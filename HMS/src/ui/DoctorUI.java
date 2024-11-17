package HMS.src.ui;

import HMS.src.appointment.SlotManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.medicalrecordsPDT.MedicalRecordManager;
import HMS.src.user.DoctorManager;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DoctorUI {

    private static final PasswordManager passwordManager = new PasswordManager();
    private static String doctorID; // Store Doctor ID globally for session

    public static void displayOptions() {
        InputScanner sc = InputScanner.getInstance();

        // Ask for Doctor ID at the start
        if (doctorID == null) { // Ensure we ask only if not already set
            System.out.print("Enter your Doctor ID to log in: ");
            doctorID = sc.next(); // Save Doctor ID globally
        }

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

            System.out.println();

            switch (doctorChoice) {
                case 1 -> {
                    System.out.println("View Patient Medical Records");
                    System.out.print("Enter Patient ID: ");
                    String patientID = sc.next();
                    MedicalRecordManager.getMedicalRecords(patientID);
                }
                // case 2 -> {
                //     System.out.println("Update Patient Medical Records");
                //     System.out.print("Enter Patient ID: ");
                //     String patientID = sc.next();
                //     // Add logic to update medical records
                // }
                case 3 -> {
                    System.out.println("View Personal Schedule");
                    SlotManager.printSlots(doctorID); // Reuse doctorID
                }
                case 4 -> {
                    System.out.println("Set Unavailability for Appointments");
                    System.out.print("Enter Timing Unavailable (YYYY-MM-DDTHH:MM): ");
                    String timeInput = sc.next();
                    try {
                        LocalDateTime time = LocalDateTime.parse(timeInput);
                        DoctorManager.setUnavailable(doctorID, time); // Reuse doctorID
                        System.out.println("Doctor " + doctorID + " set as unavailable for " + time);
                    } catch (Exception e) {
                        System.out.println("Invalid time format. Please use YYYY-MM-DDTHH:MM.");
                    }
                }
                case 5 -> {
                    System.out.println("Accept Appointment Requests");
                    System.out.print("Enter Appointment ID: ");
                    String apptID = sc.next();
                    DoctorManager.acceptAppointment(apptID, doctorID); // Reuse doctorID
                }
                case 6 -> {
                    System.out.println("Decline Appointment Requests");
                    System.out.print("Enter Appointment ID: ");
                    String apptID = sc.next();
                    DoctorManager.declineAppointment(apptID, doctorID); // Reuse doctorID
                }
                case 7 -> {
                    System.out.println("View Upcoming Appointments");
                    System.out.print("Enter the date (YYYY-MM-DD): ");
                    String dateString = sc.next();
                    LocalDate date = LocalDate.parse(dateString);
                    DoctorManager.viewScheduleForDay(date, doctorID); // Reuse doctorID
                }
                case 8 -> {
                    System.out.println("Record Appointment Outcome");
                    System.out.println("Enter Appointment ID: ");
                    String apptID = sc.nextLine();
                    DoctorManager.recordAppointmentOutcome(apptID);
                }
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

    public static void main(String[] args) {
        displayOptions();
    }
}
