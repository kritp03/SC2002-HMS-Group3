package HMS.src.user;

import HMS.src.io.MedicalRecordCsvHelper;
import HMS.src.io.PatientCsvHelper;
import HMS.src.utils.SessionManager;
import java.util.List;
import java.util.Scanner;

public class PatientManager {
    private PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
    private MedicalRecordCsvHelper medicalRecordCsvHelper = new MedicalRecordCsvHelper();
    private Scanner scanner = new Scanner(System.in);

    private String getPatientID() {
        String patientID = SessionManager.getCurrentUserID();
        if (patientID == null || !"Patient".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            throw new IllegalStateException("No patient is logged in.");
        }
        return patientID;
    }

    public void displayPatientInfo() {
        String patientID = getPatientID(); // Fetch patientID dynamically
        List<String[]> patients = patientCsvHelper.readCSV();
        boolean foundPatient = false;
        for (String[] patient : patients) {
            if (patient.length > 0 && patient[0].equalsIgnoreCase(patientID)) {
                foundPatient = true;
                System.out.println("\nPatient Information:");
                System.out.println("=======================");
                System.out.println("ID: " + patient[0]);
                System.out.println("Name: " + patient[1]);
                System.out.println("DOB: " + patient[2]);
                System.out.println("Gender: " + patient[3]);
                System.out.println("Blood Type: " + patient[4]);
                System.out.println("Email: " + patient[5]);
                System.out.println("Phone: " + patient[6]);
                System.out.println("Address: " + patient[7]);
                System.out.println("Next of Kin: " + patient[8]);
                System.out.println("Next of Kin Phone: " + patient[9]);
                break;
            }
        }
        if (!foundPatient) {
            System.out.println("No patient found with ID: " + patientID);
        }
    }

    public void displayMedicalRecords() {
        String patientID = getPatientID(); // Fetch patientID dynamically
        List<String[]> medicalRecords = medicalRecordCsvHelper.readCSV();

        boolean recordFound = false;
        System.out.println("Medical Records for Patient ID: " + patientID);
        System.out.println("================================================================");

        for (String[] record : medicalRecords) {
            if (record.length >= 5 && record[4].equalsIgnoreCase(patientID)) {
                System.out.println("Record ID: " + record[0]);
                System.out.println("Diagnosis: " + (record[1].isEmpty() ? "N/A" : record[1]));
                System.out.println("Treatment Plan: " + (record[2].isEmpty() ? "N/A" : record[2]));
                System.out.println("Prescriptions: " + (record[3].isEmpty() ? "N/A" : record[3]));
                System.out.println("----------------------------------------------------------------");
                recordFound = true;
            }
        }

        if (!recordFound) {
            System.out.println("No medical records found for patient ID: " + patientID);
        }
    }

    public void showPatientAndRecords() {
        try {
            displayPatientInfo();
            displayMedicalRecords();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
    
    // public void updatePatientContactInfo() {
    
    //     List<String[]> patients = patientCsvHelper.readCSV();
    //     String[] patientToUpdate = null;
    
    //     for (String[] patient : patients) {
    //         if (patient.length > 0 && patient[0].equalsIgnoreCase(patientID)) {
    //             patientToUpdate = patient;
    //             break;
    //         }
    //     }
    
    //     if (patientToUpdate == null) {
    //         System.out.println("No patient found with ID: " + patientID);
    //         return;
    //     }
    
    //     System.out.println("Current email: " + patientToUpdate[5]);
    //     System.out.println("Current phone number: " + patientToUpdate[6]);
    
    //     System.out.print("Do you want to update email (E) or phone number (P)? ");
    //     String choice = scanner.nextLine().trim().toUpperCase();
    
    //     if (!choice.equals("E") && !choice.equals("P")) {
    //         System.out.println("Invalid choice. Please enter 'E' for email or 'P' for phone number.");
    //         return;
    //     }
    
    //     String newContactInfo = "";
    //     boolean validInput = false;
    //     while (!validInput) {
    //         if (choice.equals("E")) {
    //             System.out.print("Enter new email: ");
    //         } else {
    //             System.out.print("Enter new phone number: ");
    //         }
    //         newContactInfo = scanner.nextLine().trim();
    //         if (choice.equals("E") && newContactInfo.matches("^(.+)@(.+)\\.(.+)$")) {
    //             validInput = true;
    //             patientToUpdate[5] = newContactInfo;
    //         } else if (choice.equals("P") && newContactInfo.matches("^[8|9]\\d{7}$")) {
    //             validInput = true;
    //             patientToUpdate[6] = newContactInfo; 
    //         } else {
    //             System.out.println("Invalid. Please provide a valid input.");
    //         }
    //     }
    
    //     patientCsvHelper.updateEntry(patientID, patientToUpdate); // Update the entry in CSV
    
    //     System.out.println("\nContact information updated successfully!");
    //     displayPatientInfo(patientID); // Display updated patient info
    // }
    
}