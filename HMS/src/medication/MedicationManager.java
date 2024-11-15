package HMS.src.medication;

import HMS.src.io_new.MedicationCsvHelper;
import HMS.src.io_new.ReplReqCsvHelper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MedicationManager {

    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_GREEN = "\u001B[32m";

    public Scanner scanner = new Scanner(System.in);
    private MedicationCsvHelper medCsvHelper = new MedicationCsvHelper();
    ReplReqCsvHelper replReqCsvHelper = new ReplReqCsvHelper();

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

    /**
     * Fetches the highest request ID from the CSV and returns the next ID incremented by one.
     * @return The next request ID in the sequence.
     */
    private String getNextRequestId() {
        List<String[]> entries = replReqCsvHelper.readCSV();
        int highestId = 0;
        for (String[] entry : entries) {
            if (entry[0].startsWith("R")) {
                int currentId = Integer.parseInt(entry[0].substring(1)); // Assume the ID format is 'R001', 'R002', etc.
                if (currentId > highestId) {
                    highestId = currentId;
                }
            }
        }
        return "R" + String.format("%03d", highestId + 1); // Returns the next ID, formatted as three digits
    }

    public void viewMedicationInventory(String filePath) {
        List<String[]> data = medCsvHelper.readCSV();
        if (data.isEmpty()) {
            System.out.println("No data found in CSV file.");
            return;
        }
        System.out.println("+----------------------+---------------+-------------+--------------+");
        System.out.format("| %-20s | %-12s | %-11s | %-12s |\n", "Medicine Name", "Initial Stock", "Stock Left",
                "Stock Status");
        System.out.println("+----------------------+---------------+-------------+--------------+");
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length >= 4) {
                String stockStatus = determineStockStatus(Integer.parseInt(row[1]), Integer.parseInt(row[2]),
                        Integer.parseInt(row[3]));
                System.out.format("| %-20s | %-13s | %-11s | %-21s |\n", row[0], row[1], row[3], stockStatus);
            }
        }
        System.out.println("+----------------------+---------------+-------------+--------------+\n");
    }

    private String determineStockStatus(int initialStock, int lowStockAlert, int stockLeft) {
        int highThreshold = (int) (lowStockAlert + 0.2 * initialStock);
        if (stockLeft > highThreshold) {
            return ANSI_GREEN + "High" + ANSI_RESET;
        } else if (stockLeft > lowStockAlert && stockLeft <= highThreshold) {
            return ANSI_YELLOW + "Moderate" + ANSI_RESET;
        } else {
            return ANSI_RED + "Low" + ANSI_RESET;
        }
    }


    private String promptForMedicineName(Set<String> allMedicines) {
        while (true) {
            System.out.print("Enter the name of the medicine you wish to replenish: "); 
            String medicineName = scanner.nextLine().toLowerCase();
            if (allMedicines.contains(medicineName)) {
                return medicineName;
            } else {
                System.out.println("Medicine not found in inventory. Please check the name and try again."); // This still prints on a new line for clarity.
            }
        }
    }

    private int promptForReplenishmentAmount() {
        while (true) {
            System.out.print("\nEnter the amount to replenish: ");
            String input = scanner.nextLine();
            if (input.matches("^[1-9]\\d*$")) {
                return Integer.parseInt(input);
            } else {
                System.out.println("\nInvalid input. Only positive values are allowed.");
            }
        }
    }

    private void confirmSubmitRequest(String medicineName, int amount) {
        String requestID = getNextRequestId();
        String status = "PENDING"; // Default status for new requests
        String approvedBy = ""; // Default approver status

        displayReplReqSummary(new ReplenishmentRequest(requestID, medicineName, amount, LocalDate.now()));

        System.out.print("\nAre you sure you want to replenish this medicine? (y/n): ");
        String confirmation = scanner.nextLine().toLowerCase();

        if ("y".equals(confirmation)) {
            String[] replReq = new String[]{
                requestID,
                medicineName,
                Integer.toString(amount),
                LocalDate.now().toString(),
                status,
                approvedBy
            };

            replReqCsvHelper.addReplReq(replReq); 
            System.out.println("Request submitted and recorded!\n");
        } else {
            System.out.println("Request cancelled.\n");
        }
    }


    private void displayReplReqSummary(ReplenishmentRequest request) {
        System.out.println("=================================");
        System.out.println("| Replenishment Request Summary |");
        System.out.println("=================================");
        System.out.println("Request ID: " + request.getRequestID());
        System.out.println("Medication Name: " + request.getmedicineName());
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

}