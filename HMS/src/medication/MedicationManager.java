package HMS.src.medication;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.ReplReqCsvHelper;

/**
 * Manages the replenishment of medication in the inventory.
 */
public class MedicationManager {

    /**
     * ANSI color codes for console output.
     */
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_YELLOW = "\u001B[33m";
    private final String ANSI_GREEN = "\u001B[32m";

    public Scanner scanner = new Scanner(System.in);
    private MedicationCsvHelper medCsvHelper = new MedicationCsvHelper();
    ReplReqCsvHelper replReqCsvHelper = new ReplReqCsvHelper();

    /**
     * Fetches all medicine names from the CSV file.
     * @return A set of all medicine names in the inventory.
     */
    public Set<String> getAllMedicineNames() {
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

    /**
     * Displays the current medication inventory.
     */
    public void viewMedicationInventory() {
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

    /**
     * Determines the stock status of a medicine based on the initial stock, low stock alert threshold, and current stock left.
     * @param initialStock The initial stock of the medicine.
     * @param lowStockAlert The low stock alert threshold.
     * @param stockLeft The current stock left.
     * @return A string representing the stock status.
     */
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

    /**
     * Prompts the user to enter the name of the medicine to replenish.
     * @param allMedicines A set of all medicine names in the inventory.
     * @return The name of the medicine to replenish.
     */
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

    /**
     * Prompts the user to enter the amount to replenish.
     * @return The amount to replenish.
     */
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

    /**
     * Confirms the submission of a replenishment request.
     * @param medicineName The name of the medicine to replenish.
     * @param amount The amount to replenish.
     */
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

    /**
     * Displays a summary of the replenishment request.
     * @param request The replenishment request to display.
     */
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

    /**
     * Submits a replenishment request for a medicine.
     */
    public void submitReplenishmentRequest() {
        Set<String> allMedicines = getAllMedicineNames();
        String medicineName = promptForMedicineName(allMedicines);
        int replenishAmt = promptForReplenishmentAmount();
        confirmSubmitRequest(medicineName, replenishAmt);
    }

}