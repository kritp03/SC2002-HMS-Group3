package HMS.src.medication;

import HMS.src.io_new.MedicationCsvHelper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

public class MedicationManager {

    private Scanner scanner = new Scanner(System.in);
    private MedicationCsvHelper medCsvHelper = new MedicationCsvHelper();

    public Set<String> getAllMedicineNames(String filePath) {
        List<String[]> data = medCsvHelper.readCSV();
        Set<String> medicineNames = new HashSet<>();
        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).length > 0) {
                medicineNames.add(data.get(i)[0].toLowerCase());
            }
        }
        return medicineNames;
    }

    public void viewMedicationInventory(String filePath) {
        List<String[]> data = medCsvHelper.readCSV();
        if (data.isEmpty()) {
            System.out.println("No data found in CSV file.");
            return;
        }
        System.out.println("+--------------------+--------------------+----------------------+");
        System.out.format("| %-18s | %-18s | %-20s|\n", "Medicine Name", "Initial Stock", "Low Stock Level Alert");
        System.out.println("+--------------------+--------------------+----------------------+");
        for (int i = 1; i < data.size(); i++) { 
            String[] row = data.get(i);
            if (row.length >= 3) { 
                System.out.format("| %-18s | %-18s | %-20s |\n", row[0], row[1], row[2]);
            }
        }
        System.out.println("+--------------------+--------------------+----------------------+\n");
    }

    private String promptForMedicineName(Set<String> allMedicines) {
        while (true) {
            System.out.println("\nEnter the name of the medicine you wish to replenish:");
            String medicineName = scanner.nextLine().toLowerCase();
            if (allMedicines.contains(medicineName)) {
                return medicineName;
            } else {
                System.out.println("Medicine not found in inventory. Please check the name and try again.");
            }
        }
    }

    private int promptForReplenishmentAmount() {
        while (true) {
            System.out.println("\nEnter the amount to replenish:");
            String input = scanner.nextLine();
            if (input.matches("^[1-9]\\d*$")) {
                return Integer.parseInt(input);
            } else {
                System.out.println("\nInvalid input. Only positive values are allowed.");
            }
        }
    }

    private void confirmSubmitRequest(String medicineName, int amount) {
        String requestID = UUID.randomUUID().toString();
        String medicationID = medicineName;
        ReplenishmentRequest request = new ReplenishmentRequest(requestID, medicationID, amount, LocalDate.now());
        System.out.println("\nHere is the replenishment request summary:");
        displayReplReqSummary(request);

        System.out.println("\nAre you sure you want to replenish this medicine? (y/n)");
        String confirmation = scanner.nextLine().toLowerCase();

        if ("y".equals(confirmation)) {
            System.out.println("Request submitted!");
        } else {
            System.out.println("Request cancelled.");
        }
    }

    private void displayReplReqSummary(ReplenishmentRequest request) {
        System.out.println("=================================");
        System.out.println("| Replenishment Request Summary |");
        System.out.println("=================================");
        System.out.println("Request ID: " + request.getRequestID());
        System.out.println("Medication ID: " + request.getMedicationID());
        System.out.println("Quantity: " + request.getQuantity());
        System.out.println("Request Date: " + request.getDate());
        System.out.println("Status: " + request.getStatus().showStatusByColor());
        System.out.println("Approved By: "
                + (request.getApprovedBy().equals("someone") ? "Not yet approved" : request.getApprovedBy()));
        System.out.println("=================================");
    }

    public void submitReplenishmentRequest(String filePath) {
        Set<String> allMedicines = getAllMedicineNames(filePath);

        String medicineName = promptForMedicineName(allMedicines);
        int replenishAmt = promptForReplenishmentAmount();
        confirmSubmitRequest(medicineName, replenishAmt);
    }

    // public static void main(String[] args) {
    //     MedicationManager manager = new MedicationManager();
    //     String filePath = "/Users/weipingtee/Library/CloudStorage/OneDrive-NanyangTechnologicalUniversity/Year 2/Sem 1/SC2002 Object Oriented Programming/Assignment/SC2002-HMS-Group3/HMS/data/Medicine_List.csv"; 
    //     manager.viewMedicationInventory(filePath);
    // }
}