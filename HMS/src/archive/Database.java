// package HMS.src.database;

// import HMS.src.management.*;
// import HMS.src.medication.*;
// import HMS.src.misc_classes.FileIO;

// import java.util.HashMap;

// public class Database 
// {
//     private static Database instance = null;

//     private static HashMap<String, User> USER_DATA = new HashMap<>();

//     private static HashMap<String, Medication> MEDICINE_DATA = new HashMap<>();

//     @SuppressWarnings("OverridableMethodCallInConstructor")
//     private Database()
//     {
//         loadDatabase();
//     }

//     public static Database getInstance() 
//     {
//         if (instance == null)
//             instance = new Database();
//         return instance;
//     }

//     @SuppressWarnings("unchecked")
//     public void loadDatabase() 
//     {
//         try
//         {
//             USER_DATA = (HashMap<String, User>) FileIO.deserializeObject("Users.txt");
//             MEDICINE_DATA = (HashMap<String, Medication>) FileIO.deserializeObject("Medicine.txt");
//         } catch (Exception e) {
//             FileIO.loadDefaultUserData(USER_DATA, MEDICINE_DATA);
//         }
//     }

//     public void saveToDatabase() 
//     {
//         FileIO.serializeObject("Users.txt", USER_DATA);
//         FileIO.serializeObject("Camps.txt", MEDICINE_DATA);

//     }

//     public static HashMap<String, User> getUserData() 
//     {
//         return USER_DATA;
//     }

//     public static HashMap<String, Medication> getMedicineData() {
//         return MEDICINE_DATA;
//     }
// }

package HMS.src.archive;

import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import HMS.src.user.User;
import HMS.src.appointment.Appointment;
import HMS.src.io.FileIO;

import java.util.HashMap;
import java.util.ArrayList;
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
        }
        return instance;
    }

    // Method to load data from files into the application's memory
    @SuppressWarnings("unchecked")
    public void loadDatabase() {
        try {
            // Load users data
            USER_DATA = (HashMap<String, User>) FileIO.deserializeObject(USERS_FILE);
            // Load medication data
            MEDICINE_DATA = (HashMap<String, Medication>) FileIO.deserializeObject(MEDICINE_FILE);
        } catch (Exception e) {
            // Load default data if files do not exist or an error occurs
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
}