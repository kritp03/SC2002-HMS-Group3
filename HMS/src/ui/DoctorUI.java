package HMS.src.ui;

import HMS.src.appointment.SlotManager;
import HMS.src.archive.Database;
import HMS.src.authorisation.PasswordManager;
import HMS.src.medicalrecordsPDT.MedicalRecordManager;
import HMS.src.user.DoctorManager;
import HMS.src.user.Patient;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DoctorUI {

    private static final PasswordManager passwordManager = new PasswordManager();
    static String doctorID;
        
        public static void displayOptions() {
            System.out.println("=====================================");
            System.out.println("|                Menu                |");
            System.out.println("|           Welcome Doctor!          |");
            System.out.println("=====================================");
        
            HashMap<String, Patient> patients = Database.getPatientData();
            
            // if (patients == null || patients.isEmpty()) {
            //     System.out.println("No patient data found in the system.");
            //     return;
            // }
    
            boolean quit = false;
            InputScanner sc = InputScanner.getInstance();
           
            // Main loop for doctor actions
            do {
                int doctorChoice = validateIntRange("""
                                                    Please select option: 
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
                        1, 9);
        
                System.out.println();
        
                switch (doctorChoice) {
                case 1 -> {
                    System.out.println("View Patient Medical Records");
                    System.out.print("Enter Patient ID: ");
                    String patientID = sc.next();
                    MedicalRecordManager.getMedicalRecords(patientID); // Assuming this method prints records
                    break;
                }
    
                // case 2 -> {
                //     System.out.println("Update Patient Medical Records");
                //     // Add implementation to update medical records here
                //     System.out.print("Enter Patient ID: ");
                //     String patientID = sc.next();
                //     // update logic functiohn not done
                //     break;

                // }
    
                case 3 -> {
                    System.out.println("View Personal Schedule"); 
                    System.out.print("Enter Doctor ID: ");
                    String doctorID = sc.next();
                    SlotManager.printSlots(doctorID); 
                    break;

                }
    
                case 4 -> {
                    System.out.println("Set Unavailability for Appointments");
                    System.out.print("Enter Doctor ID: ");
                    String doctorID = sc.next();
                    System.out.print("Enter Timing Unavailable (YYYY-MM-DDTHH:MM): ");
                    
                    // Get the time input and convert it to LocalDateTime
                    String timeInput = sc.next(); // Expecting format: YYYY-MM-DDTHH:MM
                    try {
                        LocalDateTime time = LocalDateTime.parse(timeInput); // Parse string to LocalDateTime
                
                        // Set the availability to false (unavailable) for this doctor at the given time
                        SlotManager.setAvailability(doctorID, time, false);
                        System.out.println("Doctor " + doctorID + " set as unavailable for " + time);
                    } catch (Exception e) {
                        System.out.println("Invalid time format. Please use the correct format (YYYY-MM-DDTHH:MM).");
                    }
                    break;

                }
                
                case 5 -> {
                    System.out.println("Accept Appointment Requests");
                    System.out.print("Enter AppointmentID: ");
                    String apptID = sc.next();  // Getting the appointment ID from user
                    DoctorManager.acceptAppointment(apptID, doctorID);  // Passing appointment ID and doctor ID
                    break;
                }
                
                case 6 -> {
                    System.out.println("Decline Appointment Requests");
                    System.out.print("Enter AppointmentID: ");
                    String apptID = sc.next();  // Getting the appointment ID from user
                    DoctorManager.declineAppointment(apptID, doctorID);  // Passing appointment ID and doctor ID
                    break;
               
                }
                


                case 7 -> {
                    System.out.println("View Upcoming Appointments");
                    System.out.print("Enter the date (YYYY-MM-DD): ");
                    String dateString = sc.next();
                    LocalDate date = LocalDate.parse(dateString);  // Parse the date input
                    DoctorManager.viewScheduleForDay(date, doctorID);
                    break;
                }
                
                    case 8 -> {
                    System.out.println("Record Appointment Outcome"); 
                    DoctorManager.recordAppointmentOutcome(doctorID);
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
