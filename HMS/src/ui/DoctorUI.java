package HMS.src.ui;

import HMS.src.management.doctor.*;
import HMS.src.medicalrecordsPDT.MedicalRecordManager;
import HMS.src.utils.InputScanner;
import static HMS.src.utils.ValidationHelper.validateIntRange;
// import HMS.src.io_new.MedicationCsvHelper;

public class DoctorUI {

    // private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();
    
    public static void displayOptions(){
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|           Welcome Doctor!          |");    
        System.out.println("=====================================");

        // String medFilePath =  medicationCsvHelper.getFilePath();

        boolean quit = false;
        do {
            int doctorChoice = validateIntRange("Please select option: \n1. View Patient Medical Record\n2. Update Patient Medical Records\n3. View Personal Schedule \n4. Set Availability for Appointments \n5. Accept or Decline Appointment Requests \n6. View Upcoming Appointments \n7. Record AppointmentOutcome \n8. Logout \nEnter your choice:", 1, 8);

            DoctorManager doctorManager = new DoctorManager();
            InputScanner sc = InputScanner.getInstance();
            System.out.println();

            switch(doctorChoice) {
                case 1:
                    System.out.println("=====================================");
                    System.out.println("|    View Patient Medical Records    |");
                    System.out.println("Enter Patient's patientID: ");
                    String patientID = sc.nextLine();
                    doctorManager.viewPatientMedicalRecords(patientID);
                    break; //tbd
                case 2:
                    System.out.println("=====================================");
                    System.out.println("|   Update Patient Medical Records   |"); 
                    System.out.print("Enter Patient's patientID: ");
                    sc.next();
                    patientID = sc.nextLine();
            
                    // Check if the patient ID is valid
                    while (!MedicalRecordManager.patientExists(patientID)) {
                        System.out.println("Patient with that patient ID does not exist! Enter a valid patient ID:");
                        patientID = sc.nextLine();
                    }
            
                    // Get diagnosis and treatment information
                    System.out.print("Enter the diagnosis for Patient " + patientID + ": ");
                    String diagnosis = sc.nextLine();
                    System.out.print("Enter the treatment plan for Patient " + patientID + ": ");
                    String treatment = sc.nextLine();
            
                    // Add the diagnosis and treatment as a new entry in the patient's record
                    MedicalRecordManager.addEntryToRecord(patientID, diagnosis, treatment);
                    System.out.print("Enter the prescription for Patient " + patientID + " (optional, leave blank if none): ");
                    String prescription = sc.nextLine();
                    if (!prescription.isEmpty()) {
                        MedicalRecordManager.addPrescriptionToLatestEntry(patientID, prescription);
                        System.out.println("Prescription added to the latest entry with status 'PENDING'.");
                    }
                    System.out.print("Would you like to update the prescription status? (yes/no): ");
                    String updateStatusChoice = sc.nextLine();
                    if (updateStatusChoice.equalsIgnoreCase("yes")) {
                        System.out.print("Enter the new status for prescription '" + prescription + "': ");
                        String status = sc.nextLine();
                        MedicalRecordManager.updatePrescriptionStatusInLatestEntry(patientID, prescription, status);
                        System.out.println("Prescription status updated to '" + status + "'.");
                    }
                    break;
                case 3:
                    System.out.println("View Personal Schedule"); //tbd
                    break;
                case 4:
                    System.out.println("Set Availability for Appointments"); //tbd
                    break;
                case 5:
                    System.out.println("Accept or Decline Appointment Requests"); //tbd
                    break;
                case 6:
                    System.out.println("View Upcoming Appointments"); //tbd
                    break;
                case 7:
                    System.out.println("Record AppointmentOutcome"); //tbd
                    break;
                case 8:
                System.out.println("Logging out...\nRedirecting to Main Menu...\n");
                    quit = true;
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