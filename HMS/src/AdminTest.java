package HMS.src;

import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionStatus;
import HMS.src.user.Administrator;
import HMS.src.user.Doctor;
import HMS.src.user.Gender;
import HMS.src.user.Patient;
import HMS.src.user.Pharmacist;
import HMS.src.user.User;
import HMS.src.appointment.Appointment;
import HMS.src.appointment.AppointmentStatus;
import HMS.src.appointment.ServiceType;
import HMS.src.archive.Database;
import HMS.src.medication.ReplenishmentRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminTest {
    public static void main(String[] args) {
        // Initialize Database instance and load data
        Database database = Database.getInstance();

        // Set up an existing Administrator object based on CSV data
        Administrator admin = (Administrator) Database.getUserData().get("A001");

        if (admin == null) {
            System.out.println("Administrator with ID A001 not found in CSV data.");
            return;
        }

        // Print Administrator details
        System.out.println("\n--- Administrator Details ---");
        System.out.println(admin);

        // View Medication Inventory
        System.out.println("\n--- Viewing Medication Inventory ---");
        admin.viewMedicationInventory();

        // Test Updating Medication Stock
        System.out.println("\n--- Updating Medication Stock ---");
        String medicationName = "Ibuprofen";
        int newStock = 150;
        admin.updateMedicationStock(medicationName, newStock);
        System.out.println("\n--- Medication Inventory After Stock Update ---");
        admin.viewMedicationInventory();

        // Test Processing Replenishment Request
        System.out.println("\n--- Processing Replenishment Request ---");
        ReplenishmentRequest request = new ReplenishmentRequest("req001", "Paracetamol", 30, LocalDate.now());
        Database.getReplenishmentRequests().add(request);
        admin.approveReplenishmentRequest(request, true);
        System.out.println("\n--- Medication Inventory After Processing Replenishment Request ---");
        admin.viewMedicationInventory();

        // View All Staff
        System.out.println("\n--- Viewing All Staff ---");
        for (Map.Entry<String, User> entry : Database.getUserData().entrySet()) {
            System.out.println(entry.getValue());
        }

        // Add New Staff Member
        System.out.println("\n--- Adding a New Staff Member ---");
        Doctor newDoctor = new Doctor("D003", "Laura Green", "laura.green@example.com", 42, Gender.FEMALE);
        Database.getUserData().put(newDoctor.getUserID().toString(), newDoctor);
        System.out.println("Added new staff member: " + newDoctor);
        System.out.println("\n--- Updated Staff List After Adding New Member ---");
        for (Map.Entry<String, User> entry : Database.getUserData().entrySet()) {
            System.out.println(entry.getValue());
        }

        // Update Existing Staff Member Details
        System.out.println("\n--- Updating an Existing Staff Member ---");
        Doctor existingDoctor = (Doctor) Database.getUserData().get("D001");
        if (existingDoctor != null) {
            existingDoctor.setName("John Smith Jr.");
            existingDoctor.changeEmailId("john.smithjr@example.com", existingDoctor.getEmailId());
            System.out.println("Updated staff member: " + existingDoctor);
        } else {
            System.out.println("Doctor with ID D001 not found.");
        }
        System.out.println("\n--- Updated Staff List After Modification ---");
        for (Map.Entry<String, User> entry : Database.getUserData().entrySet()) {
            System.out.println(entry.getValue());
        }

        // Remove a Staff Member
        System.out.println("\n--- Removing a Staff Member ---");
        if (Database.getUserData().containsKey("P001")) {
            User removedUser = Database.getUserData().remove("P001");
            System.out.println("Removed staff member: " + removedUser);
        } else {
            System.out.println("Pharmacist with ID P001 not found.");
        }
        System.out.println("\n--- Updated Staff List After Removal ---");
        for (Map.Entry<String, User> entry : Database.getUserData().entrySet()) {
            System.out.println(entry.getValue());
        }

        // Initialize a list to store test appointments
        List<Appointment> appointments = new ArrayList<>();

        // Create test data for appointments
        Patient patient = (Patient) Database.getUserData().get("P1001");
        Doctor doctor = (Doctor) Database.getUserData().get("D001");
        if (patient != null && doctor != null) {
            Appointment appointment1 = new Appointment("APPT001", patient, doctor, LocalDate.of(2023, 11, 20), LocalTime.of(10, 0));
            Appointment appointment2 = new Appointment("APPT002", patient, doctor, LocalDate.of(2023, 11, 21), LocalTime.of(14, 0));
            appointments.add(appointment1);
            appointments.add(appointment2);
        } else {
            System.out.println("Patient or Doctor not found.");
            return;
        }

        // View Scheduled Appointments
        System.out.println("\n--- Viewing Scheduled Appointments ---");
        admin.viewScheduledAppointments(appointments);

        // Update Appointment Status
        System.out.println("\n--- Updating Appointment Status ---");
        if (!appointments.isEmpty()) {
            doctor.updateAppointmentStatus(appointments.get(0), AppointmentStatus.CONFIRMED);
            doctor.updateAppointmentStatus(appointments.get(1), AppointmentStatus.COMPLETED);
        }

        // Record Appointment Outcome for Completed Appointment
        System.out.println("\n--- Recording Appointment Outcome ---");
        if (!appointments.isEmpty()) {
            ArrayList<Prescription> prescriptions = new ArrayList<>();
            prescriptions.add(new Prescription("RX001", "Amoxicillin", 2, PrescriptionStatus.PENDING));
            prescriptions.add(new Prescription("RX002", "Ibuprofen", 1, PrescriptionStatus.PENDING));

            doctor.recordAppointmentOutcome(appointments.get(1), ServiceType.CONSULTATION, "Diagnosis: Cold and Fever",
                    prescriptions, "Patient advised rest and hydration.");
        }

        // View Appointments After Status and Outcome Updates
        System.out.println("\n--- Viewing Appointments After Status and Outcome Updates ---");
        admin.viewScheduledAppointments(appointments);

        // Save changes to CSV if necessary
        // System.out.println("\n--- Saving Changes to CSV Files ---");
        // database.saveToCSV();
        // System.out.println("Changes saved to CSV files.");
    }
}