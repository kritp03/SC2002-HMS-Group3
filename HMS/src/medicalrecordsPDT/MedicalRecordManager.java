package HMS.src.medicalrecordsPDT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages medical records for patients, allowing for record creation, retrieval, and updates.
 */
public class MedicalRecordManager {
    private static final Map<String, List<MedicalRecord>> records = new HashMap<>(); // Stores medical records by patient ID

    /**
     * Adds a new medical record for a specific patient.
     *
     * @param patientID The ID of the patient.
     * @param record    The medical record to add.
     */
    public static void addMedicalRecord(String patientID, MedicalRecord record) {
        records.putIfAbsent(patientID, new ArrayList<>());
        records.get(patientID).add(record);
    }

    /**
     * Retrieves all medical records for a specific patient.
     *
     * @param patientID The ID of the patient.
     * @return A list of medical records for the patient, or an empty list if no records exist.
     */
    public static List<MedicalRecord> getMedicalRecords(String patientID) {
        return records.getOrDefault(patientID, new ArrayList<>());
    }

    /**
     * Checks if a patient has any medical records.
     *
     * @param patientID The ID of the patient.
     * @return True if the patient has records, false otherwise.
     */
    public static boolean patientExists(String patientID) {
        return records.containsKey(patientID);
    }

    /**
     * Adds a new entry with diagnosis and treatment to the latest medical record for a patient.
     *
     * @param patientID The ID of the patient.
     * @param diagnosis The diagnosis to add.
     * @param treatment The treatment to add.
     */
    public static void addEntryToRecord(String patientID, String diagnosis, String treatment) {
        List<MedicalRecord> recordList = getMedicalRecords(patientID);
        if (recordList != null && !recordList.isEmpty()) {
            MedicalEntry entry = new MedicalEntry(diagnosis, treatment);
            recordList.get(recordList.size() - 1).addEntry(entry); // Add entry to the latest record
            System.out.println("Added new entry to medical record for patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    /**
     * Retrieves the IDs of all patients with medical records.
     *
     * @return A set of patient IDs with medical records.
     */
    public static Set<String> getAllPatientIDs() {
        return records.keySet();
    }
}
