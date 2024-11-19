package HMS.src.io;

import HMS.src.medication.*;
import HMS.src.user.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Handles file input/output operations for the HMS system, including serialization,
 * deserialization, and processing of CSV files.
 */
public class FileIO {

    private static final String filepath = "./HMS/src/com.cmas/database/";
    private static final String STAFF_LIST_FILEPATH = "HMS/data/Staff_List.csv";
    private static final String PATIENT_LIST_FILEPATH = "HMS/data/Patient_List.csv";
    private static final String MEDICINE_LIST_FILEPATH = "HMS/data/Medicine_List.csv";

    /**
     * Serializes an object and saves it to a file in Base64 format.
     *
     * @param fileName The name of the file to save the serialized object.
     * @param object   The object to serialize.
     */
    public static void serializeObject(String fileName, Serializable object) {
        String filePath = filepath + fileName;
        try (ByteArrayOutputStream bcos = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(bcos)) {

            out.writeObject(object);
            out.flush();

            byte[] binary = bcos.toByteArray();
            String base64String = Base64.getEncoder().encodeToString(binary);

            Files.write(Paths.get(filePath), base64String.getBytes());

            System.out.println("Object serialized and saved to " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserializes an object from a Base64-encoded file.
     *
     * @param fileName The name of the file to deserialize the object from.
     * @return The deserialized object.
     * @throws Exception If an error occurs during deserialization.
     */
    public static Object deserializeObject(String fileName) throws Exception {
        fileName = filepath + fileName;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName));
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                     Base64.getDecoder().decode(reader.readLine()));
             ObjectInputStream in = new ObjectInputStream(byteArrayInputStream)) {

            Object object = in.readObject();
            System.out.println("Object deserialized from " + fileName);
            return object;
        }
    }

    /**
     * Processes a CSV file to populate user data into the given map.
     *
     * @param usersData A map to store the user data.
     * @param filePath  The path to the CSV file.
     * @param fileType  The type of users in the CSV file (e.g., "Patient", "Staff").
     */
    public static void processCSV(HashMap<String, User> usersData, String filePath, String fileType) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                switch (fileType) {
                    case "Patient":
                        processPatientData(usersData, data);
                        break;

                    case "Staff":
                        processStaffData(usersData, data);
                        break;

                    default:
                        System.out.println("Unsupported file type: " + fileType);
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error processing CSV: " + e.getMessage());
        }
    }

    /**
     * Processes the patient data and adds it to the users map.
     *
     * @param usersData The map to store the user data.
     * @param data      The patient data from the CSV file.
     */
    private static void processPatientData(HashMap<String, User> usersData, String[] data) {
        String patientId = data[0].trim();
        String patientName = data[1].trim();
        LocalDate dob = LocalDate.parse(data[2].trim());
        Gender gender = Gender.valueOf(data[3].trim().toUpperCase());
        String bloodType = data[4].trim();
        String contactEmail = data[5].trim();

        ContactInformation patientContactInfo = new ContactInformation();
        patientContactInfo.changeEmailId(contactEmail);

        Patient patient = new Patient(patientName, patientId, dob, gender, patientContactInfo, bloodType,
                new ArrayList<>(), new ArrayList<>());
        usersData.put(patientId, patient);
    }

    /**
     * Processes the staff data and adds it to the users map.
     *
     * @param usersData The map to store the user data.
     * @param data      The staff data from the CSV file.
     */
    private static void processStaffData(HashMap<String, User> usersData, String[] data) {
        String staffId = data[0].trim();
        String staffName = data[1].trim();
        String role = data[2].trim();
        Gender staffGender = Gender.valueOf(data[3].trim().toUpperCase());
        int age = Integer.parseInt(data[4].trim());

        String staffEmail = staffId + "@example.com";
        ContactInformation staffContactInfo = new ContactInformation();
        staffContactInfo.changeEmailId(staffEmail);

        User staffMember;
        switch (role) {
            case "Administrator":
                staffMember = new Administrator(staffId, staffName, staffEmail, age, staffGender);
                break;
            case "Pharmacist":
                staffMember = new Pharmacist(staffId, staffName, staffEmail, age, staffGender);
                break;
            case "Doctor":
                staffMember = new Doctor(staffId, staffName, staffEmail, age, staffGender);
                break;
            default:
                System.out.println("Unknown role: " + role);
                return;
        }
        usersData.put(staffId, staffMember);
    }

    /**
     * Processes a CSV file containing medication data and populates the medicines map.
     *
     * @param medicines A map to store the medication data.
     * @param filePath  The path to the CSV file.
     */
    public static void processMedicineCSV(HashMap<String, Medication> medicines, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String medicineName = data[0].trim();
                int initialStock = Integer.parseInt(data[1].trim());
                int lowStockLevel = Integer.parseInt(data[2].trim());

                Medication medicine = new Medication(medicineName, initialStock, lowStockLevel);
                medicines.put(medicineName, medicine);
            }
        } catch (Exception e) {
            System.out.println("Error processing Medicine CSV: " + e.getMessage());
        }
    }

    /**
     * Loads default user data (staff, patients) and medication data from their respective CSV files.
     *
     * @param usersData A map to store the user data.
     * @param medicines A map to store the medication data.
     */
    public static void loadDefaultUserData(HashMap<String, User> usersData, HashMap<String, Medication> medicines) {
        processCSV(usersData, STAFF_LIST_FILEPATH, "Staff");
        processCSV(usersData, PATIENT_LIST_FILEPATH, "Patient");
        processMedicineCSV(medicines, MEDICINE_LIST_FILEPATH);
    }
}
