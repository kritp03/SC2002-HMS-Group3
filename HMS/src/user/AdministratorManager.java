package HMS.src.user;

import HMS.src.appointment.Appointment;
import HMS.src.io.ApptCsvHelper;
import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.ReplReqCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AdministratorManager {
    private StaffCsvHelper staffHelper = new StaffCsvHelper();
    private MedicationCsvHelper medHelper = new MedicationCsvHelper();
    private ReplReqCsvHelper replReqHelper = new ReplReqCsvHelper();
    private ApptCsvHelper apptHelper = new ApptCsvHelper();

    // ANSI color codes for console output
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RESET = "\u001B[0m";

    // Staff Management Methods
    public void addStaff(User staff) {
        List<String[]> currentStaff = staffHelper.readCSV();
        
        // Generate staff ID based on role (D001 for Doctor, P001 for Pharmacist, A001 for Administrator)
        String prefix = switch (staff.getRole()) {
            case DOCTOR -> "D";
            case PHARMACIST -> "P";
            case ADMINISTRATOR -> "A";
            default -> "S"; // Default prefix for other roles
        };
        
        // Find the highest ID for this role
        int highestId = 0;
        for (String[] existingStaff : currentStaff) {
            if (existingStaff[0].startsWith(prefix)) {
                try {
                    int currentId = Integer.parseInt(existingStaff[0].substring(1));
                    if (currentId > highestId) {
                        highestId = currentId;
                    }
                } catch (NumberFormatException e) {
                    // Skip if ID is not in the expected format
                    continue;
                }
            }
        }
        
        // Generate new ID
        String staffId = prefix + String.format("%03d", highestId + 1);
        staff.setUserID(staffId);

        String[] newStaffEntry = {
            staffId,
            staff.getName(),
            staff.getRole().toString(),
            staff.getGender().toString(),
            String.valueOf(staff.getAge())
        };
        currentStaff.add(newStaffEntry);
        staffHelper.updateMedications(currentStaff);
        System.out.println("Staff member added: " + staff.getName() + " (ID: " + staffId + ")");
    }

    public void removeStaff(String staffID) {
        List<String[]> currentStaff = staffHelper.readCSV();
        boolean removed = false;
        
        List<String[]> updatedStaff = new ArrayList<>();
        for (String[] staff : currentStaff) {
            if (!staff[0].equals(staffID)) {
                updatedStaff.add(staff);
            } else {
                removed = true;
            }
        }

        if (removed) {
            staffHelper.updateMedications(updatedStaff);
            System.out.println("Staff member removed with ID: " + staffID);
        } else {
            System.out.println("Staff member not found with ID: " + staffID);
        }
    }

    public void viewStaff() {
        List<String[]> staff = staffHelper.readCSV();
        if (staff.isEmpty()) {
            System.out.println("No staff members found.");
            return;
        }
        
        System.out.println("\nCurrent Staff List:");
        String line = "+------------+----------------------+---------------+------------+-------+";
        System.out.println(line);
        System.out.format("| %-10s | %-20s | %-13s | %-10s | %-5s |%n",
            "Staff ID", "Name", "Role", "Gender", "Age");
        System.out.println(line);

        // Skip header row
        for (int i = 1; i < staff.size(); i++) {
            String[] staffMember = staff.get(i);
            // Color code different roles
            String role = colorCodeRole(staffMember[2]);
            
            System.out.format("| %-10s | %-20s | %s | %-10s | %-5s |%n",
                staffMember[0], staffMember[1], role, staffMember[3], staffMember[4]);
        }
        System.out.println(line);
    }

    private String colorCodeRole(String role) {
        String coloredRole;
        switch (role.toUpperCase()) {
            case "DOCTOR":
                coloredRole = ANSI_GREEN + String.format("%-13s", role) + ANSI_RESET;
                break;
            case "ADMINISTRATOR":
                coloredRole = ANSI_YELLOW + String.format("%-13s", role) + ANSI_RESET;
                break;
            case "PHARMACIST":
                coloredRole = ANSI_RED + String.format("%-13s", role) + ANSI_RESET;
                break;
            default:
                coloredRole = String.format("%-13s", role);
        }
        return coloredRole;
    }

    // Medication Management Methods
    public void addMedication(Medication medication) {
        List<String[]> currentMeds = medHelper.readCSV();
        
        // Check if medication already exists
        for (String[] med : currentMeds) {
            if (med[0].equalsIgnoreCase(medication.getName())) {
                System.out.println("Medication already exists. Use update stock function to modify stock levels.");
                return;
            }
        }

        // Generate next medication ID (M001, M002, etc.)
        String medID = "M" + String.format("%03d", currentMeds.size());
        medication.setMedicationID(medID);

        String[] newMedEntry = {
            medID,
            medication.getName(),
            String.valueOf(medication.getInventoryStock()),
            String.valueOf(medication.getLowThreshold()),
            String.valueOf(medication.getInventoryStock()),
            medication.getDosageForm().toString()
        };
        currentMeds.add(newMedEntry);
        medHelper.updateMedications(currentMeds);
        System.out.println("Medication added: " + medication.getName() + " (ID: " + medID + ")");
    }

    public void updateMedicationStock(String medicationName, int newStock) {
        List<String[]> meds = medHelper.readCSV();
        boolean updated = false;
        
        for (int i = 1; i < meds.size(); i++) {
            String[] med = meds.get(i);
            if (med[0].equalsIgnoreCase(medicationName)) {
                int initialStock = Integer.parseInt(med[1]);
                int lowStockAlert = Integer.parseInt(med[2]);
                med[3] = String.valueOf(newStock);
                updated = true;
                
                // Check if stock is below threshold and create replenishment request
                if (newStock <= lowStockAlert) {
                    createReplenishmentRequest(medicationName, initialStock - newStock);
                }
                break;
            }
        }

        if (updated) {
            medHelper.updateMedications(meds);
            System.out.println("Updated stock for " + medicationName + " to " + newStock);
        } else {
            System.out.println("Medication not found: " + medicationName);
        }
    }

    public void viewMedicationInventory() {
        List<String[]> meds = medHelper.readCSV();
        if (meds.isEmpty()) {
            System.out.println("No medications found.");
            return;
        }

        System.out.println("\nMedication Inventory:");
        String line = "+----------------------+---------------+------------------+-------------+--------------+------------+";
        System.out.println(line);
        System.out.format("| %-20s | %-13s | %-16s | %-11s | %-12s | %-10s |%n", 
            "Medicine Name", "Initial Stock", "Low Stock Alert", "Stock Left", "Stock Status", "Type");
        System.out.println(line);

        // Skip header row
        for (int i = 1; i < meds.size(); i++) {
            String[] med = meds.get(i);
            if (med.length >= 5) {
                int initialStock = Integer.parseInt(med[1]);
                int lowStockAlert = Integer.parseInt(med[2]);
                int stockLeft = Integer.parseInt(med[3]);
                String status = determineStockStatus(initialStock, lowStockAlert, stockLeft);
                
                // Remove ANSI codes for length calculation
                String plainStatus = status.replaceAll("\u001B\\[[;\\d]*m", "");
                // Calculate padding needed
                int padding = 12 - plainStatus.length(); // 12 is the width of the Stock Status column
                String paddedStatus = status + " ".repeat(padding);
                
                System.out.format("| %-20s | %-13s | %-16s | %-11s | %-12s | %-10s |%n",
                    med[0], med[1], med[2], med[3], paddedStatus, med[4]);
            }
        }
        System.out.println(line);
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

    private void createReplenishmentRequest(String medicineName, int amount) {
        List<String[]> requests = replReqHelper.readCSV();
        
        // Generate new request ID
        String requestID = "R" + String.format("%03d", requests.size());
        
        String[] newRequest = {
            requestID,
            medicineName,
            String.valueOf(amount),
            LocalDate.now().toString(),
            "PENDING",
            "",
            ""
        };
        
        replReqHelper.addReplReq(newRequest);
        System.out.println("Created replenishment request for " + medicineName + " (Amount: " + amount + ")");
    }

    // Replenishment Request Methods
    public void approveReplenishmentRequest(ReplenishmentRequest request, boolean approve) {
        List<String[]> requests = replReqHelper.readCSV();
        boolean found = false;
        
        for (String[] req : requests) {
            if (req[0].equals(request.getRequestID())) {
                if (approve) {
                    req[4] = "APPROVED";
                    req[6] = LocalDate.now().toString();
                    // Update medication stock
                    updateMedicationStock(req[1], 
                        getCurrentStock(req[1]) + Integer.parseInt(req[2]));
                } else {
                    req[4] = "REJECTED";
                    req[6] = LocalDate.now().toString();
                }
                found = true;
                replReqHelper.addReplReq(req); // Update the specific request
                break;
            }
        }

        if (found) {
            System.out.println("Request " + (approve ? "approved" : "rejected") + ": " + request.getRequestID());
        } else {
            System.out.println("Request not found: " + request.getRequestID());
        }
    }

    private int getCurrentStock(String medicineName) {
        List<String[]> meds = medHelper.readCSV();
        for (int i = 1; i < meds.size(); i++) {
            if (meds.get(i)[0].equalsIgnoreCase(medicineName)) {
                return Integer.parseInt(meds.get(i)[3]);
            }
        }
        return 0;
    }

    // Get pending replenishment requests
    public List<String[]> getPendingReplenishmentRequests() {
        List<String[]> allRequests = replReqHelper.readCSV();
        List<String[]> pendingRequests = new ArrayList<>();
        
        // Skip header row
        for (int i = 1; i < allRequests.size(); i++) {
            String[] request = allRequests.get(i);
            if (request[4].equals("PENDING")) {
                pendingRequests.add(request);
            }
        }
        
        return pendingRequests;
    }

    // Process a replenishment request
    public void processReplenishmentRequest(String requestID, boolean approve, Administrator admin) {
        List<String[]> requests = replReqHelper.readCSV();
        boolean found = false;
        String[] targetRequest = null;
        
        // Find the request
        for (String[] request : requests) {
            if (request[0].equals(requestID)) {
                targetRequest = request;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Request not found: " + requestID);
            return;
        }

        String status = approve ? "APPROVED" : "REJECTED";
        
        // Update request status and update medication stock if approved
        if (approve) {
            updateMedicationStock(targetRequest[1], 
                getCurrentStock(targetRequest[1]) + Integer.parseInt(targetRequest[2]));
        }

        // Update the request in CSV
        replReqHelper.updateRequest(requestID, status, admin.getName(), LocalDate.now().toString());
        System.out.println("Request " + requestID + " has been " + status.toLowerCase() + ".");
    }

    // Appointment Management Methods
    public void viewAppointments() {
        List<String[]> appointments = apptHelper.readCSV();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.println("\nAll Appointments:");
        String line = "+------------+------------+------------+------------+------------+------------+------------+";
        System.out.println(line);
        System.out.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %-10s | %-10s |%n",
            "Appt ID", "Patient ID", "Doctor ID", "Date", "Time", "Status", "Outcome");
        System.out.println(line);

        // Skip header row
        for (int i = 1; i < appointments.size(); i++) {
            String[] appt = appointments.get(i);
            // Color code the status
            String status = colorCodeStatus(appt[5]);
            
            System.out.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %s | %-10s |%n",
                appt[0], appt[1], appt[2], appt[3], appt[4], status,
                (appt.length > 6 && appt[6] != null) ? appt[6] : "");
        }
        System.out.println(line);
    }

    private String colorCodeStatus(String status) {
        String coloredStatus;
        switch (status.toUpperCase()) {
            case "PENDING":
                coloredStatus = ANSI_YELLOW + String.format("%-10s", status) + ANSI_RESET;
                break;
            case "COMPLETED":
                coloredStatus = ANSI_GREEN + String.format("%-10s", status) + ANSI_RESET;
                break;
            case "CANCELLED":
                coloredStatus = ANSI_RED + String.format("%-10s", status) + ANSI_RESET;
                break;
            default:
                coloredStatus = String.format("%-10s", status);
        }
        return coloredStatus;
    }

    public void viewScheduledAppointments(List<Appointment> appointments) {
        viewAppointments(); // For now, show all appointments
    }
}