package HMS.src.ui;

import HMS.src.authorisation.PasswordManager;
import HMS.src.database.Database;
import HMS.src.management.doctor.DoctorManager;
import HMS.src.management.patient.Patient;
import HMS.src.utils.InputScanner;
import HMS.src.utils.SessionManager;
import static HMS.src.utils.ValidationHelper.validateIntRange;
import java.util.HashMap;

public class DoctorUI {
    private static PasswordManager passwordManager = new PasswordManager();

    public static void displayOptions() {
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|           Welcome Doctor!          |");
        System.out.println("=====================================");
    
        // Ensure the database is loaded
        Database database = Database.getInstance();
        Database.loadPatients();
        HashMap<String, Patient> patients = Database.getPatientData();
    
        if (patients == null || patients.isEmpty()) 
        {
            System.out.println("No patient data found in the system.");
            return;
        }
    
        boolean quit = false;
        InputScanner sc = InputScanner.getInstance();
        DoctorManager doctorManager = new DoctorManager();
    
        // Main loop for doctor actions
        do {
            int doctorChoice = validateIntRange(
                    "Please select option: \n1. View Patient Medical Record\n2. Update Patient Medical Records\n"
                            + "3. View Personal Schedule \n4. Set Availability for Appointments \n"
                            + "5. Accept or Decline Appointment Requests \n6. View Upcoming Appointments \n"
                            + "7. Record AppointmentOutcome \n8. Reset Password \n9. Logout\nEnter your choice: ",
                    1, 9);
    
            System.out.println();
    
            switch (doctorChoice) {
                case 1:
                    System.out.println("=====================================");
                    System.out.println("|    View Patient Medical Records    |");
                    sc.nextLine();
                    System.out.print("Enter Patient's patientID: ");
                    String viewPatientID = sc.nextLine().trim();
    
                    if (!patients.containsKey(viewPatientID)) {
                        System.out.println("Patient with ID " + viewPatientID + " does not exist.");
                    } else {
                        doctorManager.viewPatientMedicalRecords(viewPatientID);
                    }
                    break;
    
                case 2:
                    System.out.println("=====================================");
                    System.out.println("|   Update Patient Medical Records   |");
                    sc.nextLine();
                    System.out.print("Enter Patient's patientID: ");
                    String updatePatientID = sc.nextLine().trim();
    
                    if (!patients.containsKey(updatePatientID)) {
                        System.out.println("Patient with ID " + updatePatientID + " does not exist.");
                    } else {
                        System.out.print("Enter the diagnosis for Patient " + updatePatientID + ": ");
                        String diagnosis = sc.nextLine();
                        System.out.print("Enter the treatment plan for Patient " + updatePatientID + ": ");
                        String treatment = sc.nextLine();
    
                        // Add diagnosis and treatment to the medical record
                        doctorManager.addDoctorDiagnosis(updatePatientID, diagnosis);
                        doctorManager.addDoctorTreatment(updatePatientID, treatment);
    
                        System.out.print("Enter the prescription for Patient " + updatePatientID + " (optional): ");
                        String prescription = sc.nextLine();
                        if (!prescription.isEmpty()) {
                            doctorManager.addDoctorPrescription(updatePatientID, prescription);
                            System.out.println("Prescription added to the current entry with status 'PENDING'.");
                        }
    
                        doctorManager.finalizeCurrentEntry(updatePatientID);
                    }
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

