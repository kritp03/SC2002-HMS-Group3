package HMS.src.prescription;

import HMS.src.io_new.PrescriptionCsvHelper;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;

public class PrescriptionManager {
    private Scanner scanner = new Scanner(System.in);
    private PrescriptionCsvHelper prescriptionCsvHelper = new PrescriptionCsvHelper();

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
            System.out.println("Prescription status updated to DISPENSED with today's date.");
        } else if ("B".equalsIgnoreCase(newStatus)) {
            return;
        } else {
            System.out.println("Invalid input. No changes made.");
        }
        showAllPrescriptions(); // Refresh and display updated prescriptions
    }

    private String getNewStatus() {
        System.out.print("Enter 'D' for Dispensed or 'B' to go back: ");
        return scanner.nextLine().trim().toUpperCase();
    }

    private void printPrescriptions(List<String[]> data, boolean skipHeader) {
        System.out.println("+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
        System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s |\n", 
                          "Prescription ID", "Medicine Name", "Dosage", "Status", "Patient Name", "Requested By", "Date of Request", "Date of Approval");
        System.out.println("+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
        int startIdx = skipHeader ? 1 : 0;
        for (int i = startIdx; i < data.size(); i++) {
            String[] row = data.get(i);
            String status = row[3].trim().toUpperCase();
            String coloredStatus = PrescriptionStatus.valueOf(status).showStatusByColor();
            System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s |\n",
                              row[0], row[1], row[2], coloredStatus, row[4], row[5], row[6], row[7]);
        }
        System.out.println("+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
    }

    public void closeScanner() {
        scanner.close();
    }

    public static void main(String[] args) {
        PrescriptionManager manager = new PrescriptionManager();
        manager.runPrescriptionUpdateProcess();
        manager.closeScanner();
    }
}