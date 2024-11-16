package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.io.AppointmentCsvHelper;
import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DoctorManager{

private static SlotManager slotManager;
private static AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper();  // Helper for writing Appointment Outcomes to CSV
private final static Scanner scanner = new Scanner(System.in);
private static Appointment appointment;
            
// Constructor to initialize SlotManager and ApptCsvHelper
public DoctorManager(SlotManager slotManager, AppointmentCsvHelper appointmentCsvHelper) {
    DoctorManager.slotManager = slotManager;
    DoctorManager.appointmentCsvHelper = appointmentCsvHelper;
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
public void setUnavailable(String doctorID, LocalDateTime dateTime) {
        SlotManager.setAvailability(doctorID, dateTime, false);
}

public static void recordAppointmentOutcome(String appointmentID) {

    if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            // Collect service type
            System.out.print("Enter service type: ");
            String serviceType = scanner.nextLine();
            // Collect diagnosis
            System.out.print("Enter diagnosis: ");
            String diagnosis = scanner.nextLine();

            // Collect prescriptions (if any)
            ArrayList<Prescription> prescriptions = new ArrayList<>();
            String addMorePrescriptions = "y";
            while (addMorePrescriptions.equalsIgnoreCase("y")) {
                System.out.print("Enter prescriptionID: ");
                String prescriptionID = scanner.nextLine();
                System.out.print("Enter prescription name: ");
                String medicationName = scanner.nextLine();
                System.out.print("Enter prescription dosage: ");
                int dosage = scanner.nextInt();  // Use nextInt for integer input
                scanner.nextLine();  // Consume the newline left by nextInt()
                System.out.print("Enter prescription status (e.g., Pending, Completed): ");
                String statusInput = scanner.nextLine();
                PrescriptionStatus status = PrescriptionStatus.valueOf(statusInput.toUpperCase());  // Convert input to PrescriptionStatus enum
                prescriptions.add(new Prescription(prescriptionID, medicationName, dosage, status));

                System.out.print("Do you want to add another prescription? (y/n): ");
                addMorePrescriptions = scanner.nextLine();
            }

            // Collect consultation notes
            System.out.print("Enter consultation notes: ");
            String consultationNotes = scanner.nextLine();

            // Create AppointmentOutcome object with the collected data
            LocalDate appointmentDate = appointment.getDate();  // Assuming appointment date is available as LocalDate
            AppointmentOutcome outcome = new AppointmentOutcome(appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes);

            // Set the outcome on the appointment
            appointment.setOutcome(outcome);

            System.out.println("Recorded appointment outcome for Appointment ID: " + appointment.getAppointmentID() + " on " + appointmentDate);

            // Write the outcome to the CSV file
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
    // Append medication name, dosage, and status, separated by commas for CSV format
    medications.append(prescription.getName()).append(";");
    medicationDosages.append(prescription.getQuantity()).append(";");
    medicationStatuses.append(prescription.getStatus().toString()).append(";");
}
} else {
// If no prescriptions, ensure empty values for CSV
medications.append("None;");
medicationDosages.append("None;");
medicationStatuses.append("None;");
}

// Assume that the `appointment` object has methods to retrieve the patient and doctor information
Patient patientID = appointment.getPatient();
Doctor doctorID = appointment.getDoctor();
LocalTime appointmentTime = appointment.getTime(); // Assuming time is available in `appointment`

// Convert the appointment outcome to a string array, including medications and their statuses
String[] outcomeData = {
appointmentID, 
patientID.toString(), 
doctorID.toString(), 
appointmentDate, 
appointmentTime.toString(), 
serviceType, 
medications.toString(), 
medicationDosages.toString(), 
medicationStatuses.toString(), 
consultationNotes
};
appointmentOutcomes.add(outcomeData);

// Use appointmentCsvHelper to write data to CSV
appointmentCsvHelper.writeEntries(appointmentOutcomes);
}


// public void acceptAppointment(Appointment appointmentID, String doctorID) {
//     if (appointmentID.getStatus() == AppointmentStatus.PENDING) {
//         appointmentID.setStatus(AppointmentStatus.CONFIRMED);
//         System.out.println("Appointment ID " + appointmentID.getAppointmentID() + " accepted by Dr. " + doctorID);
//     } else {
//         System.out.println("Cannot accept appointment. Current status: " + appointment.getStatus());
//     }
// }

// // Method to decline an appointment
// public void declineAppointment(Appointment appointmentID, String doctorID) {
//     if (appointmentID.getStatus() == AppointmentStatus.PENDING) {
//         appointmentID.setStatus(AppointmentStatus.DECLINED);
//         System.out.println("Appointment ID " + appointmentID.getAppointmentID() + " declined by Dr. " + doctorID);
//     } else {
//         System.out.println("Cannot decline appointment. Current status: " + appointmentID.getStatus());
//     }
// }


// Method to accept an appointment
public static void acceptAppointment(String appointmentID, String doctorID) {
// Get the appointment by ID from AppointmentManager
Appointment appointment = AppointmentManager.getAppointmentByID(appointmentID);

if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
appointment.setStatus(AppointmentStatus.CONFIRMED);
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
appointment.setStatus(AppointmentStatus.DECLINED);
System.out.println("Appointment ID " + appointment.getAppointmentID() + " declined by Dr. " + doctorID);
} else if (appointment != null) {
System.out.println("Cannot decline appointment. Current status: " + appointment.getStatus());
} else {
System.out.println("Appointment not found.");
}
}



}