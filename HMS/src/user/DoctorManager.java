package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.archive.Database;
import HMS.src.io.*;
import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionStatus;
import java.time.LocalDate;
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

    // public static void viewPatientMedicalRecords(String patientID) {
        
    //     // Initialize the CSV Helper to fetch medical records
    //     MedicalRecordCsvHelper csvHelper = new MedicalRecordCsvHelper();
    //     List<String[]> medicalRecords = csvHelper.readCSV();
    
    //     // Check if records exist for the given patient ID
    //     boolean recordFound = false;
    //     System.out.println("Medical Records for Patient ID: " + patientID);
    //     System.out.println("================================================================");
    
    //     for (String[] record : medicalRecords) {
    //         // Ensure the CSV row is valid and belongs to the patient ID
    //         if (record.length >= 5 && record[4].equalsIgnoreCase(patientID)) {
    //             System.out.println("Record ID: " + record[0]);
    //             System.out.println("Diagnosis: " + (record[1].isEmpty() ? "N/A" : record[1]));
    //             System.out.println("Treatment Plan: " + (record[2].isEmpty() ? "N/A" : record[2]));
    //             System.out.println("Prescriptions: " + (record[3].isEmpty() ? "N/A" : record[3]));
    //             System.out.println("----------------------------------------------------------------");
    //             recordFound = true;
    //         }
    //     }
    
    //     if (!recordFound) {
    //         System.out.println("No medical records found for patient ID: " + patientID);
    //     }
    // }


    public static void viewPatientMedicalRecords(String patientID) {
        // Initialize the CSV Helper to fetch medical records
        MedicalRecordCsvHelper csvHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalRecords = csvHelper.readCSV();
    
        // Check if records exist for the given patient ID
        boolean recordFound = false;
    
        // Print the header
        System.out.println("+------------+---------------------+------------------------+--------------------+");
        System.out.format("| %-10s | %-20s | %-22s | %-18s |\n", 
                          "Record ID", "Diagnosis", "Treatment Plan", "Prescriptions");
        System.out.println("+------------+---------------------+------------------------+--------------------+");
    
        for (String[] record : medicalRecords) {
            // Ensure the CSV row is valid and belongs to the patient ID
            if (record.length >= 5 && record[4].equalsIgnoreCase(patientID)) {
                System.out.format("| %-10s | %-20s | %-22s | %-18s |\n",
                                  record[0],
                                  (record[1].isEmpty() ? "N/A" : record[1]),
                                  (record[2].isEmpty() ? "N/A" : record[2]),
                                  (record[3].isEmpty() ? "N/A" : record[3]));
                recordFound = true;
            }
        }
    
        // Close the table or display a message if no records found
        if (recordFound) {
            System.out.println("+------------+---------------------+------------------------+--------------------+");
        } else {
            System.out.println("No medical records found for patient ID: " + patientID);
        }
    }
    
    public static void updatePatientMedicalRecord(String patientID, String diagnosis, String treatmentPlan, String prescriptions) {
        System.out.println("Updating Medical Records for Patient ID: " + patientID);
    
        // Initialize the CSV Helper to fetch and update medical records
        MedicalRecordCsvHelper csvHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalRecords = csvHelper.readCSV();
    
        int highestRecordID = 0;
    
        // Find the highest existing RecordID
        for (String[] record : medicalRecords) {
            if (record.length >= 5) {
                // Extract the numeric part of the RecordID and track the highest value
                String recordID = record[0];
                if (recordID.startsWith("R")) {
                    int recordIDNum = Integer.parseInt(recordID.substring(1));
                    highestRecordID = Math.max(highestRecordID, recordIDNum);
                }
            }
        }
    
        // Generate the next RecordID for the new entry
        String newRecordID = "R" + String.format("%03d", highestRecordID + 1);
    
        // Create a new record for the patient
        String[] newRecord = {
            newRecordID,                                      // RecordID
            diagnosis != null ? diagnosis : "",              // Diagnosis
            treatmentPlan != null ? treatmentPlan : "",      // Treatment Plan
            prescriptions != null ? prescriptions : "",      // Prescriptions
            patientID                                        // PatientID
        };
    
        // Append the new record to the list
        medicalRecords.add(newRecord);
        System.out.println("New medical record added with RecordID: " + newRecordID);
    
        // Write updated records back to the CSV
        csvHelper.updateMedicalRecords(medicalRecords);
        System.out.println("Medical record updated successfully.");
    }
    
    
}
