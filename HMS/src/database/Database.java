package HMS.src.database;

import HMS.src.appointment.Appointment;
import HMS.src.io_new.FileIO;
import HMS.src.io_new.PatientCsvHelper;
import HMS.src.management.ContactInformation;
import HMS.src.management.Gender;
import HMS.src.management.User;
import HMS.src.management.patient.Patient;
import HMS.src.medicalrecordsPDT.MedicalRecordManager;
import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Database {
    // Singleton instance of the Database
    private static Database instance = null;
    
    // Path to the data folder and files within it
    private static final String DATA_PATH = "HMS/data/";  
    private static final String USERS_FILE = DATA_PATH + "Users.txt";  
    private static final String MEDICINE_FILE = DATA_PATH + "Medicine.txt";  
    
    // Data structures for managing hospital data
    private static HashMap<String, User> USER_DATA = new HashMap<>();
    private static HashMap<String, Medication> MEDICINE_DATA = new HashMap<>();
    private static HashMap<String, Patient> PATIENT_DATA = new HashMap<>();  // Add PATIENT_DATA
    private static List<Appointment> APPOINTMENTS = new ArrayList<>();
    private static List<ReplenishmentRequest> REPLENISHMENT_REQUESTS = new ArrayList<>();
    
    // Currently logged-in user
    private static User currentUser;

    // Private constructor for Singleton pattern
    private Database() {
        loadDatabase();
    }

    // Singleton getInstance method
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
            instance.loadDatabase(); // Ensure database loads properly
            System.out.println("Database initialized and loaded.");
        }
        return instance;
    }
    
    

    // Method to load data from files into the application's memory
    @SuppressWarnings("unchecked")
    public void loadDatabase() {
    try {
        USER_DATA = (HashMap<String, User>) FileIO.deserializeObject(USERS_FILE);
        MEDICINE_DATA = (HashMap<String, Medication>) FileIO.deserializeObject(MEDICINE_FILE);
        loadPatients();
        MedicalRecordManager.initializeMedicalRecordsForPatients(); // Initialize medical records
    } catch (Exception e) {
        FileIO.loadDefaultUserData(USER_DATA, MEDICINE_DATA);
    }
}


    // Method to save data back to files
    public void saveToDatabase() {
        FileIO.serializeObject(USERS_FILE, USER_DATA);
        FileIO.serializeObject(MEDICINE_FILE, MEDICINE_DATA);
    }

    // Getter for USER_DATA HashMap
    public static HashMap<String, User> getUserData() {
        return USER_DATA;
    }

    // Getter for PATIENT_DATA HashMap
    public static HashMap<String, Patient> getPatientData() {
        return PATIENT_DATA;
    }

    // Getter for MEDICINE_DATA HashMap
    public static HashMap<String, Medication> getMedicineData() {
        return MEDICINE_DATA;
    }

    // Getter for APPOINTMENTS List
    public static List<Appointment> getAppointments() {
        return APPOINTMENTS;
    }

    // Getter for REPLENISHMENT_REQUESTS List
    public static List<ReplenishmentRequest> getReplenishmentRequests() {
        return REPLENISHMENT_REQUESTS;
    }

    // Method to set the current logged-in user
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    // Getter for currentUser (used in AdminUI and other UIs)
    public static User getCurrentUser() {
        return currentUser;
    }

    // Method to load patients from CSV into PATIENT_DATA
    public static void loadPatients() {
        PatientCsvHelper patientHelper = new PatientCsvHelper();
        List<String[]> patientData = patientHelper.readCSV();
    
        if (patientData == null || patientData.isEmpty()) {
            System.out.println("PatientCsvHelper returned no data. Ensure the file exists and has data.");
            return;
        }
    
        // Skip the header row
        boolean isHeader = true;
    
        for (String[] data : patientData) {
            if (isHeader) {
                isHeader = false;
                continue;  // Skip the first row
            }
    
            if (data.length < 6) {
                System.out.println("Invalid patient data: " + String.join(",", data));
                continue;
            }
    
            try {
                String patientID = data[0].trim();
                String name = data[1].trim();
                LocalDate dob = LocalDate.parse(data[2].trim());
                Gender gender = Gender.valueOf(data[3].trim().toUpperCase());
                String bloodType = data[4].trim();
                String contactEmail = data[5].trim();
    
                ContactInformation patientContactInfo = new ContactInformation();
                patientContactInfo.changeEmailId(contactEmail);
    
                // Create Patient object and add to PATIENT_DATA
                Patient patient = new Patient(patientID, name, dob, gender, bloodType, patientContactInfo);
                PATIENT_DATA.put(patientID, patient);
            } catch (Exception e) {
                System.out.println("Error processing patient record: " + String.join(",", data));
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String [] args)
    {
        Database.loadPatients();
    }
}

    
