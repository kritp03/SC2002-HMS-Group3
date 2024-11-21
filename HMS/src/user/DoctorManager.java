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
        System.out.print("Enter any notes: ");
        String notes = scanner.nextLine().trim();

        System.out.println("Diagnosis: " + diagnosis);
        System.out.println("Treatment Plan: " + treatmentPlan);

        // Combine medicine and dosage for prescriptions
        String prescriptions = (medicine != null && !medicine.isEmpty() ? medicine : "N/A");

        // Create the outcome entry for the appointment outcome CSV (with service)
        String[] newOutcome = new String[] {
                appointment[0],  // Appointment ID
                appointment[1],  // Patient ID
                appointment[2],  // Doctor ID
                appointment[3],  // Date of Appointment
                appointment[4],  // Appointment Time
                service,         // Service Type (Service Provided)
                prescriptions,   // Prescriptions (medicine and dosage)
                "PENDING",       // Status (PENDING by default)
                notes            // Notes
        };

        // Prepare the updated appointment status (set to "COMPLETED")
        String[] updatedAppointment = new String[] {
                appointment[0],  // Appointment ID
                appointment[1],  // Patient ID
                appointment[2],  // Doctor ID
                appointment[3],  // Date of Appointment
                appointment[4],  // Appointment Time
                "COMPLETED"    ,  // Status
                appointment[6]   // Outcome ID
        };

        // Add the outcome entry to the appointment outcome CSV
        apptCsvHelper.addNewOutcome(newOutcome);
        csvHelper.updateApptById(appointmentID, updatedAppointment);

        // Now, check if a medical record exists for the patient
        List<String[]> medicalRecords = medicalrecCsvHelper.readCSV();
        String patientID = appointment[1];

             
        
        // Create the new record ID for this entry
        String newRecordID = getNextRecordID(medicalRecords);

        // to create string variable for doctorID
        String doctorID = appointment[2];

        // Create the new medical record with diagnosis and treatment plan
        String[] newRecord = new String[] {
            newRecordID,        // Generate new Record ID
            diagnosis,          // Diagnosis
            treatmentPlan,      // Treatment Plan
            patientID,    // Patient ID
            doctorID
        };

        // Add the new record to the list
        medicalRecords.add(newRecord);
        // Update the medical records CSV by appending the new record
        medicalrecCsvHelper.updateMedicalRecords(medicalRecords);

        System.out.println("Updated appointment details and added new medical record successfully.");
    }
}


// Helper method to find the next available Record ID
public static String getNextRecordID(List<String[]> medicalRecords) {
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

    public static void viewPatientMedicalRecords(String patientID, String doctorID) {
        // Initialize the MedicalRecordCsvHelper to fetch medical records
        MedicalRecordCsvHelper csvHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalRecords = csvHelper.readCSV();
    
        // Check if records exist for the given patient ID and doctor ID
        boolean recordFound = false;
         //read patient blood type
         PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
         List<String[]> patientRec = patientCsvHelper.readCSV();
         String patientName = null;
         String patientGender = null;
         String patientBloodType = null;
 
 
         // Loop through all rows (skipping the header)
         for (int i = 1; i < patientRec.size(); i++) { // Start from 1 to skip the header row
             String[] row = patientRec.get(i);
             if (row[0].equals(patientID)) {
                 patientName = row[1];
                 patientGender = row[3];
                 patientBloodType = row[4];
                 break; // Exit the loop once a match is found
             }
         }   
    
        // Print the header for the table
        System.out.println("\nPatient Name: " + patientName + " | Gender: "+ patientGender + " | Blood Type: " + patientBloodType);
        System.out.println("+------------+----------------------+--------------------------+");
        System.out.format("| %-10s | %-20s | %-24s |\n",
                "Record ID", "Diagnosis", "Treatment Plan");
        System.out.println("+------------+----------------------+--------------------------+");
        
        
       
        // Iterate through the medical records and check for the patient's records assigned to the doctor
        for (String[] record : medicalRecords) {
            // Ensure the record has valid data and matches the patient ID and doctor ID
            if (record.length >= 5 && record[3].equalsIgnoreCase(patientID) && record[4].equalsIgnoreCase(doctorID)) {
                System.out.format("| %-10s | %-20s | %-24s |\n",
                        record[0],                                // Record ID
                        (record[1].isEmpty() ? "N/A" : record[1]), // Diagnosis
                        (record[2].isEmpty() ? "N/A" : record[2]) // Treatment Plan
                        );
                recordFound = true;
            }
        }
    
        // Close the table or display a message if no records found
        if (recordFound) {
            System.out.println("+------------+----------------------+--------------------------+");
        } else {
            System.out.println("No medical records found for patient ID: " + patientID + " under doctor ID: " + doctorID);
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
            System.out.println("+-----------+------------+------------+-----------+");
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


    public static boolean isPatientAssignedToDoctor(String patientID, String doctorID) 
    {
        // Initialize the MedicalRecordCsvHelper to fetch medical records
        MedicalRecordCsvHelper csvHelper = new MedicalRecordCsvHelper();
        List<String[]> medicalRecords = csvHelper.readCSV();

        // Iterate through the records and check for matching patient ID and doctor ID
        for (String[] record : medicalRecords) {
            if (record.length >= 5 && record[3].equalsIgnoreCase(patientID) && record[4].equalsIgnoreCase(doctorID)) {
                return true; // The patient is assigned to the doctor
            }
        }
        return false; // The patient is not assigned to the doctor
    }

    

}
