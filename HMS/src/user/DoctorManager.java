package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.io.*;
import java.time.LocalDate;
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
    MedicalRecordCsvHelper medicalrecCsvHelper = new MedicalRecordCsvHelper();

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
        // Ask for doctor inputs related to the outcome
        System.out.print("Enter the type of service provided: ");
        String service = scanner.nextLine();
        System.out.print("Enter the Diagnosis: ");
        String diagnosis = scanner.nextLine();
        System.out.print("Enter the Treatment Plan: ");
        String treatmentPlan = scanner.nextLine();
        System.out.print("Enter the medicine name (if any): ");
        String medicine = scanner.nextLine().trim();
        System.out.print("Enter the dosage (if any): ");
        String dosage = scanner.nextLine().trim();
        System.out.print("Enter any notes: ");
        String notes = scanner.nextLine().trim();

        // Debug: Print out diagnosis and treatmentPlan
        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Treatment Plan: " + treatmentPlan);

        // Combine medicine and dosage for prescriptions
        String prescriptions = (medicine != null && !medicine.isEmpty() ? medicine : "N/A") + 
                               (dosage != null && !dosage.isEmpty() ? ", " + dosage : "");

        // Create the outcome entry for the appointment outcome CSV (with service)
        String[] newOutcome = new String[] {
                appointment[0],  // Appointment ID
                appointment[1],  // Patient ID
                appointment[2],  // Doctor ID
                appointment[3],  // Date of Appointment
                appointment[4],  // Appointment Time
                service,         // Service Type (Service Provided)
                prescriptions,   // Prescriptions (medicine and dosage)
                notes            // Notes
        };

        // Prepare the updated appointment status (set to "COMPLETED")
        String[] updatedAppointment = new String[] {
                appointment[0],  // Appointment ID
                appointment[1],  // Patient ID
                appointment[2],  // Doctor ID
                appointment[3],  // Date of Appointment
                appointment[4],  // Appointment Time
                "COMPLETED"      // Status
        };

        // Add the outcome entry to the appointment outcome CSV
        apptCsvHelper.addNewOutcome(newOutcome);
        csvHelper.updateApptById(appointmentID, updatedAppointment);

        // Now, check if a medical record exists for the patient
        List<String[]> medicalRecords = medicalrecCsvHelper.readCSV();
        String patientID = appointment[1];

        // Create the new record ID for this entry
        String newRecordID = getNextRecordID(medicalRecords);


        // Create the new medical record with diagnosis and treatment plan
        String[] newRecord = new String[] {
            newRecordID,        // Generate new Record ID
            diagnosis,          // Diagnosis
            treatmentPlan,      // Treatment Plan
            patientID           // Patient ID
        };

        // Add the new record to the list
        medicalRecords.add(newRecord);
        // Update the medical records CSV by appending the new record
        medicalrecCsvHelper.updateMedicalRecords(medicalRecords);

        System.out.println("Updated appointment details and added new medical record successfully.");
    }
}


// Helper method to find the next available Record ID
private static String getNextRecordID(List<String[]> medicalRecords) {
    int highestRecordID = 0;

    // Iterate over all records and find the highest Record ID that starts with "R"
    for (String[] record : medicalRecords) {
        if (record.length >= 5 && record[0].startsWith("R")) {
            try {
                // Get numeric part after "R" and find the highest number
                int recordID = Integer.parseInt(record[0].substring(1));
                highestRecordID = Math.max(highestRecordID, recordID);
            } catch (NumberFormatException e) {
                System.err.println("Error parsing RecordID: " + record[0]);
            }
        }
    }

    // Return the next Record ID as "R" followed by the incremented number
    return "R" + String.format("%03d", highestRecordID + 1);
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
