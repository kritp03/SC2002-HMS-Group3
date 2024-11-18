package HMS.src.user;

import HMS.src.io.AppointmentCsvHelper;
import HMS.src.io.ApptCsvHelper;
import HMS.src.io.AvailabilityCsvHelper;
import HMS.src.io.MedicalRecordCsvHelper;
import HMS.src.io.PatientCsvHelper;
import HMS.src.utils.SessionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PatientManager {
    private final PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
    private final MedicalRecordCsvHelper medicalRecordCsvHelper = new MedicalRecordCsvHelper();
    private final AvailabilityCsvHelper availabilityCsvHelper = new AvailabilityCsvHelper();
    private final AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper();
    private final Scanner scanner = new Scanner(System.in);

    private String getPatientID() {
        String patientID = SessionManager.getCurrentUserID();
        if (patientID == null || !"Patient".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            throw new IllegalStateException("No patient is logged in.");
        }
        return patientID;
    }

    public void displayPatientInfo() {
        String patientID = getPatientID(); // Fetch patientID dynamically
        List<String[]> patients = patientCsvHelper.readCSV();
        boolean foundPatient = false;
        for (String[] patient : patients) {
            if (patient.length > 0 && patient[0].equalsIgnoreCase(patientID)) {
                foundPatient = true;
                System.out.println("\nPatient Information:");
                System.out.println("=======================");
                System.out.println("ID: " + patient[0]);
                System.out.println("Name: " + patient[1]);
                System.out.println("DOB: " + patient[2]);
                System.out.println("Gender: " + patient[3]);
                System.out.println("Blood Type: " + patient[4]);
                System.out.println("Email: " + patient[5]);
                System.out.println("Phone: " + patient[6]);
                System.out.println("Address: " + patient[7]);
                System.out.println("Next of Kin: " + patient[8]);
                System.out.println("Next of Kin Phone: " + patient[9]);
                break;
            }
        }
        if (!foundPatient) {
            System.out.println("No patient found with ID: " + patientID);
        }
    }

    public void displayMedicalRecords() {
        String patientID = getPatientID(); // Fetch patientID dynamically
        List<String[]> medicalRecords = medicalRecordCsvHelper.readCSV();

        boolean recordFound = false;
        System.out.println("Medical Records for Patient ID: " + patientID);
        System.out.println("================================================================");

        for (String[] record : medicalRecords) {
            if (record.length >= 5 && record[4].equalsIgnoreCase(patientID)) {
                System.out.println("Record ID: " + record[0]);
                System.out.println("Diagnosis: " + (record[1].isEmpty() ? "N/A" : record[1]));
                System.out.println("Treatment Plan: " + (record[2].isEmpty() ? "N/A" : record[2]));
                System.out.println("Prescriptions: " + (record[3].isEmpty() ? "N/A" : record[3]));
                System.out.println("----------------------------------------------------------------");
                recordFound = true;
            }
        }

        if (!recordFound) {
            System.out.println("No medical records found for patient ID: " + patientID);
        }
    }

    public void showPatientAndRecords() {
        try {
            displayPatientInfo();
            displayMedicalRecords();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updatePatientContactInfo() {
        String patientID = getPatientID();
        List<String[]> patients = patientCsvHelper.readCSV();
        String[] patientToUpdate = null;

        for (String[] patient : patients) {
            if (patient.length > 0 && patient[0].equalsIgnoreCase(patientID)) {
                patientToUpdate = patient;
                break;
            }
        }

        if (patientToUpdate == null) {
            System.out.println("No patient found with ID: " + patientID);
            return;
        }

        System.out.println("Current email: " + patientToUpdate[5]);
        System.out.println("Current phone number: " + patientToUpdate[6]);

        System.out.print("Do you want to update email (E) or phone number (P)? ");
        String choice = scanner.nextLine().trim().toUpperCase();

        if (!choice.equals("E") && !choice.equals("P")) {
            System.out.println("Invalid choice. Please enter 'E' for email or 'P' for phone number.");
            return;
        }

        String newContactInfo = "";
        boolean validInput = false;
        while (!validInput) {
            if (choice.equals("E")) {
                System.out.print("Enter new email: ");
            } else {
                System.out.print("Enter new phone number: ");
            }
            newContactInfo = scanner.nextLine().trim();
            if (choice.equals("E") && newContactInfo.matches("^(.+)@(.+)\\.(.+)$")) {
                validInput = true;
                patientToUpdate[5] = newContactInfo;
            } else if (choice.equals("P") && newContactInfo.matches("^[8|9]\\d{7}$")) {
                validInput = true;
                patientToUpdate[6] = newContactInfo;
            } else {
                System.out.println("Invalid. Please provide a valid input.");
            }
        }

        patientCsvHelper.updateEntry(patientID, patientToUpdate); // Update the entry in CSV

        System.out.println("\nContact information updated successfully!");
        displayPatientInfo(); // Display updated patient info
    }

    public void viewAvailableSlots() {
        List<String[]> availabilityData = availabilityCsvHelper.readCSV();

        if (availabilityData.isEmpty()) {
            System.out.println("No available slots found.");
            return;
        }

        System.out.println("+----------+----------------------+---------------+-------------+");
        System.out.format("| %-8s | %-20s | %-12s | %-11s |\n", "Slot No.", "Doctor ID", "Date", "Time Slot");
        System.out.println("+----------+----------------------+---------------+-------------+");

        int slotNumber = 1; // Initialize slot number
        for (String[] row : availabilityData) {
            if (row.length >= 3) { // Ensure row has at least doctorID, date, and time
                System.out.format("| %-8d | %-20s | %-12s | %-11s |\n", 
                                  slotNumber++, row[0], row[1], row[2]);
            }
        }

        System.out.println("+----------+----------------------+---------------+-------------+\n");
    }

    public void scheduleAppointment(String patientID) {
        List<String[]> availabilityData = availabilityCsvHelper.readCSV();

        if (availabilityData.isEmpty()) {
            System.out.println("No available slots found.");
            return;
        }

        // Display available slots in tabular format
        System.out.println("+----------+----------------------+---------------+-------------+");
        System.out.format("| %-8s | %-20s | %-12s | %-11s |\n", "Slot No.", "Doctor ID", "Date", "Time Slot");
        System.out.println("+----------+----------------------+---------------+-------------+");

        int slotNumber = 1; // Initialize slot number
        for (String[] row : availabilityData) {
            if (row.length >= 3) { // Ensure row has at least doctorID, date, and time
                System.out.format("| %-8d | %-20s | %-12s | %-11s |\n",
                                  slotNumber++, row[0], row[1], row[2]);
            }
        }

        System.out.println("+----------+----------------------+---------------+-------------+\n");

        // Prompt the patient to select a slot
        int selectedSlot = -1;
        while (selectedSlot < 1 || selectedSlot > availabilityData.size()) {
            System.out.print("Enter the slot number to schedule an appointment: ");
            try {
                selectedSlot = Integer.parseInt(scanner.nextLine().trim());
                if (selectedSlot < 1 || selectedSlot > availabilityData.size()) {
                    System.out.println("Invalid slot number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        // Retrieve the selected slot data
        String[] selectedSlotData = availabilityData.get(selectedSlot - 1);

        // Remove the selected slot from Availability_List.csv
        availabilityData.remove(selectedSlot - 1);
        availabilityCsvHelper.writeEntries(availabilityData);

        // Add the appointment to Appt_List.csv as PENDING
        String[] appointmentEntry = {
            "AP" + String.format("%03d", appointmentCsvHelper.readCSV().size()), // Generate unique appointment ID
            patientID,
            selectedSlotData[0], // Doctor ID
            selectedSlotData[1], // Date
            selectedSlotData[2], // Time
            "PENDING",           // Status
            ""                   // Outcome
        };
        appointmentCsvHelper.updateAppointment(appointmentEntry);

        System.out.println("Appointment successfully scheduled!");
        System.out.println("Doctor: " + selectedSlotData[0]);
        System.out.println("Date: " + selectedSlotData[1]);
        System.out.println("Time: " + selectedSlotData[2]);
    }

    public void viewScheduledAppointments(String patientID) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();

        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");
        System.out.format("| %-8s | %-10s | %-20s | %-12s | %-11s | %-11s |\n", 
                      "Appt ID", "Status", "Doctor ID", "Date", "Time Slot", "Outcome");
        System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");

        boolean foundAppointments = false;

        for (String[] appointment : appointments) {
            try {
                // Ensure each row has the required fields and matches the patient ID
                if (appointment.length >= 7 && appointment[1].equalsIgnoreCase(patientID)) {
                    foundAppointments = true;

                    // Default handling for empty fields
                    String status = appointment[5].isEmpty() ? "N/A" : appointment[5];
                    String outcome = appointment[6].isEmpty() ? "N/A" : appointment[6];

                    System.out.format("| %-8s | %-10s | %-20s | %-12s | %-11s | %-11s |\n", 
                                  appointment[0], // Appointment ID
                                  status,         // Status (Pending/Confirmed)
                                  appointment[2], // Doctor ID
                                  appointment[3], // Date
                                  appointment[4], // Time Slot
                                  outcome         // Outcome
                    );
                }
            }catch (Exception e) {
                System.out.println("Error processing row: " + String.join(", ", appointment));
                System.out.println("Details: " + e.getMessage());
            }
        }

        if (!foundAppointments) {
            System.out.println("No appointments found for Patient ID: " + patientID);
        }

        System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+\n");
    }

    public void cancelAppointment() {
    List<String[]> appointments = appointmentCsvHelper.readCSV();
    List<String[]> availabilitySlots = availabilityCsvHelper.readCSV();
    String patientID = getPatientID();

    // Filter appointments for the logged-in patient
    List<String[]> patientAppointments = new ArrayList<>();
    for (String[] appointment : appointments) {
        if (appointment.length >= 7 && appointment[1].trim().equalsIgnoreCase(patientID)) {
            patientAppointments.add(appointment);
        }
    }

    if (patientAppointments.isEmpty()) {
        System.out.println("You have no appointments to cancel.");
        return;
    }

    // Print the appointments with an extra "Slot Number" column
    System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");
    System.out.format("| %-8s | %-10s | %-20s | %-13s | %-11s | %-11s |\n", 
                      "Slot No.", "Appt ID", "Doctor ID", "Date", "Time Slot", "Status");
    System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");

    for (int i = 0; i < patientAppointments.size(); i++) {
        String[] appointment = patientAppointments.get(i);
        System.out.format("| %-8d | %-10s | %-20s | %-13s | %-11s | %-11s |\n", 
                          i + 1, appointment[0], appointment[2], appointment[3], appointment[4], appointment[5]);
    }
    System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+\n");

    // Prompt for slot number
    int slotNumber = -1;
    boolean validInput = false;
    while (!validInput) {
        try {
            System.out.print("Enter the slot number of the appointment you wish to cancel: ");
            slotNumber = Integer.parseInt(scanner.nextLine().trim());
            if (slotNumber < 1 || slotNumber > patientAppointments.size()) {
                System.out.println("Invalid slot number. Please select a valid number from the table.");
            } else {
                validInput = true;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
    }

    // Get the appointment to cancel
    String[] canceledAppointment = patientAppointments.get(slotNumber - 1);
    appointments.remove(canceledAppointment);

    // Add the canceled slot back to availability
    String[] newSlot = {
        canceledAppointment[2], // Doctor ID
        canceledAppointment[3], // Date
        canceledAppointment[4]  // Time
    };
    availabilitySlots.add(newSlot);

    // Update the CSV files
    appointmentCsvHelper.writeEntries(appointments);
    availabilityCsvHelper.writeEntries(availabilitySlots);

    System.out.println("Appointment " + canceledAppointment[0] + " has been successfully canceled.");
}


    public int getAppointmentToReschedule(String patientID) 
    {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        List<String[]> patientAppointments = new ArrayList<>();

        // Filter appointments belonging to the patient
        for (String[] appointment : appointments) {
            if (appointment.length >= 2 && appointment[1].equalsIgnoreCase(patientID)) {
                patientAppointments.add(appointment);
            }
        }

        if (patientAppointments.isEmpty()) {
            System.out.println("No scheduled appointments found for patient ID: " + patientID);
            return -1;
        }

        // Display appointments in tabular format
        System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");
        System.out.format("| %-8s | %-10s | %-20s | %-13s | %-11s | %-11s |\n", "Slot No.", "Appt ID", "Doctor ID", "Date", "Time", "Status");
        System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");

        int slotNumber = 1;
        for (String[] appointment : patientAppointments) {
            System.out.format("| %-8d | %-10s | %-20s | %-13s | %-11s | %-11s |\n",
                    slotNumber++, appointment[0], appointment[2], appointment[3], appointment[4], appointment[5]);
        }
        System.out.println("+----------+------------+----------------------+---------------+-------------+-------------+");

        // Prompt user to select the appointment by slot number
        System.out.print("Enter the slot number of the appointment to reschedule: ");
        int selectedSlot = new Scanner(System.in).nextInt();

        if (selectedSlot < 1 || selectedSlot > patientAppointments.size()) {
            System.out.println("Error: Invalid slot number.");
            return -1;
        }

        return selectedSlot - 1; // Return index in the list
    }

    public boolean rescheduleAppointment(int appointmentIndex) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        List<String[]> availability = availabilityCsvHelper.readCSV();

        if (appointmentIndex < 0 || appointmentIndex >= appointments.size()) {
            System.out.println("Error: Invalid appointment index.");
            return false;
        }

        String[] appointmentToReschedule = appointments.get(appointmentIndex);

        // Add the slot back to Availability_List
        String[] newAvailability = {appointmentToReschedule[2], appointmentToReschedule[3], appointmentToReschedule[4]};
        availability.add(newAvailability);

        // Remove the appointment from Appt_List
        appointments.remove(appointmentIndex);

        // Write updated data back to CSV files
        availabilityCsvHelper.writeEntries(availability);
        appointmentCsvHelper.writeEntries(appointments);

        return true;
    }

    public void viewPastAppointmentOutcomes(String patientID) {
        ApptCsvHelper apptOutcomeHelper = new ApptCsvHelper();
        List<String[]> outcomeData = apptOutcomeHelper.readCSV();

        // Filter outcomes belonging to the patient
        List<String[]> patientOutcomes = new ArrayList<>();
        for (String[] record : outcomeData) {
            if (record.length >= 2 && record[1].equalsIgnoreCase(patientID)) 
            {
                patientOutcomes.add(record);
            }
        }

        // If no outcomes found, print a message and return
        if (patientOutcomes.isEmpty()) {
            System.out.println("No past appointment outcomes found for patient ID: " + patientID);
            return;
        }

    // Define column headers
        String[] headers = {
            "Appt ID", "Patient ID", "Dr ID", "Date", "Time", 
            "Service", "Medicine Name", "Dosage", "Notes"
        };

        // Determine column widths dynamically
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length(); // Start with header length
        }
        for (String[] record : patientOutcomes) {
            for (int i = 0; i < record.length; i++) {
            columnWidths[i] = Math.max(columnWidths[i], record[i].length());
            }
        }

        // Adjust column widths for empty values
        for (int i = 0; i < columnWidths.length; i++) {
            columnWidths[i] = Math.max(columnWidths[i], 2); // Minimum width for "-"
        }

        // Print header
        StringBuilder separator = new StringBuilder("+");
        for (int width : columnWidths) {
        separator.append("-".repeat(width + 2)).append("+");
        }
        System.out.println(separator);
        System.out.print("|");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" %-"+columnWidths[i]+"s |", headers[i]);
        }
        System.out.println();
        System.out.println(separator);

        // Print rows
        for (String[] record : patientOutcomes) {
            System.out.print("|");
            for (int i = 0; i < headers.length; i++) {
                String value = i < record.length ? record[i] : "-";
                System.out.printf(" %-"+columnWidths[i]+"s |", value.isEmpty() ? "-" : value);
            }
            System.out.println();
        }
        System.out.println(separator);
    }
}
