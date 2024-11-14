package HMS.src.io_new;

import HMS.src.management.*;
import HMS.src.medication.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class FileIO {

    private static final String filepath = "./HMS/src/com.cmas/database/";

    private static final String STAFF_LIST_FILEPATH = "HMS/data/Staff_List.csv";

    private static final String PATIENT_LIST_FILEPATH = "HMS/data/Patient_List.csv";

    private static final String MEDICINE_LIST_FILEPATH = "HMS/data/Medicine_List.csv";

    public static void serializeObject(String fileName, Serializable object) {
        String filePath = filepath + fileName;
        ByteArrayOutputStream bcos = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bcos)) {
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

    public static Object deserializeObject(String fileName) throws Exception {
        fileName = filepath + fileName;
        Object object = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)); ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.getDecoder().decode(reader.readLine())); ObjectInputStream in = new ObjectInputStream(byteArrayInputStream)) {
            object = in.readObject();
            System.out.println("Object deserialized from " + fileName);
            return object;
        }
    }

    public static void processCSV(HashMap<String, User> usersData, String filePath, String fileType) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                switch (fileType) {
                    case "Patient":
                        // Fields: "Patient ID", "Name", "Date of Birth", "Gender", "Blood Type", "Contact Information"
                        String patientId = data[0].trim();
                        String patientName = data[1].trim();
                        LocalDate dob = LocalDate.parse(data[2].trim());
                        Gender gender = Gender.valueOf(data[3].trim().toUpperCase());
                        String bloodType = data[4].trim();
                        String contactEmail = data[5].trim();

                        // Create a ContactInformation object for the patient
                        ContactInformation patientContactInfo = new ContactInformation();
                        patientContactInfo.changeEmailId(contactEmail);  // Email set here

                        // Create and add Patient instance to usersData
                        Patient patient = new Patient(patientName, patientId, dob, gender, patientContactInfo, bloodType, new ArrayList<>(), new ArrayList<>());
                        usersData.put(patientId, patient);
                        break;

                    case "Staff":
                        // Fields: "Staff ID", "Name", "Role", "Gender", "Age"
                        String staffId = data[0].trim();
                        String staffName = data[1].trim();
                        String role = data[2].trim();
                        Gender staffGender = Gender.valueOf(data[3].trim().toUpperCase());
                        int age = Integer.parseInt(data[4].trim());

                        // Create ContactInformation for staff with default email format
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
                                staffMember = new Doctor(staffId, staffName, staffEmail, age, staffGender); // Assuming doctor constructor uses this email format
                                break;
                            default:
                                System.out.println("Unknown role: " + role);
                                continue;
                        }
                        usersData.put(staffId, staffMember);
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

    public static void processMedicineCSV(HashMap<String, Medication> medicines, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String medicineName = data[0].trim();
                int initialStock = Integer.parseInt(data[1].trim());
                int lowStockLevel = Integer.parseInt(data[2].trim());

                // Create and add Medication to map
                Medication medicine = new Medication(medicineName, initialStock, lowStockLevel);
                medicines.put(medicineName, medicine);
            }
        } catch (Exception e) {
            System.out.println("Error processing Medicine CSV: " + e.getMessage());
        }
    }

    public static void loadDefaultUserData(HashMap<String, User> usersData, HashMap<String, Medication> medicines) {
        processCSV(usersData, STAFF_LIST_FILEPATH, "Staff");
        processCSV(usersData, PATIENT_LIST_FILEPATH, "Patient");
        processMedicineCSV(medicines, MEDICINE_LIST_FILEPATH);
    }
}
