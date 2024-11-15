package HMS.src.medicalrecordsPDT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import HMS.src.database.Database;

public class MedicalRecordManager {
    private static Map<String, List<MedicalRecord>> records = new HashMap<>();

    // Add a new medical record for a patient
    public static void addMedicalRecord(String patientID, MedicalRecord record) {
        records.putIfAbsent(patientID, new ArrayList<>());
        records.get(patientID).add(record);
    }

    // Retrieve all medical records for a patient
    public static List<MedicalRecord> getMedicalRecords(String patientID) {
        return records.getOrDefault(patientID, new ArrayList<>());
    }

    // Retrieve the latest medical record for a patient
    public static MedicalRecord getMedicalRecord(String patientID) {
        List<MedicalRecord> patientRecords = getMedicalRecords(patientID);
        if (patientRecords.isEmpty()) {
            return null;
        }
        return patientRecords.get(patientRecords.size() - 1);
    }

    // Check if a patient exists
    public static boolean patientExists(String patientID) {
        return records.containsKey(patientID);
    }

    // Add a new entry with diagnosis and treatment to a patient's medical record
    public static void addEntryToRecord(String patientID, String diagnosis, String treatment) {
        MedicalRecord record = getMedicalRecord(patientID);
        if (record != null) {
            MedicalEntry entry = new MedicalEntry(diagnosis, treatment);
            record.addEntry(entry);
            System.out.println("Added new entry to medical record for patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    // Add a prescription to the latest entry in a patient's medical record
    public static void addPrescriptionToLatestEntry(String patientID, String medicationName) {
        MedicalRecord record = getMedicalRecord(patientID);
        if (record != null) {
            MedicalEntry latestEntry = record.getLatestEntry();
            if (latestEntry != null) {
                latestEntry.addPrescription(medicationName);
                System.out.println("Added prescription to the latest entry for patient ID: " + patientID);
            } else {
                System.out.println("No entries found in the medical record for patient ID: " + patientID);
            }
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    // Initialize medical records for patients without existing records
    public static void initializeMedicalRecordsForPatients() 
    {
        for (String patientID : Database.getPatientData().keySet()) {
            records.putIfAbsent(patientID, new ArrayList<>());
        }
        System.out.println("Medical records initialized for patients.");
    }


    // Get all patient IDs with medical records
    public static Set<String> getAllPatientIDs() {
        return records.keySet();
    }
}

