package HMS.src.prescription;

import java.util.List;
import java.util.Scanner;

import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.PrescriptionCsvHelper;

import java.time.LocalDate;

public class PrescriptionManager {
    private Scanner scanner = new Scanner(System.in);
    private PrescriptionCsvHelper prescriptionCsvHelper = new PrescriptionCsvHelper();
    private MedicationCsvHelper medicationCsvHelper = new MedicationCsvHelper();

    public void runPrescriptionUpdateProcess() {
        List<String[]> prescriptions = showAllPrescriptions();
        String prescriptionId = getPrescriptionId();
        if ("B".equalsIgnoreCase(prescriptionId)) {
            return;
        }
        processStatusUpdate(prescriptions, prescriptionId);
    }

    private List<String[]> showAllPrescriptions() {
        List<String[]> data = prescriptionCsvHelper.readCSV();
        if (data.size() <= 1) {
            System.out.println("No prescriptions found in the file.");
            return data;
        }
        printPrescriptions(data, true);
        return data;
    }

    private String getPrescriptionId() {
        System.out.print("Enter the Prescription ID to edit or 'B' to go back: ");
        String prescriptionId = scanner.nextLine().trim();
        while (prescriptionId.isEmpty()) {
            System.out.println("Input cannot be empty. Please try again or enter 'B' to go back.\n");
            prescriptionId = scanner.nextLine().trim();
        }
        return prescriptionId;
    }

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

    private void updatePrescriptionStatus(String[] prescription) {
        String newStatus = getNewStatus();
        if ("D".equalsIgnoreCase(newStatus)) {
            prescription[3] = "DISPENSED";
            prescription[7] = LocalDate.now().toString();
            prescriptionCsvHelper.updatePrescriptionById(prescription[0], prescription);
            decrementStock(prescription[1]);
            System.out.println("Prescription has been dispensed!");
        } else if ("C".equalsIgnoreCase(newStatus)) {
            prescription[3] = "CANCELLED";
            prescriptionCsvHelper.updatePrescriptionById(prescription[0], prescription);
            System.out.println("Prescription has been cancelled.");

        }else if ("B".equalsIgnoreCase(newStatus)) {
            return;
        } else {
            System.out.println("Invalid input. No changes made.");
        }
        showAllPrescriptions();
    }

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

    private String getNewStatus() {
        System.out.print("Enter 'D' to dispense, 'C' to cancel or 'B' to go back: ");
        return scanner.nextLine().trim().toUpperCase();
    }

    private void printPrescriptions(List<String[]> data, boolean skipHeader) {
        System.out.println(
                "+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
        System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s |\n",
                "Prescription ID", "Medicine Name", "Dosage", "Status", "Patient Name", "Requested By",
                "Date of Request", "Date of Approval");
        System.out.println(
                "+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
        int startIdx = skipHeader ? 1 : 0;
        for (int i = startIdx; i < data.size(); i++) {
            String[] row = data.get(i);
            String status = row[3].trim().toUpperCase();
            String coloredStatus = PrescriptionStatus.valueOf(status).showStatusByColor();
            System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s |\n",
                    row[0], row[1], row[2], coloredStatus, row[4], row[5], row[6], row[7]);
        }
        System.out.println(
                "+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
    }

}