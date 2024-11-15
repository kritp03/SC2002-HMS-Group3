package HMS.src.database;

import HMS.src.appointment.Appointment;
import HMS.src.io_new.FileIO;
import HMS.src.io_new.MedicalRecordCsvHelper;
import HMS.src.io_new.PatientCsvHelper;
import HMS.src.management.ContactInformation;
import HMS.src.management.Gender;
import HMS.src.management.User;
import HMS.src.management.patient.Patient;
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
    private static Database instance = null;
    private static final String DATA_PATH = "HMS/data/";  
    private static final String USERS_FILE = DATA_PATH + "Users.txt";  
    private static final String MEDICINE_FILE = DATA_PATH + "Medicine.txt";  

    private static HashMap<String, User> USER_DATA = new HashMap<>();
    private static HashMap<String, Medication> MEDICINE_DATA = new HashMap<>();
    private static HashMap<String, Patient> PATIENT_DATA = new HashMap<>();
    private static List<Appointment> APPOINTMENTS = new ArrayList<>();
    private static List<ReplenishmentRequest> REPLENISHMENT_REQUESTS = new ArrayList<>();
    private static User currentUser;

    private Database() {
        loadDatabase();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
            instance.loadDatabase();
            System.out.println("Database initialized and loaded.");
        }
        return instance;
    }

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

    public void saveToDatabase() {
        FileIO.serializeObject(USERS_FILE, USER_DATA);
        FileIO.serializeObject(MEDICINE_FILE, MEDICINE_DATA);
        saveMedicalRecords();
    }

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

    public static void loadMedicalRecords() {
        MedicalRecordCsvHelper medicalRecordHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalData = medicalRecordHelper.readCSV();
    
        if (medicalData == null || medicalData.isEmpty()) {
            System.out.println("MedicalRecordCsvHelper returned no data.");
            return;
        }
    
        // Skip header
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
    
                // Check if the record already exists for the patient
                List<MedicalRecord> records = MedicalRecordManager.getMedicalRecords(patientID);
                boolean recordExists = records.stream()
                    .anyMatch(record -> record.getRecordID().equals(recordID) &&
                            record.getEntries().stream().anyMatch(entry ->
                                entry.getDiagnosis().equals(diagnosis) &&
                                entry.getTreatment().equals(treatmentPlan) &&
                                String.join(", ", entry.getPrescriptions().keySet()).equals(prescriptions)));
    
                if (recordExists) {
                    continue; // Skip duplicate records
                }
    
                // Create a new MedicalEntry
                MedicalEntry entry = new MedicalEntry();
                entry.setDiagnosis(diagnosis);
                entry.setTreatment(treatmentPlan);
                if (!prescriptions.equals("-")) {
                    for (String prescription : prescriptions.split(", ")) {
                        entry.addPrescription(prescription);
                    }
                }
    
                // Add the entry to a new or existing MedicalRecord
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
    
    
    public static void saveMedicalRecords() {
        MedicalRecordCsvHelper medicalRecordHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalData = new ArrayList<>();
    
        // Add header to the CSV
        medicalData.add(new String[]{"recordID", "diagnosis", "treatmentPlan", "prescriptions", "Patient ID"});
    
        for (String patientID : MedicalRecordManager.getAllPatientIDs()) {
            List<MedicalRecord> records = MedicalRecordManager.getMedicalRecords(patientID);

            Set<String> uniqueEntries = new HashSet<>(); // Track unique entries

            for (MedicalRecord record : records) {
                for (MedicalEntry entry : record.getEntries()) {
                    String entryKey = String.format(
                        "%s|%s|%s|%s", 
                        record.getRecordID(),
                        entry.getDiagnosis(),
                        entry.getTreatment(),
                        String.join(", ", entry.getPrescriptions().keySet())
                    );

                    // Skip duplicates
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

        // Write to CSV
        medicalRecordHelper.updateMedications(medicalData);
        System.out.println("Medical records saved to file.");
    }

    

    public static HashMap<String, Patient> getPatientData() 
    {
        return PATIENT_DATA;
    }

    public static HashMap<String, User> getUserData() 
    {
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
