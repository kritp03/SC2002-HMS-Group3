package HMS.src.ui;

import static HMS.src.utils.ValidationHelper.validateIntRange;

import HMS.src.patient.PatientManager;

// import static HMS.src.patient.PatientManager;
// import HMS.src.io_new.MedicationCsvHelper;

public class PatientUI {
    private static PatientManager patientManager = new PatientManager();


    // private static MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();
    
    public static void displayOptions(){
        System.out.println("=====================================");
        System.out.println("|                Menu                |");
        System.out.println("|          Welcome Patient!          |");    
        System.out.println("=====================================");

        // String medFilePath =  medicationCsvHelper.getFilePath();

        boolean quit = false;
        do {
            int patientChoice = validateIntRange("Please select option: \n1. View Medical Record\n2. Update Personal Information\n3. View Available Appointment Slots \n4. Schedule an Appointment \n5. Reschedule an Appointment \n6. Cancel an Appointment \n7. View Scheduled Appointments \n8. View Past Appointment Outcome Records \n9. Logout \n", 1, 9);
            System.out.println();

            switch(patientChoice) {
                case 1:
                    patientManager.showPatientAndRecords();
                    break; 
                case 2:
                    patientManager.updatePatientContactInfo();
                    break;
                case 3:
                    System.out.println("View Available Appointment Slots"); //tbd
                    break;
                case 4:
                    System.out.println("Schedule an Appointment"); //tbd
                    break;
                case 5:
                    System.out.println("Reschedule an Appointment"); //tbd
                    break;
                case 6:
                    System.out.println("Cancel an Appointment"); //tbd
                    break;
                case 7:
                    System.out.println("View Scheduled Appointments"); //tbd
                    break;
                case 8:
                    System.out.println("View Past Appointment Outcome Records"); //tbd
                    break;
                case 9:
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