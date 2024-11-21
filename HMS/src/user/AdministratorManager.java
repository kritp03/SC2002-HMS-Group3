package HMS.src.user;

import HMS.src.appointment.Appointment;
import HMS.src.appointment.AppointmentStatus;
import HMS.src.authorisation.PasswordManager;
import HMS.src.authorisation.IPasswordManager;
import HMS.src.io.AppointmentCsvHelper;
import HMS.src.io.MedicationCsvHelper;
import HMS.src.io.PasswordCsvHelper;
import HMS.src.io.ReplReqCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.medication.Medication;
import HMS.src.medication.MedicationManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The AdministratorManager class handles operations related to administrative tasks in the hospital management system.
 * These include adding and removing staff, viewing staff details, and managing credentials.
 * The class interacts with various CSV helper classes to perform data manipulation and storage.
 */
public class AdministratorManager {
    /**
     * Helper for managing staff information stored in the staff CSV.
     */
    private StaffCsvHelper staffHelper = new StaffCsvHelper();
    /**
     * Helper for managing medication information stored in the medication CSV.
     */
    private MedicationCsvHelper medHelper = new MedicationCsvHelper();
    /**
     * Helper for managing replenishment requests stored in the replenishment request CSV.
     */
    private ReplReqCsvHelper replReqHelper = new ReplReqCsvHelper();
    /**
     * Manager for handling medication-related operations.
     */
    private AppointmentCsvHelper apptHelper = new AppointmentCsvHelper();

    private MedicationManager medicationManager = new MedicationManager();
    /**
     * Helper for managing passwords stored in the password CSV.
     */
    private PasswordCsvHelper passwordHelper = new PasswordCsvHelper();
    /**
     * ANSI color codes for terminal text formatting.
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
     * Generates a unique staff ID based on the staff role and ensures the staff entry is added to the staff CSV.
     * A default password is also set for the new staff member.
     * 
     * @param staff The staff member to be added, represented as a {@link User} object.
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
        IPasswordManager passwordManager = new PasswordManager();
        String hashedPassword = passwordManager.hashPassword(defaultPassword);
        String[] passwordEntry = {staffId, hashedPassword};
        passwordHelper.addEntry(passwordEntry);

        System.out.println("\nStaff member added: " + staff.getName() + " (ID: " + staffId + ")");
        System.out.println("Default password set to: " + defaultPassword);
        System.out.println("Please ask the staff member to change their password upon first login.");
    }
    /**
     * Removes a staff member from the system based on their staff ID.
     * Administrators cannot be removed from the system.
     * 
     * @param staffID The ID of the staff member to be removed.
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
            if (staff[0].equalsIgnoreCase(staffID)) {
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
     * Displays a list of all doctors and pharmacists in the system.
     * The list is sorted by role, name, gender, and age, in that order.
     * The role is color-coded for better visibility.
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
     * Helper method to assign ANSI color codes to roles for terminal display.
     * 
     * @param role The role of the staff member (e.g., "Doctor", "Pharmacist").
     * @return A string representing the role, color-coded for terminal display.
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
     * Ensures the medication name is unique and assigns a new medication ID.
     *
     * @param medication The medication object containing details like name, stock, low threshold, and dosage form.
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
     * Updates the stock of a specific medication in the inventory.
     *
     * @param medicineID The ID of the medication to update.
     * @param newStock   The new stock level to be set for the medication.
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
     * Updates the low-stock alert threshold for a specific medication.
     *
     * @param medicineID    The ID of the medication to update.
     * @param newThreshold  The new threshold value to trigger low-stock alerts.
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
     * Displays the current medication inventory in a tabular format.
     * Includes details like medication ID, name, stock levels, low stock alerts, and status.
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
     * Determines the stock status (High, Moderate, Low) based on stock levels and thresholds.
     *
     * @param initialStock   The initial stock level of the medication.
     * @param lowStockAlert  The low-stock alert threshold for the medication.
     * @param stockLeft      The current stock level of the medication.
     * @return A string representing the stock status, color-coded for terminal display.
     */
    private String determineStockStatus(int initialStock, int lowStockAlert, int stockLeft) {
        // Define thresholds for clarity
        int highThreshold = (int) (initialStock * 0.8); 
        int moderateThreshold = lowStockAlert;         
    
        // Determine stock status based on thresholds
        if (stockLeft >= highThreshold) {
            return ANSI_GREEN + "High" + ANSI_RESET;       
        } else if (stockLeft >= moderateThreshold && stockLeft < highThreshold) {
            return ANSI_YELLOW + "Moderate" + ANSI_RESET;   
        } else {
            return ANSI_RED + "Low" + ANSI_RESET;          
        }
    }
    /**
     * Approves or rejects a replenishment request for a specific medication.
     * If approved, updates the stock level in the inventory.
     *
     * @param requestID  The ID of the replenishment request.
     * @param approve    Boolean flag indicating whether the request is approved or rejected.
     * @param admin      The administrator approving or rejecting the request.
     */
    public void approveReplenishmentRequest(String requestID, boolean approve, String adminID) {
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

        String adminName = getAdminNameByID(adminID);
        replReqHelper.updateRequest(requestID, status, adminName, LocalDate.now().toString());
        System.out.println("Request " + requestID + " has been " + status.toLowerCase() + ".");
    }
    /**
     * Retrieves a list of pending replenishment requests.
     *
     * @return A list of string arrays, each representing a pending replenishment request.
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
     * Displays all appointments in the system in a tabular format.
     * Includes details like appointment ID, patient ID, doctor ID, date, time, status, and outcome.
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

    /**
     * Gets the administrator's name by their ID.
     * 
     * @param adminID The ID of the administrator
     * @return The administrator's name if found, null otherwise
     */
    public String getAdminNameByID(String adminID) {
        List<String[]> staff = staffHelper.readCSV();
        for (String[] staffMember : staff) {
            if (staffMember[0].equalsIgnoreCase(adminID) && 
                staffMember[2].equalsIgnoreCase("Administrator")) {
                return staffMember[1];
            }
        }
        return null;
    }
}