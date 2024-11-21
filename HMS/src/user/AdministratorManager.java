package HMS.src.user;

import HMS.src.appointment.Appointment;
import HMS.src.appointment.AppointmentStatus;
import HMS.src.authorisation.PasswordManager;
import HMS.src.io.AppointmentCsvHelper;
import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.PasswordCsvHelper;
import HMS.src.io.ReplReqCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.medication.Medication;
import HMS.src.medication.MedicationManager;
import HMS.src.medication.ReplenishmentRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The AdministratorManager class handles all administrative operations in the Hospital Management System.
 * This includes managing staff, medication inventory, appointments, and replenishment requests.
 */
public class AdministratorManager {
    /**
     * Helper class for managing staff data in CSV format
     */
    private StaffCsvHelper staffHelper = new StaffCsvHelper();
    
    /**
     * Helper class for managing medication data in CSV format
     */
    private MedicationCsvHelper medHelper = new MedicationCsvHelper();
    
    /**
     * Helper class for managing replenishment requests in CSV format
     */
    private ReplReqCsvHelper replReqHelper = new ReplReqCsvHelper();
    
    /**
     * Helper class for managing appointment data in CSV format
     */
    private AppointmentCsvHelper apptHelper = new AppointmentCsvHelper();
    
    /**
     * Manager class for medication-related operations
     */
    private MedicationManager medicationManager = new MedicationManager();
    
    /**
     * Helper class for managing password data in CSV format
     */
    private PasswordCsvHelper passwordHelper = new PasswordCsvHelper();

    /**
     * ANSI color code for red text
     */
    private static final String ANSI_RED = "\u001B[31m";
    
    /**
     * ANSI color code for green text
     */
    private static final String ANSI_GREEN = "\u001B[32m";
    
    /**
     * ANSI color code for yellow text
     */
    private static final String ANSI_YELLOW = "\u001B[33m";
    
    /**
     * ANSI color code to reset text color
     */
    private static final String ANSI_RESET = "\u001B[0m";

    /**
     * Adds a new staff member to the system.
     * Automatically generates a unique staff ID based on their role (D for Doctor, P for Pharmacist).
     * 
     * @param staff The staff member to add to the system
     */
    public void addStaff(User staff) {
        List<String[]> currentStaff = staffHelper.readCSV();
        
        String prefix = switch (staff.getRole()) {
            case DOCTOR -> "D";
            case PHARMACIST -> "P";
            default -> "S";
        };
        
        int highestId = 0;
        for (String[] existingStaff : currentStaff) {
            if (existingStaff[0].startsWith(prefix)) {
                try {
                    int currentId = Integer.parseInt(existingStaff[0].substring(1));
                    if (currentId > highestId) {
                        highestId = currentId;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        
        String staffId = prefix + String.format("%03d", highestId + 1);
        staff.setUserID(staffId);

        String role = staff.getRole().toString();
        role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
        
        String gender = staff.getGender().toString();
        gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();

        String[] newStaffEntry = {
            staffId,
            staff.getName(),
            role,
            gender,
            String.valueOf(staff.getAge()),
            staff.getEmailId()
        };
        currentStaff.add(newStaffEntry);
        staffHelper.updateMedications(currentStaff);

        String defaultPassword = "password";
        String hashedPassword = new PasswordManager().hashPassword(defaultPassword);
        String[] passwordEntry = {staffId, hashedPassword};
        passwordHelper.addEntry(passwordEntry);

        System.out.println("\nStaff member added: " + staff.getName() + " (ID: " + staffId + ")");
        System.out.println("Default password set to: " + defaultPassword);
        System.out.println("Please ask the staff member to change their password upon first login.");
    }

    /**
     * Removes a staff member from the system.
     * Also removes their corresponding password entry from the password list.
     * 
     * @param staffID The ID of the staff member to remove
     */
    public void removeStaff(String staffID) {
        List<String[]> currentStaff = staffHelper.readCSV();
        List<String[]> currentPasswords = passwordHelper.readCSV();
        boolean removed = false;
        boolean found = false;
        
        List<String[]> updatedStaff = new ArrayList<>();
        List<String[]> updatedPasswords = new ArrayList<>();
        
        // First check if staff exists and is not an administrator
        for (String[] staff : currentStaff) {
            if (staff[0].equals(staffID)) {
                found = true;
                if (staff[2].equalsIgnoreCase("Administrator")) {
                    System.out.println("Error: Administrators cannot be removed from the system.");
                    return;
                }
            }
        }

        if (!found) {
            System.out.println("Staff member not found with ID: " + staffID);
            return;
        }

        // Remove from staff list
        for (String[] staff : currentStaff) {
            if (!staff[0].equalsIgnoreCase(staffID)) {
                updatedStaff.add(staff);
            } else {
                removed = true;
            }
        }

        // Remove from password list
        for (String[] password : currentPasswords) {
            if (!password[0].equalsIgnoreCase(staffID)) {
                updatedPasswords.add(password);
            }
        }

        if (removed) {
            staffHelper.updateMedications(updatedStaff);
            passwordHelper.updatePasswords(updatedPasswords);
            System.out.println("Staff member removed with ID: " + staffID);
        }
    }

    /**
     * Displays a list of all staff members in the system.
     */
    public void viewStaff() {
        List<String[]> staff = staffHelper.readCSV();
        if (staff.isEmpty()) {
            System.out.println("No staff members found.");
            return;
        }

        List<String[]> staffList = new ArrayList<>();
        for (int i = 1; i < staff.size(); i++) {
            String[] staffMember = staff.get(i);
            String role = staffMember[2].toUpperCase();
            if (role.equals("DOCTOR") || role.equals("PHARMACIST")) {
                staffList.add(staffMember);
            }
        }

        staffList.sort((a, b) -> {
            int roleCompare = a[2].compareToIgnoreCase(b[2]);
            if (roleCompare != 0) return roleCompare;

            int nameCompare = a[1].compareToIgnoreCase(b[1]);
            if (nameCompare != 0) return nameCompare;

            int genderCompare = a[3].compareToIgnoreCase(b[3]);
            if (genderCompare != 0) return genderCompare;

            try {
                return Integer.compare(Integer.parseInt(a[4]), Integer.parseInt(b[4]));
            } catch (NumberFormatException e) {
                return 0;
            }
        });
        
        System.out.println("\nCurrent Staff List (Doctors and Pharmacists):");
        String line = "+------------+----------------------+---------------+------------+-------+----------------------+";
        System.out.println(line);
        System.out.format("| %-10s | %-20s | %-13s | %-10s | %-5s | %-20s |%n",
            "Staff ID", "Name", "Role", "Gender", "Age", "Email");
        System.out.println(line);

        for (String[] staffMember : staffList) {
            String coloredRole = colorCodeRole(staffMember[2]);
            
            System.out.format("| %-10s | %-20s | %s | %-10s | %-5s | %-20s |%n",
                staffMember[0], staffMember[1], coloredRole, staffMember[3], staffMember[4], staffMember[5]);
        }
        System.out.println(line);
    }

    /**
     * Returns a colored representation of a staff member's role.
     * 
     * @param role The role to color
     * @return The colored role
     */
    private String colorCodeRole(String role) {
        String coloredRole;
        switch (role.toUpperCase()) {
            case "DOCTOR":
                coloredRole = ANSI_GREEN + String.format("%-13s", role) + ANSI_RESET;
                break;
            case "PHARMACIST":
                coloredRole = ANSI_RED + String.format("%-13s", role) + ANSI_RESET;
                break;
            default:
                coloredRole = String.format("%-13s", role);
        }
        return coloredRole;
    }

    /**
     * Adds a new medication to the inventory.
     * 
     * @param medication The medication to add
     */
    public void addMedication(Medication medication) {
        List<String[]> currentMeds = medHelper.readCSV();
        
        for (String[] med : currentMeds) {
            if (med[1].equalsIgnoreCase(medication.getName())) {
                System.out.println("Medication already exists. Use update stock function to modify stock levels.");
                return;
            }
        }

        int highestId = 0;
        for (String[] med : currentMeds) {
            if (med[0].startsWith("M")) {
                try {
                    int currentId = Integer.parseInt(med[0].substring(1));
                    if (currentId > highestId) {
                        highestId = currentId;
                    }
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        String medID = "M" + String.format("%03d", highestId + 1);
        medication.setMedicationID(medID);

        String dosageForm = medication.getDosageForm().toString();
        dosageForm = dosageForm.substring(0, 1).toUpperCase() + dosageForm.substring(1).toLowerCase();

        String[] newMedEntry = {
            medID,
            medication.getName(),
            String.valueOf(medication.getInventoryStock()),
            String.valueOf(medication.getLowThreshold()),
            String.valueOf(medication.getInventoryStock()),
            dosageForm
        };
        currentMeds.add(newMedEntry);
        medHelper.updateMedications(currentMeds);
        System.out.println("Medication added: " + medication.getName() + " (ID: " + medID + ")");
    }

    /**
     * Updates the stock level of a medication in the inventory.
     * 
     * @param medicineID The ID of the medication to update
     * @param newStock The new stock level
     */
    public void updateMedicationStock(String medicineID, int newStock) {
        String medicineName = medicationManager.getMedicineNameByID(medicineID);
        if (medicineName != null) {
            medicationManager.updateMedicationStock(medicineID, newStock);
            
            System.out.println("Updated stock for " + medicineName + " (ID: " + medicineID + ") to " + newStock);
        } else {
            System.out.println("Medicine ID not found: " + medicineID + ". Please enter a valid ID.");
        }
    }

    /**
     * Updates the low stock alert threshold for a medication in the inventory.
     * 
     * @param medicineID The ID of the medication to update
     * @param newThreshold The new low stock alert threshold
     */
    public void updateLowStockAlert(String medicineID, int newThreshold) {
        String medicineName = medicationManager.getMedicineNameByID(medicineID);
        if (medicineName != null) {
            medicationManager.updateLowStockAlert(medicineID, newThreshold);
            
            System.out.println("Updated low stock alert for " + medicineName + " (ID: " + medicineID + ") to " + newThreshold);
        } else {
            System.out.println("Medicine ID not found: " + medicineID + ". Please enter a valid ID.");
        }
    }

    /**
     * Displays the current medication inventory.
     */
    public void viewMedicationInventory() {
        List<String[]> meds = medHelper.readCSV();
        if (meds.isEmpty()) {
            System.out.println("No medications found.");
            return;
        }

        System.out.println("\nMedication Inventory:");
        String line = "+--------------+----------------------+---------------+------------------+-------------+--------------+------------+";
        System.out.println(line);
        System.out.format("| %-12s | %-20s | %-13s | %-16s | %-11s | %-12s | %-10s |%n", 
            "Medicine ID", "Medicine Name", "Initial Stock", "Low Stock Alert", "Stock Left", "Stock Status", "Type");
        System.out.println(line);

        for (int i = 1; i < meds.size(); i++) {
            String[] med = meds.get(i);
            if (med.length >= 5) {
                int initialStock = Integer.parseInt(med[2]);
                int lowStockAlert = Integer.parseInt(med[3]);
                int stockLeft = Integer.parseInt(med[4]);
                String status = determineStockStatus(initialStock, lowStockAlert, stockLeft);
                
                String plainStatus = status.replaceAll("\u001B\\[[;\\d]*m", "");
                int padding = 12 - plainStatus.length();
                String paddedStatus = status + " ".repeat(padding);
                
                System.out.format("| %-12s | %-20s | %-13s | %-16s | %-11s | %-12s | %-10s |%n",
                    med[0], med[1], med[2], med[3], med[4], paddedStatus, med[5]);
            }
        }
        System.out.println(line);
    }

    /**
     * Determines the stock status of a medication based on its initial stock, low stock alert, and current stock level.
     * 
     * @param initialStock The initial stock level of the medication
     * @param lowStockAlert The low stock alert threshold of the medication
     * @param stockLeft The current stock level of the medication
     * @return The stock status of the medication
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
     * Approves or rejects a replenishment request.
     * 
     * @param requestID The ID of the request to approve or reject
     * @param approve Whether to approve or reject the request
     * @param admin The administrator approving or rejecting the request
     */
    public void approveReplenishmentRequest(String requestID, boolean approve, Administrator admin) {
        List<String[]> requests = replReqHelper.readCSV();
        boolean found = false;
        String[] targetRequest = null;
        
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
        String medicineName = targetRequest[1];
        String medicineID = medicationManager.getMedicineIDByName(medicineName);
        
        if (medicineID == null) {
            System.out.println("Error: Medicine not found in inventory: " + medicineName);
            return;
        }

        if (approve) {
            int currentStock = medicationManager.getCurrentStock(medicineID);
            int requestAmount = Integer.parseInt(targetRequest[2]);
            updateMedicationStock(medicineID, currentStock + requestAmount);
        }

        replReqHelper.updateRequest(requestID, status, admin.getName(), LocalDate.now().toString());
        System.out.println("Request " + requestID + " has been " + status.toLowerCase() + ".");
    }

    /**
     * Retrieves a list of all pending replenishment requests.
     * 
     * @return The list of pending replenishment requests
     */
    public List<String[]> getPendingReplenishmentRequests() {
        List<String[]> allRequests = replReqHelper.readCSV();
        List<String[]> pendingRequests = new ArrayList<>();
        
        for (int i = 1; i < allRequests.size(); i++) {
            String[] request = allRequests.get(i);
            if (request[4].equals("PENDING")) {
                pendingRequests.add(request);
            }
        }
        
        return pendingRequests;
    }

    /**
     * Displays a list of all appointments in the system.
     */
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

        for (int i = 1; i < appointments.size(); i++) {
            String[] appt = appointments.get(i);
            String status = padColoredString(AppointmentStatus.valueOf(appt[5].toUpperCase()).showStatusByColor(), 10);
            
            System.out.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %s | %-10s |%n",
                appt[0], appt[1], appt[2], appt[3], appt[4], status,
                (appt.length > 6 && appt[6] != null) ? appt[6] : "");
        }
        System.out.println(line);
    }

    /**
     * Pads a colored string to a specific width, accounting for ANSI color codes.
     * 
     * @param str The colored string containing ANSI codes
     * @param width The desired width of the visible text
     * @return The padded string with proper spacing
     */
    private String padColoredString(String str, int width) {
        String plainText = str.replaceAll("\u001B\\[[;\\d]*m", "");
        int padding = width - plainText.length();
        int lastResetIndex = str.lastIndexOf("\u001B[0m");
        
        if (lastResetIndex >= 0) {
            return str.substring(0, lastResetIndex) + " ".repeat(padding) + str.substring(lastResetIndex);
        } else {
            return String.format("%-" + width + "s", str);
        }
    }

    /**
     * Displays a list of scheduled appointments.
     * 
     * @param appointments The list of appointments to display
     */
    public void viewScheduledAppointments(List<Appointment> appointments) {
        viewAppointments();
    }

    /**
     * Removes a medication from the inventory.
     * 
     * @param medicineID The ID of the medication to remove
     */
    public void removeMedication(String medicineID) {
        List<String[]> currentMeds = medHelper.readCSV();
        boolean removed = false;
        boolean found = false;
        
        List<String[]> updatedMeds = new ArrayList<>();
        for (String[] med : currentMeds) {
            if (!med[0].equals(medicineID)) {
                updatedMeds.add(med);
            } else {
                found = true;
                removed = true;
                System.out.println("Medication '" + med[1] + "' (ID: " + medicineID + ") has been removed from the inventory.");
            }
        }

        if (removed) {
            medHelper.updateMedications(updatedMeds);
        } else if (!found) {
            System.out.println("Medication not found with ID: " + medicineID);
        }
    }
}