package HMS.src.medicalrecordsPDT;

import HMS.src.archive.Database;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MedicalRecordManager {
    private static final Map<String, List<MedicalRecord>> records = new HashMap<>();

    // Add a new medical record for a patient
    public static void addMedicalRecord(String patientID, MedicalRecord record) {
        records.putIfAbsent(patientID, new ArrayList<>());
        records.get(patientID).add(record);
    }

    // Retrieve all medical records for a patient
    public static List<MedicalRecord> getMedicalRecords(String patientID) {
        return records.getOrDefault(patientID, new ArrayList<>());
    }


    // Check if a patient exists
    public static boolean patientExists(String patientID) {
        return records.containsKey(patientID);
    }

    // Add a new entry with diagnosis and treatment to a patient's medical record
    public static void addEntryToRecord(String patientID, String diagnosis, String treatment) {
        List<MedicalRecord> record = getMedicalRecords(patientID);
        if (record != null) {
            MedicalEntry entry = new MedicalEntry(diagnosis, treatment);
            ((MedicalRecord) record).addEntry(entry);
            System.out.println("Added new entry to medical record for patient ID: " + patientID);
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

