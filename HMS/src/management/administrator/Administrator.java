


package HMS.src.management.administrator;

import HMS.src.appointment.Appointment;
import HMS.src.database.Database;
import HMS.src.management.Gender;
import HMS.src.management.Role;
import HMS.src.management.User;
import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import java.util.List;

public class Administrator extends User {
    public Administrator(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.ADMINISTRATOR, emailId, age, gender);
    }

    // Manage Staff
    public void addStaff(User staff) {
        Database.getUserData().put(staff.getUserID().toString(), staff);
        System.out.println("Staff member added: " + staff.getName());
    }

    public void removeStaff(String staffID) {
        if (Database.getUserData().remove(staffID) != null) {
            System.out.println("Staff member removed with ID: " + staffID);
        } else {
            System.out.println("Staff member not found with ID: " + staffID);
        }
    }

    // View Appointments
    public void viewAppointments() {
        Database.getAppointments().forEach(appointment ->
            System.out.println(appointment)
        );
    }

    // Manage Medication Inventory
    public void addMedication(Medication medication) {
        Database.getMedicineData().put(medication.getName(), medication);
        System.out.println("Medication added: " + medication.getName());
    }

    public void updateMedicationStock(String medicationName, int newStock) {
        Medication medication = Database.getMedicineData().get(medicationName);
        if (medication != null) {
            medication.setInventoryStock(newStock);
            System.out.println("Updated stock for " + medicationName + " to " + newStock);
        } else {
            System.out.println("Medication not found: " + medicationName);
        }
    }

    public void viewMedicationInventory() {
        Database.getMedicineData().values().forEach(medication ->
            System.out.println(medication)
        );
    }

    // Approve or Reject Replenishment Requests
    public void approveReplenishmentRequest(ReplenishmentRequest request, boolean approve) {
        if (approve) {
            request.approveRequest(this.getName());
            Medication medication = Database.getMedicineData().get(request.getmedicineName());
            if (medication != null) {
                medication.setInventoryStock(medication.getInventoryStock() + request.getQuantity());
                System.out.println("Replenishment approved for " + request.getmedicineName() + ": stock updated.");
            }
        } else {
            request.rejectRequest(this.getName());
            System.out.println("Replenishment request rejected for " + request.getmedicineName());
        }
    }

    // Method to view scheduled appointments
    public void viewScheduledAppointments(List<Appointment> appointments) {
        System.out.println("Scheduled Appointments:");
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }
    }
}