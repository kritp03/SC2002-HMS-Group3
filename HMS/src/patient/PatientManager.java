package HMS.src.patient;

import java.util.List;
import java.util.Scanner;

import HMS.src.io_new.PatientCsvHelper;
import HMS.src.io_new.MedicalRecordCsvHelper;

import HMS.src.utils.SessionManager;

public class PatientManager {
    private PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
    private MedicalRecordCsvHelper medicalRecordCsvHelper = new MedicalRecordCsvHelper();
    private Scanner scanner = new Scanner(System.in);
    String patientID = SessionManager.getCurrentUserID();

    private List<String[]> getAllPatientInfo() {
        return patientCsvHelper.readCSV();
    }

    private List<String[]> getAllMedicalRecords() {
        return medicalRecordCsvHelper.readCSV();
    }

    public void displayPatientInfo(String patientID) {
        List<String[]> patients = getAllPatientInfo();
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
                System.out.println("Contact Information: " + patient[5]+"\n");
                break;
            }
        }
        if (!foundPatient) {
            System.out.println("No patient found with ID: " + patientID);
        }
    }

    public void displayMedicalRecords(String patientID) {
        List<String[]> records = getAllMedicalRecords();
        boolean foundRecord = false;
        for (String[] record : records) {
            if (record.length > 0 && record[4].equalsIgnoreCase(patientID)) {
                foundRecord = true;
                System.out.println("\nMedical Records:");
                System.out.println("=======================");
                System.out.println("Record ID: " + record[0]);
                System.out.println("Diagnosis: " + record[1]);
                System.out.println("Treatment: " + record[2]);
                System.out.println("Prescription: " + record[3]);
                System.out.println("----------------------");
            }
        }
        if (!foundRecord) {
            System.out.println("No medical records found for patient ID: " + patientID);
        }
    }

    public void showPatientAndRecords() {
        
        displayPatientInfo(patientID);
        displayMedicalRecords(patientID);
    }
    
    public void updatePatientContactInfo() {
    
        List<String[]> patients = patientCsvHelper.readCSV();
        String[] patientToUpdate = null;
    
        for (String[] patient : patients) {
            if (patient.length > 0 && patient[0].equalsIgnoreCase(patientID)) {
                patientToUpdate = patient;
                break;
            }
        }
    
        if (patientToUpdate == null) {
            System.out.println("No patient found with ID: " + patientID);
            return;
        }
    
        System.out.println("Current email: " + patientToUpdate[5]);
        System.out.println("Current phone number: " + patientToUpdate[6]);
    
        System.out.print("Do you want to update email (E) or phone number (P)? ");
        String choice = scanner.nextLine().trim().toUpperCase();
    
        if (!choice.equals("E") && !choice.equals("P")) {
            System.out.println("Invalid choice. Please enter 'E' for email or 'P' for phone number.");
            return;
        }
    
        String newContactInfo = "";
        boolean validInput = false;
        while (!validInput) {
            if (choice.equals("E")) {
                System.out.print("Enter new email: ");
            } else {
                System.out.print("Enter new phone number: ");
            }
            newContactInfo = scanner.nextLine().trim();
            if (choice.equals("E") && newContactInfo.matches("^(.+)@(.+)\\.(.+)$")) {
                validInput = true;
                patientToUpdate[5] = newContactInfo;
            } else if (choice.equals("P") && newContactInfo.matches("^[8|9]\\d{7}$")) {
                validInput = true;
                patientToUpdate[6] = newContactInfo; 
            } else {
                System.out.println("Invalid. Please provide a valid input.");
            }
        }
    
        patientCsvHelper.updateEntry(patientID, patientToUpdate); // Update the entry in CSV
    
        System.out.println("\nContact information updated successfully!");
        displayPatientInfo(patientID); // Display updated patient info
    }
    
}