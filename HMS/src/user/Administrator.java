package HMS.src.user;

import HMS.src.appointment.Appointment;
import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import java.util.List;

/**
 * Represents an Administrator in the Hospital Management System.
 * The Administrator is responsible for managing staff, appointments, and medication inventory,
 * as well as approving or rejecting replenishment requests.
 */
public class Administrator extends User {

    /**
     * Instance of the AdministratorManager to handle administrative operations.
     */
    private AdministratorManager manager = new AdministratorManager();

    /**
     * Constructor for creating an Administrator object.
     *
     * @param userID  Unique ID of the Administrator.
     * @param name    Name of the Administrator.
     * @param emailId Email ID of the Administrator.
     * @param age     Age of the Administrator.
     * @param gender  Gender of the Administrator.
     */
    public Administrator(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.ADMINISTRATOR, emailId, age, gender);
    }

    /**
     * Adds a staff member to the system.
     *
     * @param staff The staff member to be added.
     */
    public void addStaff(User staff) {
        manager.addStaff(staff);
    }

    /**
     * Removes a staff member from the system by their ID.
     *
     * @param staffID The ID of the staff member to be removed.
     */
    public void removeStaff(String staffID) {
        manager.removeStaff(staffID);
    }

    /**
     * Views all staff members in the system.
     */
    public void viewStaff() {
        manager.viewStaff();
    }

    /**
     * Views all appointments in the system.
     */
    public void viewAppointments() {
        manager.viewAppointments();
    }

    /**
     * Adds a new medication to the inventory.
     *
     * @param medication The medication to be added.
     */
    public void addMedication(Medication medication) {
        manager.addMedication(medication);
    }

    
    /**
     * Removes a medication from the inventory.
     *
     * @param medicineID The ID of the medication to be removed.
     */
    public void removeMedication(String medicineID) {
        manager.removeMedication(medicineID);
    }


    /**
     * Updates the stock level of an existing medication.
     *
     * @param medicationName The name of the medication.
     * @param newStock       The new stock level.
     */
    public void updateMedicationStock(String medicineID, int newStock) {
        manager.updateMedicationStock(medicineID, newStock);
    }

    
    /**
     * Updates the low stock alert threshold of a medication.
     *
     * @param medicineID The ID of the medication.
     * @param newLowStock The new low stock alert threshold.
     */
    public void updateLowStockAlert(String medicineID, int newLowStock) {
        manager.updateLowStockAlert(medicineID, newLowStock);
    }


    /**
     * Views the current medication inventory.
     */
    public void viewMedicationInventory() {
        manager.viewMedicationInventory();
    }

    /**
     * Views all scheduled appointments.
     *
     * @param appointments The list of scheduled appointments to be viewed.
     */
    public void viewScheduledAppointments(List<Appointment> appointments) {
        manager.viewScheduledAppointments(appointments);
    }

    /**
     * Retrieves all pending replenishment requests.
     *
     * @return A list of pending replenishment requests.
     */
    public List<String[]> getPendingReplenishmentRequests() {
        return manager.getPendingReplenishmentRequests();
    }

    /**
     * Approves a specific replenishment request by approving or rejecting it.
     *
     * @param requestID The ID of the replenishment request.
     * @param approve true to approve, false to reject.
     */
    public void approveReplenishmentRequest(String requestID, boolean approve, String adminID) {
        manager.approveReplenishmentRequest(requestID, approve, adminID);
    }

    /**
     * Returns a string representation of the Administrator.
     *
     * @return A string containing the Administrator's details.
     */
    @Override
    public String toString() {
        return String.format("Administrator[ID=%s, Name=%s, Email=%s, Age=%d, Gender=%s]",
            getUserID(), getName(), getEmailId(), getAge(), getGender());
    }
}
