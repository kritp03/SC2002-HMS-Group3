package HMS.src.medicalrecordsPDT;

import HMS.src.database.Database;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecordManager {
    private static Map<String, MedicalRecord> records = new HashMap<>();

    // Add a new medical record for a patient
    public static void addMedicalRecord(String patientID, MedicalRecord record) {
        records.put(patientID, record);
    }
    

    // Retrieve a medical record for a patient by patient ID
    public static MedicalRecord getMedicalRecord(String patientID) {
        return records.get(patientID);
    }

    // Check if a patient exists
    public static boolean patientExists(String patientID) 
    {
        Database database = Database.getInstance();
        Database.loadPatients();
        return Database.getPatientData().containsKey(patientID);
    }


    // Add a new entry with diagnosis and treatment to a patient's medical record
    public static void addEntryToRecord(String patientID, String diagnosis, String treatment) {
        MedicalRecord record = getMedicalRecord(patientID);
        if (record != null) {
            record.addEntry(diagnosis, treatment);
            System.out.println("Added new entry to medical record for patient ID: " + patientID);
        } else {
            System.out.println("Patient with ID " + patientID + " does not have a medical record.");
        }
    }

    // Add a prescription to the latest entry in a patient's medical record
    public static void addPrescriptionToLatestEntry(String patientID, String medicationName) {
        MedicalRecord record = getMedicalRecord(patientID);
        if (record != null) {
            record.addPrescriptionToLatestEntry(medicationName);
            System.out.println("Added prescription to the latest entry for patient ID: " + patientID);
        } else {
            System.out.println("Patient with ID " + patientID + " does not have a medical record.");
        }
    }

    // Update the prescription status in the latest entry of a patient's medical record
    public static void updatePrescriptionStatusInLatestEntry(String patientID, String medicationName, String status) {
        MedicalRecord record = getMedicalRecord(patientID);
        if (record != null) {
            record.updatePrescriptionStatusInLatestEntry(medicationName, status);
            System.out.println("Updated prescription status in the latest entry for patient ID: " + patientID);
        } else {
            System.out.println("Patient with ID " + patientID + " does not have a medical record.");
        }
    }

    public static void initializeMedicalRecordsForPatients() {
        for (String patientID : HMS.src.database.Database.getPatientData().keySet()) {
            if (!records.containsKey(patientID)) {
                records.put(patientID, new MedicalRecord(patientID, LocalDate.now()));
                System.out.println("Initialized medical record for patient ID: " + patientID);
            }
        }
    }
}

