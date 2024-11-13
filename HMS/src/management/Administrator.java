


package HMS.src.management;

import HMS.src.medication.Medication;
import HMS.src.medication.ReplenishmentRequest;
import HMS.src.medication.ReplenishmentRequestStatus;
import HMS.src.database.Database;
import HMS.src.misc_classes.Gender;
import HMS.src.prescription.Prescription;
import HMS.src.management.User;
import HMS.src.appointment.Appointment;
import HMS.src.appointment.AppointmentStatus;
import HMS.src.appointment.AppointmentOutcome;
import HMS.src.appointment.ServiceType;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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
            Medication medication = Database.getMedicineData().get(request.getMedicationID());
            if (medication != null) {
                medication.setInventoryStock(medication.getInventoryStock() + request.getQuantity());
                System.out.println("Replenishment approved for " + request.getMedicationID() + ": stock updated.");
            }
        } else {
            request.rejectRequest(this.getName());
            System.out.println("Replenishment request rejected for " + request.getMedicationID());
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