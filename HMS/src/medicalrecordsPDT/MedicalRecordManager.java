package HMS.src.medicalrecordsPDT;

import java.util.HashMap;
import java.util.Map;

public class MedicalRecordManager {
    private static final Map<String, MedicalRecord> records = new HashMap<>();

    public static void addMedicalRecord(MedicalRecord record) {
        records.put(record.getPatientID(), record);
    }

    public static MedicalRecord getMedicalRecord(String patientID) {
        return records.get(patientID);
    }
}
