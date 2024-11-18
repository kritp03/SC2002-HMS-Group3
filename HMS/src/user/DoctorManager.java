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

public class DoctorManager {

    private static SlotManager slotManager;
    private static AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper(); // Helper for writing
                                                                                           // Appointment Outcomes to
                                                                                           // CSV
    private final static Scanner scanner = new Scanner(System.in);

    // Constructor to initialize SlotManager and ApptCsvHelper
    public DoctorManager(SlotManager slotManager, AppointmentCsvHelper appointmentCsvHelper) {
        DoctorManager.slotManager = slotManager;
        DoctorManager.appointmentCsvHelper = appointmentCsvHelper;
    }

    // Helper method to get appointment by ID
    private static boolean getAppointmentByID(String appointmentID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        List<String[]> appointments = csvHelper.readCSV();

        for (String[] appointment : appointments) {
            if (appointment[0].equalsIgnoreCase(appointmentID)) {
                return true; // Appointment ID found
            }
        }
        return false; // Appointment ID not found
    }

    // Method to record appointment outcome
    public static void recordAppointmentOutcome(String appointmentID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        ApptCsvHelper apptCsvHelper = new ApptCsvHelper();

        // Check if the appointment exists
        if (!getAppointmentByID(appointmentID)) {
            System.out.println("Appointment not found for ID: " + appointmentID);
            return;
        }

        // Retrieve the appointment array using the getEntryById method
        String[] appointment = csvHelper.getEntryById(appointmentID);

        if (appointment == null) {
            System.out.println("No data found for appointment ID: " + appointmentID);
        } else {
            System.out.print("Enter the type of service provided: ");
            String service = scanner.nextLine().trim();
            System.out.print("Enter the medicine name (if any): ");
            String medicine = scanner.nextLine().trim();
            System.out.print("Enter the dosage (if any): ");
            String dosage = scanner.nextLine().trim();
            System.out.print("Enter any notes: ");
            String notes = scanner.nextLine().trim();

            String[] newOutome = new String[] {
                    appointment[0],
                    appointment[1],
                    appointment[2],
                    appointment[3],
                    appointment[4],
                    service,
                    medicine,
                    dosage,
                    notes
            };

            String [] updatedAppointment = new String[] {
                    appointment[0],
                    appointment[1],
                    appointment[2],
                    appointment[3],
                    appointment[4],
                    "COMPLETED"
            };
            apptCsvHelper.addNewOutcome(newOutome);
            csvHelper.updateApptById(appointmentID, updatedAppointment);
            System.out.println("Updated appointment details saved successfully.");
        }
    }
    // public static void recordAppointmentOutcome(String appointmentID) {
    // AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
    // List<String[]> appointments = csvHelper.getApptById();
    // // // Retrieve the appointment object based on the appointmentID
    // // Appointment appointmentOutcome =
    // getAppointmentByID(appointmentID.toUpperCase());

    // // Check if the appointment exists
    // if (getAppointmentByID(appointmentID) == false) {
    // System.out.println("Appointment not found for ID: " + appointmentID);
    // return;
    // }
    // else{

    // }

    // // Proceed only if the appointment status is COMPLETED
    // if (appointmentOutcome.getStatus() == AppointmentStatus.COMPLETED) {
    // // Collect service type
    // System.out.print("Enter service type: ");
    // String serviceType = scanner.nextLine();

    // // Collect diagnosis
    // System.out.print("Enter diagnosis: ");
    // String diagnosis = scanner.nextLine();

    // // Collect prescriptions
    // ArrayList<Prescription> prescriptions = new ArrayList<>();
    // String addMorePrescriptions = "y";
    // while (addMorePrescriptions.equalsIgnoreCase("y")) {
    // System.out.print("Enter prescription ID: ");
    // String prescriptionID = scanner.nextLine();
    // System.out.print("Enter prescription name: ");
    // String medicationName = scanner.nextLine();
    // System.out.print("Enter prescription dosage: ");
    // int dosage = scanner.nextInt();
    // scanner.nextLine(); // Consume newline
    // System.out.print("Enter prescription status (e.g., Pending, Completed): ");
    // String statusInput = scanner.nextLine();
    // PrescriptionStatus status =
    // PrescriptionStatus.valueOf(statusInput.toUpperCase());

    // prescriptions.add(new Prescription(prescriptionID, medicationName, dosage,
    // status));

    // System.out.print("Do you want to add another prescription? (y/n): ");
    // addMorePrescriptions = scanner.nextLine();
    // }

    // // Collect consultation notes
    // System.out.print("Enter consultation notes: ");
    // String consultationNotes = scanner.nextLine();

    // // Get doctor object from appointment
    // Doctor doctor = appointmentOutcome.getDoctor(); // Assuming appointment has a
    // method getDoctor()

    // // Create AppointmentOutcome object
    // LocalDate appointmentDate = appointmentOutcome.getDate();
    // AppointmentOutcome outcome = new AppointmentOutcome(
    // appointmentID, appointmentDate, serviceType, diagnosis, prescriptions,
    // consultationNotes, doctor);

    // // Set the outcome on the appointment
    // appointmentOutcome.setOutcome(outcome);

    // System.out.println(
    // "Recorded appointment outcome for Appointment ID: " +
    // appointmentOutcome.getAppointmentID());

    // // Write the outcome to a CSV file
    // writeAppointmentOutcomeToCSV(outcome);
    // } else {
    // System.out.println("Appointment is not completed yet, cannot record
    // outcome.");
    // }
    // }

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

        // Assuming `appointment` object is stored with `outcome` or can be retrieved
        // elsewhere
        Appointment appointment = getAppointmentByID(outcome.getAppointmentID()); // Retrieve appointment using the
                                                                                  // appointmentID
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
                patient.toString(), // Assuming patient object has a toString() method
                doctor.toString(), // Assuming doctor object has a toString() method
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

    public static void acceptAppointment(String appointmentID, String doctorID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        List<String[]> appointments = csvHelper.readCSV();
        boolean found = false;

        // Prepare the updated appointment data
        String[] updatedAppointment = null;

        for (String[] appointment : appointments) {
            if (appointment[0].equalsIgnoreCase(appointmentID) && appointment[2].equalsIgnoreCase(doctorID)
                    && "PENDING".equalsIgnoreCase(appointment[5])) {
                appointment[5] = "CONFIRMED"; // Update the status to CONFIRMED
                updatedAppointment = appointment; // This holds the full updated appointment row
                found = true;
                break;
            }
        }

        if (found && updatedAppointment != null) {
            csvHelper.updateApptById(updatedAppointment[0], updatedAppointment); // Call update method with the updated
                                                                                 // array
            System.out.println(updatedAppointment[0].toUpperCase() + " successfully accepted!\n");
        } else {
            System.out.println("Appointment not found or already processed.");
        }
    }

    public static void declineAppointment(String appointmentID, String doctorID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        List<String[]> appointments = csvHelper.readCSV();
        boolean found = false;

        // Prepare the updated appointment data
        String[] updatedAppointment = null;

        for (String[] appointment : appointments) {
            if (appointment[0].equalsIgnoreCase(appointmentID) && appointment[2].equalsIgnoreCase(doctorID)
                    && "PENDING".equalsIgnoreCase(appointment[5])) {
                appointment[5] = "DECLINED"; // Update the status to CONFIRMED
                updatedAppointment = appointment; // This holds the full updated appointment row
                found = true;
                break;
            }
        }

        if (found && updatedAppointment != null) {
            csvHelper.updateApptById(updatedAppointment[0], updatedAppointment); // Call update method with the updated
                                                                                 // array
            System.out.println(updatedAppointment[0].toUpperCase() + " successfully declined!\n");
        } else {
            System.out.println("Appointment not found or already processed.");
        }
    }

    // public static void viewPatientMedicalRecords(String patientID) {

    // // Initialize the CSV Helper to fetch medical records
    // MedicalRecordCsvHelper csvHelper = new MedicalRecordCsvHelper();
    // List<String[]> medicalRecords = csvHelper.readCSV();

    // // Check if records exist for the given patient ID
    // boolean recordFound = false;
    // System.out.println("Medical Records for Patient ID: " + patientID);
    // System.out.println("================================================================");

    // for (String[] record : medicalRecords) {
    // // Ensure the CSV row is valid and belongs to the patient ID
    // if (record.length >= 5 && record[4].equalsIgnoreCase(patientID)) {
    // System.out.println("Record ID: " + record[0]);
    // System.out.println("Diagnosis: " + (record[1].isEmpty() ? "N/A" :
    // record[1]));
    // System.out.println("Treatment Plan: " + (record[2].isEmpty() ? "N/A" :
    // record[2]));
    // System.out.println("Prescriptions: " + (record[3].isEmpty() ? "N/A" :
    // record[3]));
    // System.out.println("----------------------------------------------------------------");
    // recordFound = true;
    // }
    // }

    // if (!recordFound) {
    // System.out.println("No medical records found for patient ID: " + patientID);
    // }
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

    public static boolean viewAllPending(String doctorID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        List<String[]> appList = csvHelper.readCSV();

        boolean hasPending = false;

        for (String[] appointment : appList) {
            if (appointment.length >= 6 && appointment[2].equalsIgnoreCase(doctorID)
                    && "pending".equalsIgnoreCase(appointment[5])) {
                if (!hasPending) {
                    // Print headers only if there's at least one pending appointment
                    System.out.println("+-----------+-----------+------------+------------+-----------+");
                    System.out.println("| Appt ID   | Patient ID | Date       | Time       | Status    |");
                    System.out.println("+-----------+-----------+------------+------------+-----------+");
                    hasPending = true;
                }
                System.out.format("| %-9s | %-10s | %-11s | %-11s | %-9s |\n",
                        appointment[0], // Appointment ID
                        appointment[1], // Patient ID
                        appointment[3], // Date
                        appointment[4], // Time
                        appointment[5]); // Status
            }
        }
        if (hasPending) {
            System.out.println("+-----------+-----------+------------+------------+-----------+");
        } else {
            System.out.println("You do not have any pending appointments!");
        }
        return hasPending;
    }

    public static boolean viewAllConfirmedAndPending(String doctorID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        List<String[]> appList = csvHelper.readCSV();

        boolean hasAppointments = false;

        for (String[] appointment : appList) {
            if (appointment.length >= 6 && appointment[2].equalsIgnoreCase(doctorID)
                    && ("CONFIRMED".equalsIgnoreCase(appointment[5]) || "PENDING".equalsIgnoreCase(appointment[5]))) {
                if (!hasAppointments) {
                    // Print headers only if there's at least one confirmed or pending appointment
                    System.out.println("+-----------+-----------+------------+------------+-----------+");
                    System.out.println("| Appt ID   | Patient ID | Date       | Time       | Status    |");
                    System.out.println("+-----------+-----------+------------+------------+-----------+");
                    hasAppointments = true;
                }
                String statusColored = AppointmentStatus.valueOf(appointment[5].toUpperCase()).showStatusByColor();
                System.out.format("| %-9s | %-10s | %-11s | %-11s | %-9s |\n",
                        appointment[0], // Appointment ID
                        appointment[1], // Patient ID
                        appointment[3], // Date
                        appointment[4], // Time
                        statusColored); // Status
            }
        }
        if (hasAppointments) {
            System.out.println("+-----------+-----------+------------+------------+-----------+\n");
        } else {
            System.out.println("You do not have any confirmed or pending appointments!\n");
        }
        return hasAppointments;
    }

    public static void viewAllConfirmed(String doctorID) {
        AppointmentCsvHelper csvHelper = new AppointmentCsvHelper();
        List<String[]> appList = csvHelper.readCSV();

        boolean hasAppointments = false;

        for (String[] appointment : appList) {
            if (appointment.length >= 6 && appointment[2].equalsIgnoreCase(doctorID)
                    && "CONFIRMED".equalsIgnoreCase(appointment[5])) {
                if (!hasAppointments) {
                    // Print headers only if there's at least one confirmed appointment
                    System.out.println("+-----------+-----------+------------+------------+-----------+");
                    System.out.println("| Appt ID   | Patient ID | Date       | Time       | Status    |");
                    System.out.println("+-----------+-----------+------------+------------+-----------+");
                    hasAppointments = true;
                }
                // Use the enum to get the colored status text
                String statusColored = AppointmentStatus.valueOf(appointment[5].toUpperCase()).showStatusByColor();
                System.out.format("| %-9s | %-10s | %-11s | %-11s | %-9s |\n",
                        appointment[0], // Appointment ID
                        appointment[1], // Patient ID
                        appointment[3], // Date
                        appointment[4], // Time
                        statusColored); // Colored Status
            }
        }
        if (hasAppointments) {
            System.out.println("+-----------+-----------+------------+------------+-----------+");
        } else {
            System.out.println("You do not have any confirmed appointments!");
        }
    }

    public static void updatePatientMedicalRecord(String patientID, String diagnosis, String treatmentPlan,
            String prescriptions) {
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
                newRecordID, // RecordID
                diagnosis != null ? diagnosis : "", // Diagnosis
                treatmentPlan != null ? treatmentPlan : "", // Treatment Plan
                prescriptions != null ? prescriptions : "", // Prescriptions
                patientID // PatientID
        };

        // Append the new record to the list
        medicalRecords.add(newRecord);
        System.out.println("New medical record added with RecordID: " + newRecordID);

        // Write updated records back to the CSV
        csvHelper.updateMedicalRecords(medicalRecords);
        System.out.println("Medical record updated successfully.");
    }

}
