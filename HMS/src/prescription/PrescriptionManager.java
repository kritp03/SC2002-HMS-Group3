package HMS.src.prescription;

import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.PrescriptionCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.utils.SessionManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the processing, updating, and displaying of prescription records.
 */
public class PrescriptionManager {
    private final Scanner scanner = new Scanner(System.in); // Scanner for user input
    private final PrescriptionCsvHelper prescriptionCsvHelper = new PrescriptionCsvHelper(); // Helper for managing prescriptions in CSV
    private final MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper(); // Helper for managing medications in CSV
    private static StaffCsvHelper staffCsvHelper = new StaffCsvHelper();

    List<String[]> prescriptionRecords = prescriptionCsvHelper.readCSV();

    /**
     * Runs the prescription update process, allowing the user to view and modify
     * prescriptions.
     */
    public void runPrescriptionUpdateProcess() {
        List<String[]> prescriptions = showAllPrescriptions();
        String prescriptionId = getPrescriptionId();
        if ("B".equalsIgnoreCase(prescriptionId)) {
            return;
        }
        processStatusUpdate(prescriptions, prescriptionId);
    }

    /**
     * Displays all prescriptions and returns the list of prescription data.
     *
     * @return List of prescription records from the CSV.
     */
    private List<String[]> showAllPrescriptions() {
        List<String[]> data = prescriptionCsvHelper.readCSV();
        if (data.size() <= 1) {
            System.out.println("No prescriptions found in the file.");
            return data;
        }
        printPrescriptions(data, true);
        return data;
    }

    /**
     * Prompts the user to input a prescription ID to edit.
     *
     * @return The prescription ID entered by the user.
     */
    private String getPrescriptionId() {
        System.out.print("Enter the Prescription ID to edit or 'B' to go back: ");
        String prescriptionId = scanner.nextLine().trim();
        while (prescriptionId.isEmpty()) {
            System.out.println("Input cannot be empty. Please try again or enter 'B' to go back.\n");
            prescriptionId = scanner.nextLine().trim();
        }
        return prescriptionId;
    }


    /**
     * Processes the status update for a specific prescription.
     *
     * @param prescriptions List of prescriptions.
     * @param prescriptionId The ID of the prescription to update.
     */
    private void processStatusUpdate(List<String[]> prescriptions, String prescriptionId) {
        String[] prescription = prescriptionCsvHelper.getPrescriptionById(prescriptionId);
        if (prescription == null) {
            System.out.println("Prescription ID not found.");
            return;
        }
        if ("CANCELLED".equalsIgnoreCase(prescription[3])) {
            System.out.println("This prescription has been cancelled and cannot be dispensed.");
            return;
        }
        if ("DISPENSED".equalsIgnoreCase(prescription[3])) {
            System.out.println("This prescription has already been dispensed.");
            return;
        }
        updatePrescriptionStatus(prescription);
    }

    /**
     * Gets the pharmacist name by its ID.
     * @param pharmID The ID of the pharmacist
     * @return The pharmacist name if found, null otherwise
     */
    public static String getPharmNameByID(String pharmID) {
        List<String[]> pharm = staffCsvHelper.readCSV();
        for (int i = 1; i < pharm.size(); i++) {
            if (pharm.get(i)[0].equalsIgnoreCase(pharmID)) {
                return pharm.get(i)[1];
            }
        }
        return null;
    }

    /**
     * Updates the status of a prescription.
     *
     * @param prescription The prescription data to update.
     */
    private void updatePrescriptionStatus(String[] prescription) {
        String newStatus = getNewStatus();
        if ("D".equalsIgnoreCase(newStatus)) {
            prescription[2] = "DISPENSED";
            prescription[6] = LocalDate.now().toString();
            prescription[7] = getPharmNameByID(SessionManager.getCurrentUserID());
            prescriptionCsvHelper.updatePrescriptionById(prescription[0], prescription);
            decrementStock(prescription[1]);
            System.out.println("Prescription has been dispensed!");
        } else if ("C".equalsIgnoreCase(newStatus)) {
            prescription[3] = "CANCELLED";
            prescriptionCsvHelper.updatePrescriptionById(prescription[0], prescription);
            System.out.println("Prescription has been cancelled.");
        } else if ("B".equalsIgnoreCase(newStatus)) {
            return;
        } else {
            System.out.println("Invalid input. No changes made.");
        }
        showAllPrescriptions();
    }

    /**
     * Decrements the stock of a specific medication.
     *
     * @param medicineName The name of the medicine to decrement.
     */
    private void decrementStock(String medicineName) {
        List<String[]> allMedicines = medicationCsvHelper.readCSV();
        boolean isUpdated = false;
        for (String[] medicine : allMedicines) {
            if (medicineName.equalsIgnoreCase(medicine[1])) { // Assuming medicine[0] is the name, medicine[3] is stock left
                int stockLeft = Integer.parseInt(medicine[4]) - 1;
                medicine[4] = String.valueOf(stockLeft);
                isUpdated = true;
                break;
            }
        }
        if (isUpdated) {
            medicationCsvHelper.updateMedications(allMedicines);
        } else {
            System.out.println("An error occurred while updating the stock.");
        }
    }

    /**
     * Prompts the user to enter a new status for the prescription.
     *
     * @return The new status entered by the user.
     */
    private String getNewStatus() {
        System.out.print("Enter 'D' to dispense, 'C' to cancel or 'B' to go back: ");
        return scanner.nextLine().trim().toUpperCase();
    }

    /**
     * Prints the list of prescriptions in a tabular format.
     *
     * @param data List of prescription records.
     * @param skipHeader Whether to skip the header row in the data.
     */
    private void printPrescriptions(List<String[]> data, boolean skipHeader) {
        System.out.println(
                "+--------------+---------------+----------+--------------+---------------+----------------+----------------+-------------------+");
        System.out.format("| %-12s | %-13s | %-8s | %-14s | %-13s | %-14s | %-14s | %-11s |\n",
                "PrescriptionID", "Medicine Name", "Status", "Patient Name", "Requested By",
                "Date of Request", "Date of Approval", "Approved By");
        System.out.println(
                "+--------------+---------------+----------+--------------+---------------+----------------+----------------+-------------------+");
    
        int startIdx = skipHeader ? 1 : 0;  // Skip header if necessary
        for (int i = startIdx; i < data.size(); i++) {
            String[] row = data.get(i);
            // Make sure each row has enough columns to prevent IndexOutOfBoundsException
            if (row.length >= 8) {
                String status = row[2].trim().toUpperCase();
                String coloredStatus = PrescriptionStatus.valueOf(status).showStatusByColor();
                System.out.format("| %-12s | %-13s | %-20s | %-14s | %-13s | %-14s | %-16s | %-9s |\n",
                        row[0], // PrescriptionID
                        row[1], // Medicine Name
                        coloredStatus, // Status
                        row[3], // Patient Name
                        row[4], // Requested By
                        row[5], // Date of Request
                        row[6], // Date of Approval
                        row[7]); // Approved By
            }
        }
        System.out.println(
                "+--------------+---------------+----------+--------------+---------------+----------------+----------------+---------------+");
    }
    

    // Helper method to find the next available Record ID
    public static String getNextRecordID(List<String[]> prescriptionRecords) {
        int highestRecordID = 0;
    
        // Iterate over all records and find the highest Record ID that starts with "PR"
        for (String[] record : prescriptionRecords) {
            if (record.length > 0 && record[0].startsWith("PR")) {
                try {
                    // Get numeric part after "PR" and find the highest number
                    int recordID = Integer.parseInt(record[0].substring(2)); 
                    highestRecordID = Math.max(highestRecordID, recordID);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing RecordID: " + record[0]);
                }
            }
        }
    
        // Return the next Record ID as "PR" followed by the incremented number
        return "PR" + String.format("%03d", highestRecordID + 1);
    }

    /**
     * Closes the scanner to prevent resource leaks.
     */
    public void closeScanner() {
        scanner.close();
    }

    public static void main(String[] args) {
        PrescriptionManager prescriptionManager = new PrescriptionManager();
        prescriptionManager.runPrescriptionUpdateProcess();
        prescriptionManager.closeScanner();
    }
}
