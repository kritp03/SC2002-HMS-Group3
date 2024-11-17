package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.archive.Database;
import HMS.src.io.AppointmentCsvHelper;
import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DoctorManager{

    
    private static SlotManager slotManager;
    private static AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper();  // Helper for writing Appointment Outcomes to CSV
    private final static Scanner scanner = new Scanner(System.in);

    // Constructor to initialize SlotManager and ApptCsvHelper
    public DoctorManager(SlotManager slotManager, AppointmentCsvHelper appointmentCsvHelper) {
        DoctorManager.slotManager = slotManager;
        DoctorManager.appointmentCsvHelper = appointmentCsvHelper;
    }

    // Helper method to get appointment by ID
    private static Appointment getAppointmentByID(String appointmentID) {
        // Assume appointments are stored in a map or list
        HashMap<String, Appointment> appointments = Database.getAppointments();
        return appointments.get(appointmentID);
    }

    // Method to record appointment outcome
    public static void recordAppointmentOutcome(String appointmentID) {
        // Retrieve the appointment object based on the appointmentID
        Appointment appointmentOutcome = getAppointmentByID(appointmentID);

        // Check if the appointment exists
        if (appointmentOutcome == null) {
            System.out.println("Appointment with ID " + appointmentID + " not found.");
            return; // Exit the method
        }

        // Proceed only if the appointment status is COMPLETED
        if (appointmentOutcome.getStatus() == AppointmentStatus.COMPLETED) {
            // Collect service type
            System.out.print("Enter service type: ");
            String serviceType = scanner.nextLine();

            // Collect diagnosis
            System.out.print("Enter diagnosis: ");
            String diagnosis = scanner.nextLine();

            // Collect prescriptions
            ArrayList<Prescription> prescriptions = new ArrayList<>();
            String addMorePrescriptions = "y";
            while (addMorePrescriptions.equalsIgnoreCase("y")) {
                System.out.print("Enter prescription ID: ");
                String prescriptionID = scanner.nextLine();
                System.out.print("Enter prescription name: ");
                String medicationName = scanner.nextLine();
                System.out.print("Enter prescription dosage: ");
                int dosage = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                System.out.print("Enter prescription status (e.g., Pending, Completed): ");
                String statusInput = scanner.nextLine();
                PrescriptionStatus status = PrescriptionStatus.valueOf(statusInput.toUpperCase());

                prescriptions.add(new Prescription(prescriptionID, medicationName, dosage, status));

                System.out.print("Do you want to add another prescription? (y/n): ");
                addMorePrescriptions = scanner.nextLine();
            }

            // Collect consultation notes
            System.out.print("Enter consultation notes: ");
            String consultationNotes = scanner.nextLine();

            // Get doctor object from appointment
            Doctor doctor = appointmentOutcome.getDoctor(); // Assuming appointment has a method getDoctor()

            // Create AppointmentOutcome object
            LocalDate appointmentDate = appointmentOutcome.getDate();
            AppointmentOutcome outcome = new AppointmentOutcome(
                    appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes, doctor);

            // Set the outcome on the appointment
            appointmentOutcome.setOutcome(outcome);

            System.out.println("Recorded appointment outcome for Appointment ID: " + appointmentOutcome.getAppointmentID());

            // Write the outcome to a CSV file
            writeAppointmentOutcomeToCSV(outcome);
        } else {
            System.out.println("Appointment is not completed yet, cannot record outcome.");
        }
    }

    // Helper method to write the appointment outcome to a CSV
    private static void writeAppointmentOutcomeToCSV(AppointmentOutcome outcome) {
        List<String[]> appointmentOutcomes = new ArrayList<>();
        String appointmentID = outcome.getAppointmentID();
        String serviceType = outcome.getServiceType();
        String appointmentDate = outcome.getAppointmentDate().toString();
        String consultationNotes = outcome.getConsultationNotes();
    
        // Prescriptions and their statuses
        StringBuilder medications = new StringBuilder();
        StringBuilder medicationDosages = new StringBuilder();
        StringBuilder medicationStatuses = new StringBuilder();
    
        // Add medication details (if any) to the output
        if (outcome.getPrescriptions() != null && !outcome.getPrescriptions().isEmpty()) {
            for (Prescription prescription : outcome.getPrescriptions()) {
                medications.append(prescription.getName()).append(";");
                medicationDosages.append(prescription.getQuantity()).append(";");
                medicationStatuses.append(prescription.getStatus().toString()).append(";");
            }
        } else {
            medications.append("None;");
            medicationDosages.append("None;");
            medicationStatuses.append("None;");
        }
    
        // Assuming `appointment` object is stored with `outcome` or can be retrieved elsewhere
        Appointment appointment = getAppointmentByID(outcome.getAppointmentID()); // Retrieve appointment using the appointmentID
        if (appointment == null) {
            System.out.println("Appointment not found for outcome: " + outcome.getAppointmentID());
            return;
        }
    
        // Retrieve the patient and doctor information from the appointment
        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();
        LocalTime appointmentTime = appointment.getTime(); // Assuming time is available in `appointment`
    
        // Convert the appointment outcome to a string array
        String[] outcomeData = {
                appointmentID,
                patient.toString(),  // Assuming patient object has a toString() method
                doctor.toString(),   // Assuming doctor object has a toString() method
                appointmentDate,
                appointmentTime.toString(),
                serviceType,
                medications.toString(),
                medicationDosages.toString(),
                medicationStatuses.toString(),
                consultationNotes
        };
        appointmentOutcomes.add(outcomeData);
    
        // Write the data to a CSV using the helper
        appointmentCsvHelper.writeEntries(appointmentOutcomes);
    }

    // View available slots for the doctor
    public void viewAvailableSlots(String doctorID) {
        System.out.println("Available slots for Dr. " + doctorID + ":");
        SlotManager.printSlots(doctorID);
    }

    // View available slots for a specific day
    public static void viewScheduleForDay(LocalDate date, String doctorID) {
        System.out.println("Schedule for Dr. " + doctorID + " on " + date + ":");
        List<Slot> slots = slotManager.getSlots(doctorID);
        for (Slot slot : slots) {
            if (slot.getDateTime().toLocalDate().equals(date)) {
                System.out.println(slot);
            }
        }
    }

    // Method to mark a slot as unavailable
    public static void setUnavailable(String doctorID, LocalDateTime dateTime) {
        SlotManager.setAvailability(doctorID, dateTime, false);
    }
    
    // Method to accept an appointment
    public static void acceptAppointment(String appointmentID, String doctorID) {
        // Get the appointment by ID from AppointmentManager
        Appointment appointment = AppointmentManager.getAppointmentByID(appointmentID);

        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);  // Use the correct object reference
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " accepted by Dr. " + doctorID);
        } else if (appointment != null) {
            System.out.println("Cannot accept appointment. Current status: " + appointment.getStatus());
        } else {
            System.out.println("Appointment not found.");
        }
    }

    // Method to decline an appointment
    public static void declineAppointment(String appointmentID, String doctorID) {
        // Get the appointment by ID from AppointmentManager
        Appointment appointment = AppointmentManager.getAppointmentByID(appointmentID);

        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.DECLINED);  // Use the correct object reference
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " declined by Dr. " + doctorID);
        } else if (appointment != null) {
            System.out.println("Cannot decline appointment. Current status: " + appointment.getStatus());
        } else {
            System.out.println("Appointment not found.");
        }
    }
}
