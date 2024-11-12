package HMS.src.database;

import HMS.src.management.*;
import HMS.src.medication.*;
import HMS.src.misc_classes.FileIO;

import java.util.HashMap;

public class Database 
{
    private static Database instance = null;

    private static HashMap<String, User> USER_DATA = new HashMap<>();

    private static HashMap<String, Medication> MEDICINE_DATA = new HashMap<>();

    @SuppressWarnings("OverridableMethodCallInConstructor")
    private Database()
    {
        loadDatabase();
    }

    public static Database getInstance() 
    {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    @SuppressWarnings("unchecked")
    public void loadDatabase() 
    {
        try
        {
            USER_DATA = (HashMap<String, User>) FileIO.deserializeObject("Users.txt");
            MEDICINE_DATA = (HashMap<String, Medication>) FileIO.deserializeObject("Medicine.txt");
        } catch (Exception e) {
            FileIO.loadDefaultUserData(USER_DATA, MEDICINE_DATA);
        }
    }

    public void saveToDatabase() 
    {
        FileIO.serializeObject("Users.txt", USER_DATA);
        FileIO.serializeObject("Camps.txt", MEDICINE_DATA);

    }

    public static HashMap<String, User> getUserData() 
    {
        return USER_DATA;
    }

    public static HashMap<String, Medication> getMedicineData() {
        return MEDICINE_DATA;
    }
}
