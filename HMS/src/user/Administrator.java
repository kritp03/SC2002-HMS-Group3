package HMS.src.user;

import HMS.src.appointment.Appointment;
import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import java.util.List;

public class Administrator extends User {
    private AdministratorManager manager = new AdministratorManager();

    public Administrator(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.ADMINISTRATOR, emailId, age, gender);
    }

    // Manage Staff
    public void addStaff(User staff) {
        manager.addStaff(staff);
    }

    public void removeStaff(String staffID) {
        manager.removeStaff(staffID);
    }

    public void viewStaff() {
        manager.viewStaff();
    }

    // View Appointments
    public void viewAppointments() {
        manager.viewAppointments();
    }

    // Manage Medication Inventory
    public void addMedication(Medication medication) {
        manager.addMedication(medication);
    }

    public void updateMedicationStock(String medicationName, int newStock) {
        manager.updateMedicationStock(medicationName, newStock);
    }

    public void viewMedicationInventory() {
        manager.viewMedicationInventory();
    }

    // Approve or Reject Replenishment Requests
    public void approveReplenishmentRequest(ReplenishmentRequest request, boolean approve) {
        manager.approveReplenishmentRequest(request, approve);
    }

    // Method to view scheduled appointments
    public void viewScheduledAppointments(List<Appointment> appointments) {
        manager.viewScheduledAppointments(appointments);
    }

    // Method to get pending replenishment requests
    public List<String[]> getPendingReplenishmentRequests() {
        return manager.getPendingReplenishmentRequests();
    }

    // Method to process a replenishment request
    public void processReplenishmentRequest(String requestID, boolean approve) {
        manager.processReplenishmentRequest(requestID, approve, this);
    }

    @Override
    public String toString() {
        return String.format("Administrator[ID=%s, Name=%s, Email=%s, Age=%d, Gender=%s]",
            getUserID(), getName(), getEmailId(), getAge(), getGender());
    }
}