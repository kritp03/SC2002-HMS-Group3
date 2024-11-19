package HMS.src.prescription;

import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.PrescriptionCsvHelper;
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
        updatePrescriptionStatus(prescription);
    }

    /**
     * Updates the status of a prescription.
     *
     * @param prescription The prescription data to update.
     */
    private void updatePrescriptionStatus(String[] prescription) {
        String newStatus = getNewStatus();
        if ("D".equalsIgnoreCase(newStatus)) {
            prescription[3] = "DISPENSED";
            prescription[7] = LocalDate.now().toString();
            prescription[8] = SessionManager.getCurrentUserID();
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
            if (medicineName.equals(medicine[0])) { // Assuming medicine[0] is the name, medicine[3] is stock left
                int stockLeft = Integer.parseInt(medicine[3]) - 1;
                medicine[3] = String.valueOf(stockLeft);
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
                "+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+----------------+");
        System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s | %-14s |\n",
                "Prescription ID", "Medicine Name", "Dosage", "Status", "Patient Name", "Requested By",
                "Date of Request", "Date of Approval", "Approved by");
        System.out.println(
                "+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+----------------+");
        int startIdx = skipHeader ? 1 : 0;
        for (int i = startIdx; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length >= 9) { // Ensure there is an "Approved by" data in the row
                String status = row[3].trim().toUpperCase();
                String coloredStatus = PrescriptionStatus.valueOf(status).showStatusByColor();
                System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s | %-14s |\n",
                        row[0], row[1], row[2], coloredStatus, row[4], row[5], row[6], row[7], row[8]);
            }
        }
        System.out.println(
                "+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+----------------+");
    }

    /**
     * Closes the scanner to prevent resource leaks.
     */
    public void closeScanner() {
        scanner.close();
    }
}
