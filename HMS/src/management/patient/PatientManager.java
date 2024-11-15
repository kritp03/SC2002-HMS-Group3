package HMS.src.management.patient;

import java.util.List;
import java.util.Scanner;

import HMS.src.io_new.PatientCsvHelper;
import HMS.src.io_new.MedicalRecordCsvHelper;

public class PatientManager {
    private PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
    private MedicalRecordCsvHelper medicalRecordCsvHelper = new MedicalRecordCsvHelper();
    private Scanner scanner = new Scanner(System.in);

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
                System.out.println("Contact Information: " + patient[5]);
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
        System.out.print("Enter the patient ID to display information: ");
        String patientID = scanner.nextLine().trim();

        displayPatientInfo(patientID);
        displayMedicalRecords(patientID);
    }

    public void updatePatientContactInfo() {
        System.out.print("Enter the patient ID for which you want to update contact info: ");
        String patientID = scanner.nextLine().trim().toUpperCase();

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

        System.out.println("Current contact information: " + patientToUpdate[5]);
        String newContactInfo = "";
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter new contact information (Email or Phone Number): ");
            newContactInfo = scanner.nextLine().trim();
            if (newContactInfo.matches("^(\\d{8})$|^([8|9]\\d{7})$|^(.+)@(.+)\\.(.+)$")) {
                validInput = true;
            } else {
                System.out.println("\nInvalid contact information. Please ensure it is a valid email or a phone number.");
            }
        }

        patientToUpdate[5] = newContactInfo; // Update the contact information field

        patientCsvHelper.updateEntry(patientID, patientToUpdate); // Update the entry in CSV

        System.out.println("\nInformation successfully added!");
        displayPatientInfo(patientID);
    }
    
}