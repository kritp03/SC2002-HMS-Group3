package HMS.src.archive;

import HMS.src.appointment.Appointment;
import HMS.src.io.FileIO;
import HMS.src.io.MedicalRecordCsvHelper;
import HMS.src.io.PatientCsvHelper;
import HMS.src.user.ContactInformation;
import HMS.src.user.Gender;
import HMS.src.user.User;
import HMS.src.user.patient.Patient;
import HMS.src.medicalrecordsPDT.MedicalEntry;
import HMS.src.medicalrecordsPDT.MedicalRecord;
import HMS.src.medicalrecordsPDT.MedicalRecordManager;
import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Database {
    // Singleton instance
    private static Database instance = null;

    // File paths
    private static final String DATA_PATH = "HMS/data/";
    private static final String USERS_FILE = DATA_PATH + "Users.txt";
    private static final String MEDICINE_FILE = DATA_PATH + "Medicine.txt";

    // Data structures
    private static HashMap<String, User> USER_DATA = new HashMap<>();
    private static HashMap<String, Medication> MEDICINE_DATA = new HashMap<>();
    private static HashMap<String, Patient> PATIENT_DATA = new HashMap<>();
    private static List<Appointment> APPOINTMENTS = new ArrayList<>();
    private static List<ReplenishmentRequest> REPLENISHMENT_REQUESTS = new ArrayList<>();
    private static User currentUser;

    // Private constructor for Singleton
    private Database() {
        loadDatabase();
    }

    // Singleton getInstance method
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
            instance.loadDatabase();
            System.out.println("Database initialized and loaded.");
        }
        return instance;
    }

    // Load data from files into memory
    @SuppressWarnings("unchecked")
    public void loadDatabase() {
        try {
            USER_DATA = (HashMap<String, User>) FileIO.deserializeObject(USERS_FILE);
            MEDICINE_DATA = (HashMap<String, Medication>) FileIO.deserializeObject(MEDICINE_FILE);
            loadPatients();
            loadMedicalRecords();
            MedicalRecordManager.initializeMedicalRecordsForPatients();
        } catch (Exception e) {
            FileIO.loadDefaultUserData(USER_DATA, MEDICINE_DATA);
        }
    }

    // Save data back to files
    public void saveToDatabase() {
        FileIO.serializeObject(USERS_FILE, USER_DATA);
        FileIO.serializeObject(MEDICINE_FILE, MEDICINE_DATA);
        saveMedicalRecords();
    }

    // Load patients from CSV
    public static void loadPatients() {
        PatientCsvHelper patientHelper = new PatientCsvHelper();
        List<String[]> patientData = patientHelper.readCSV();
        if (patientData == null || patientData.isEmpty()) {
            System.out.println("PatientCsvHelper returned no data.");
            return;
        }

        boolean isHeader = true;
        for (String[] data : patientData) {
            if (isHeader) {
                isHeader = false;
                continue;
            }
            if (data.length < 6) continue;

            try {
                String patientID = data[0].trim();
                String name = data[1].trim();
                LocalDate dob = LocalDate.parse(data[2].trim());
                Gender gender = Gender.valueOf(data[3].trim().toUpperCase());
                String bloodType = data[4].trim();
                String contactEmail = data[5].trim();
                ContactInformation patientContactInfo = new ContactInformation();
                patientContactInfo.changeEmailId(contactEmail);
                Patient patient = new Patient(patientID, name, dob, gender, bloodType, patientContactInfo);
                PATIENT_DATA.put(patientID, patient);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Load medical records from CSV
    public static void loadMedicalRecords() {
        MedicalRecordCsvHelper medicalRecordHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalData = medicalRecordHelper.readCSV();

        if (medicalData == null || medicalData.isEmpty()) {
            System.out.println("MedicalRecordCsvHelper returned no data.");
            return;
        }

        boolean isHeader = true;
        for (String[] data : medicalData) {
            if (isHeader) {
                isHeader = false;
                continue;
            }
            if (data.length < 5) continue;

            try {
                String recordID = data[0].trim();
                String diagnosis = data[1].trim();
                String treatmentPlan = data[2].trim();
                String prescriptions = data[3].trim();
                String patientID = data[4].trim();

                List<MedicalRecord> records = MedicalRecordManager.getMedicalRecords(patientID);
                boolean recordExists = records.stream()
                    .anyMatch(record -> record.getRecordID().equals(recordID) &&
                            record.getEntries().stream().anyMatch(entry ->
                                entry.getDiagnosis().equals(diagnosis) &&
                                entry.getTreatment().equals(treatmentPlan) &&
                                String.join(", ", entry.getPrescriptions().keySet()).equals(prescriptions)));

                if (recordExists) {
                    continue;
                }

                MedicalEntry entry = new MedicalEntry();
                entry.setDiagnosis(diagnosis);
                entry.setTreatment(treatmentPlan);
                if (!prescriptions.equals("-")) {
                    for (String prescription : prescriptions.split(", ")) {
                        entry.addPrescription(prescription);
                    }
                }

                if (records.isEmpty()) {
                    MedicalRecord newRecord = new MedicalRecord(recordID, patientID, LocalDate.now());
                    newRecord.addEntry(entry);
                    MedicalRecordManager.addMedicalRecord(patientID, newRecord);
                } else {
                    records.get(records.size() - 1).addEntry(entry);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Loaded medical records for patients.");
    }

    // Save medical records to CSV
    public static void saveMedicalRecords() {
        MedicalRecordCsvHelper medicalRecordHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalData = new ArrayList<>();
        medicalData.add(new String[]{"recordID", "diagnosis", "treatmentPlan", "prescriptions", "Patient ID"});

        for (String patientID : MedicalRecordManager.getAllPatientIDs()) {
            List<MedicalRecord> records = MedicalRecordManager.getMedicalRecords(patientID);

            Set<String> uniqueEntries = new HashSet<>();
            for (MedicalRecord record : records) {
                for (MedicalEntry entry : record.getEntries()) {
                    String entryKey = String.format(
                        "%s|%s|%s|%s",
                        record.getRecordID(),
                        entry.getDiagnosis(),
                        entry.getTreatment(),
                        String.join(", ", entry.getPrescriptions().keySet())
                    );
                    if (uniqueEntries.contains(entryKey)) continue;
                    uniqueEntries.add(entryKey);

                    medicalData.add(new String[]{
                        record.getRecordID(),
                        entry.getDiagnosis() != null ? entry.getDiagnosis() : "-",
                        entry.getTreatment() != null ? entry.getTreatment() : "-",
                        String.join(", ", entry.getPrescriptions().keySet()),
                        patientID
                    });
                }
            }
        }

        medicalRecordHelper.updateMedications(medicalData);
        System.out.println("Medical records saved to file.");
    }

    // Getters
    public static HashMap<String, Patient> getPatientData() {
        return PATIENT_DATA;
    }

    public static HashMap<String, User> getUserData() {
        return USER_DATA;
    }

    public static List<ReplenishmentRequest> getReplenishmentRequests() {
        return REPLENISHMENT_REQUESTS;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static List<Appointment> getAppointments() {
        return APPOINTMENTS;
    }

    public static HashMap<String, Medication> getMedicineData() {
        return MEDICINE_DATA;
    }
}
