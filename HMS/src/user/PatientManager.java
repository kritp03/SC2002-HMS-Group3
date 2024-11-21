package HMS.src.user;

import HMS.src.appointment.AppointmentStatus;
import HMS.src.io.AppointmentCsvHelper;
import HMS.src.io.ApptCsvHelper;
import HMS.src.io.AvailabilityCsvHelper;
import HMS.src.io.MedicalRecordCsvHelper;
import HMS.src.io.PatientCsvHelper;
import HMS.src.io.StaffCsvHelper;
import HMS.src.utils.SessionManager;
import HMS.src.utils.ValidationHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * The PatientManager class provides functionalities for managing patient-related operations, 
 * including viewing and updating patient information, managing appointments, and accessing medical records.
 */
public class PatientManager {
    private final PatientCsvHelper patientCsvHelper = new PatientCsvHelper();
    private final MedicalRecordCsvHelper medicalRecordCsvHelper = new MedicalRecordCsvHelper();
    private final AvailabilityCsvHelper availabilityCsvHelper = new AvailabilityCsvHelper();
    private final AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper();
    private static StaffCsvHelper staffCsvHelper = new StaffCsvHelper();
    private final ValidationHelper validationHelper = new ValidationHelper();
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Retrieves the current patient's ID from the session.
     *
     * @return The patient ID of the logged-in patient.
     * @throws IllegalStateException if no patient is logged in.
     */
    private String getPatientID() {
        String patientID = SessionManager.getCurrentUserID();
        if (patientID == null || !"Patient".equalsIgnoreCase(SessionManager.getCurrentUserRole())) {
            throw new IllegalStateException("No patient is logged in.");
        }
        return patientID;
    }
    /**
     * Displays the patient's information, such as name, date of birth, gender, and contact details.
     */
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
    /**
     * Displays the medical records for the currently logged-in patient.
     */
    public void displayMedicalRecords() {
        String patientID = getPatientID(); // Fetch patientID dynamically
        List<String[]> medicalRecords = medicalRecordCsvHelper.readCSV();
    
        boolean recordFound = false;
        System.out.println("Medical Records for Patient ID: " + patientID);
    
        // Define column headers
        String[] headers = {"Record ID", "Diagnosis", "Treatment Plan", "Doctor ID"};
    
        // Determine column widths dynamically
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length(); // Start with header length
        }
        for (String[] record : medicalRecords) {
            if (record.length >= 5 && record[3].equalsIgnoreCase(patientID)) {
                for (int i = 0; i < headers.length; i++) {
                    String value = (i < record.length) ? record[i] : "N/A";
                    columnWidths[i] = Math.max(columnWidths[i], value.length());
                }
                recordFound = true;
            }
        }
    
        // If no records found, print a message and return
        if (!recordFound) {
            System.out.println("No medical records found for Patient ID: " + patientID);
            return;
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
        for (String[] record : medicalRecords) {
            if (record.length >= 5 && record[3].equalsIgnoreCase(patientID)) {
                System.out.print("|");
                System.out.printf(" %-"+columnWidths[0]+"s |", record[0]); // Record ID
                System.out.printf(" %-"+columnWidths[1]+"s |", record[1].isEmpty() ? "N/A" : record[1]); // Diagnosis
                System.out.printf(" %-"+columnWidths[2]+"s |", record[2].isEmpty() ? "N/A" : record[2]); // Treatment Plan
                System.out.printf(" %-"+columnWidths[3]+"s |", record[4]); // Doctor ID
                System.out.println();
            }
        }
        System.out.println(separator);
    }
    
    /**
     * Displays both the patient's information and their medical records.
     */
    public void showPatientAndRecords() {
        try {
            displayPatientInfo();
            displayMedicalRecords();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Allows the patient to update their contact information, including email, phone number, 
     * next of kin name, and next of kin phone number.
     */
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
        System.out.println("Current next of kin name: " + patientToUpdate[8]);
        System.out.println("Current next of kin phone number: " + patientToUpdate[9]);
    
        System.out.print("What would you like to update? (E: Email, P: Phone, N: Next of Kin Name, K: Next of Kin Phone): ");
        String choice = scanner.nextLine().trim().toUpperCase();
    
        if (!choice.equals("E") && !choice.equals("P") && !choice.equals("N") && !choice.equals("K")) {
            System.out.println("Invalid choice. Please enter 'E' for email, 'P' for phone, 'N' for next of kin name, or 'K' for next of kin phone.");
            return;
        }
    
        String newContactInfo = "";
        boolean validInput = false;
    
        while (!validInput) {
            if (choice.equals("E")) {
                System.out.print("Enter new email: ");
                newContactInfo = scanner.nextLine().trim();
                if (newContactInfo.matches("^(.+)@(.+)\\.(.+)$")) {
                    validInput = true;
                    patientToUpdate[5] = newContactInfo;
                } else {
                    System.out.println("Invalid email format. Please try again.");
                }
            } else if (choice.equals("P")) {
                System.out.print("Enter new phone number: ");
                newContactInfo = scanner.nextLine().trim();
                if (newContactInfo.matches("^[8|9]\\d{7}$")) {
                    validInput = true;
                    patientToUpdate[6] = newContactInfo;
                } else {
                    System.out.println("Invalid phone number. It must start with 8 or 9 and have 8 digits. Please try again.");
                }
            } else if (choice.equals("N")) {
                System.out.print("Enter new next of kin name: ");
                newContactInfo = scanner.nextLine().trim();
                if (!newContactInfo.isEmpty()) {
                    validInput = true;
                    patientToUpdate[8] = newContactInfo;
                } else {
                    System.out.println("Name cannot be empty. Please try again.");
                }
            } else if (choice.equals("K")) {
                System.out.print("Enter new next of kin phone number: ");
                newContactInfo = scanner.nextLine().trim();
                if (newContactInfo.matches("^[8|9]\\d{7}$")) {
                    validInput = true;
                    patientToUpdate[9] = newContactInfo;
                } else {
                    System.out.println("Invalid phone number. It must start with 8 or 9 and have 8 digits. Please try again.");
                }
            }
        }
    
        patientCsvHelper.updateEntry(patientID, patientToUpdate); // Update the entry in CSV
    
        System.out.println("\nContact information updated successfully!");
        displayPatientInfo(); // Display updated patient info
    }

    /**
     * Gets the dr name by its ID.
     * @param doctorID The ID of the medicine
     * @return The doctor name if found, null otherwise
     */
    public static String getDrNameByID(String doctorID) {
        List<String[]> staff = staffCsvHelper.readCSV();
        for (int i = 1; i < staff.size(); i++) {
            if (staff.get(i)[0].equalsIgnoreCase(doctorID)) {
                return staff.get(i)[1];
            }
        }
        return null;
    }

    /**
     * Displays available appointment slots for scheduling.
     */
    public void viewAvailableSlots() {
        List<String[]> availabilityData = availabilityCsvHelper.readCSV();
    
        if (availabilityData.size() <= 1) { // Check if there's only a header or empty data
            System.out.println("No available slots found.");
            return;
        }
    
        System.out.println("+----------+----------------------+---------------+-------------+");
        System.out.format("| %-8s | %-20s | %-12s | %-12s |\n", "Slot No.", "Doctor Name", "Date", "Time Slot");
        System.out.println("+----------+----------------------+---------------+-------------+");
    
        int slotNumber = 1; // Initialize slot number
        for (int i = 1; i < availabilityData.size(); i++) { // Start from the second row
            String[] row = availabilityData.get(i);
            if (row.length >= 3) { // Ensure row has at least doctorID, date, and time
                System.out.format("| %-8d | %-20s | %-12s | %-12s |\n", 
                                  slotNumber++, getDrNameByID(row[0]), row[1], row[2]);
            }
        }
    
        System.out.println("+----------+----------------------+---------------+-------------+\n");
    }
    /**
     * Allows the patient to schedule an appointment by selecting an available slot.
     *
     * @param patientID The ID of the logged-in patient.
     */
    public void scheduleAppointment(String patientID) {
        List<String[]> availabilityData = availabilityCsvHelper.readCSV();
    
        if (availabilityData.size() <= 1) { // Check if there's only a header or empty data
            System.out.println("No available slots found.");
            return;
        }
    
        // Display available slots in tabular format
        System.out.println("+----------+----------------------+---------------+-------------+");
        System.out.format("| %-8s | %-20s | %-12s | %-12s |\n", "Slot No.", "Doctor Name", "Date", "Time Slot");
        System.out.println("+----------+----------------------+---------------+-------------+");
    
        int slotNumber = 1; // Initialize slot number
        for (int i = 1; i < availabilityData.size(); i++) { // Start from the second row
            String[] row = availabilityData.get(i);
            if (row.length >= 3) { // Ensure row has at least doctorID, date, and time
                System.out.format("| %-8d | %-20s | %-12s | %-12s |\n", 
                                  slotNumber++, getDrNameByID(row[0]), row[1], row[2]);
            }
        }
    
        System.out.println("+----------+----------------------+---------------+-------------+\n");
    
        // Prompt the patient to select a slot
        int selectedSlot = -1;
        while (selectedSlot < 1 || selectedSlot > availabilityData.size()) { // Adjust for header
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
    
        // Retrieve the selected slot data (adjust index for header)
        String[] selectedSlotData = availabilityData.get(selectedSlot);
    
        // Remove the selected slot from Availability_List.csv
        availabilityData.remove(selectedSlot);
        availabilityCsvHelper.writeEntries(availabilityData);
    
        // Generate a unique appointment ID
        String newAppointmentID = generateUniqueAppointmentID();
    
        // Add the appointment to Appt_List.csv as PENDING
        String[] appointmentEntry = {
            newAppointmentID,        // Unique appointment ID
            patientID,               // Patient ID
            selectedSlotData[0],     // Doctor ID
            selectedSlotData[1],     // Date
            selectedSlotData[2],     // Time
            "PENDING",               // Status
            ""                       // Outcome
        };
        appointmentCsvHelper.addAppointment(appointmentEntry);
    
        System.out.println("Appointment successfully scheduled!");
        System.out.println("Doctor: " + getDrNameByID(selectedSlotData[0]));
        System.out.println("Date: " + selectedSlotData[1]);
        System.out.println("Time: " + selectedSlotData[2]);
    }
    
    /**
     * Generates a unique appointment ID by checking the maximum existing ID in Appt_List.csv.
     * @return A new unique appointment ID.
     */
    private String generateUniqueAppointmentID() {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        int maxID = 0;
    
        // Iterate through existing appointments to find the maximum ID
        for (String[] appointment : appointments) {
            if (appointment.length > 0 && appointment[0].startsWith("AP")) {
                try {
                    int currentID = Integer.parseInt(appointment[0].substring(2));
                    maxID = Math.max(maxID, currentID);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing appointment ID: " + appointment[0]);
                }
            }
        }
    
        // Return the next ID in the format "AP###"
        return "AP" + String.format("%03d", maxID + 1);
    }
    /**
     * Displays all scheduled appointments for the currently logged-in patient.
     *
     * @param patientID The ID of the logged-in patient.
     */
    public void viewScheduledAppointments(String patientID) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
    
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
            return;
        }

        String line = "+------------+------------+-------------+---------------+-------------+";
        System.out.println(line);
        System.out.format("| %-10s | %-10s | %-11s | %-13s | %-11s |%n",
                      "Appt ID", "Status", "Doctor ID", "Date", "Time Slot");
        System.out.println(line);

        boolean foundAppointments = false;

        for (String[] appointment : appointments) {
            try {
                String appointmentID = appointment[0];
                String patientIDFromAppointment = appointment[1];

                // Only show appointments for the current patient
                if (patientIDFromAppointment.equals(patientID)) {
                    AppointmentStatus status = AppointmentStatus.valueOf(appointment[5].toUpperCase());

                    // Only show pending or confirmed appointments
                    if (status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED) {
                        foundAppointments = true;

                        // Use the status color coding and pad it properly
                        String statusColor = padColoredString(status.showStatusByColor(), 10);

                        System.out.format("| %-10s | %s | %-11s | %-13s | %-11s |%n",
                                          appointment[0],    // Appointment ID
                                          statusColor,       // Color-coded Status with padding
                                          appointment[2],    // Doctor ID
                                          appointment[3],    // Date
                                          appointment[4]    // Time Slot
                        );
                    }
                }
            } catch (Exception e) {
                System.out.println("Error processing row: " + String.join(", ", appointment));
                System.out.println("Details: " + e.getMessage());
            }
        }

        if (!foundAppointments) {
            System.out.println("No pending or confirmed appointments found for Patient ID: " + patientID);
        }

        System.out.println(line + "\n");
    }
    
    /**
     * Allows the patient to cancel one of their scheduled appointments.
     * The canceled slot is added back to the availability list.
     */
    public void cancelAppointment() {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        List<String[]> availabilitySlots = availabilityCsvHelper.readCSV();
        String patientID = getPatientID();

        // Filter appointments for the logged-in patient with status PENDING or CONFIRMED
        List<String[]> cancelableAppointments = new ArrayList<>();
        for (String[] appointment : appointments) {
            if (appointment.length >= 6 && appointment[1].trim().equalsIgnoreCase(patientID)) {
                AppointmentStatus status = AppointmentStatus.valueOf(appointment[5].toUpperCase());
                if (status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED) {
                    cancelableAppointments.add(appointment);
                }
            }
        }

        if (cancelableAppointments.isEmpty()) {
            System.out.println("You have no appointments that can be cancelled.");
            return;
        }

        String line = "+------------+------------+------------+------------+------------+------------+";
        System.out.println(line);
        System.out.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %-10s |%n",
                "Slot No.", "Appt ID", "Doctor ID", "Date", "Time", "Status");
        System.out.println(line);

        // Print rows with color-coded status
        for (int i = 0; i < cancelableAppointments.size(); i++) {
            String[] appointment = cancelableAppointments.get(i);
            AppointmentStatus status = AppointmentStatus.valueOf(appointment[5].toUpperCase());
            String coloredStatus = padColoredString(status.showStatusByColor(), 10);

            System.out.format("| %-10d | %-10s | %-10s | %-10s | %-10s | %s |%n",
                i + 1,                // Slot No.
                appointment[0],       // Appt ID
                appointment[2],       // Doctor ID
                appointment[3],       // Date
                appointment[4],       // Time
                coloredStatus         // Status with color and padding
            );
        }
        System.out.println(line);

        // Get valid slot number using ValidationHelper
        int slotNumber = validationHelper.validateIntRange("Enter the slot number of the appointment you wish to cancel: ", 1, cancelableAppointments.size());

        // Get the appointment to cancel
        String[] canceledAppointment = cancelableAppointments.get(slotNumber - 1); // Adjust for 0-based index
        appointments.removeIf(app -> app[0].equals(canceledAppointment[0])); // Remove by matching appointment ID

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

        System.out.println("Appointment " + canceledAppointment[0] + " has been successfully cancelled.");
    }
    

    /**
     * Retrieves the slot number of an appointment the patient wishes to reschedule.
     *
     * @param patientID The ID of the logged-in patient.
     * @return The slot number of the appointment to be rescheduled, or -1 if no appointments are found.
     */
    public int getAppointmentToReschedule(String patientID) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        List<String[]> reschedulableAppointments = new ArrayList<>();
    
        // Filter appointments that are reschedulable (PENDING or CONFIRMED)
        for (String[] appointment : appointments) {
            if (appointment.length >= 6 && appointment[1].equalsIgnoreCase(patientID)) {
                AppointmentStatus status = AppointmentStatus.valueOf(appointment[5].toUpperCase());
                if (status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED) {
                    reschedulableAppointments.add(appointment);
                }
            }
        }
    
        if (reschedulableAppointments.isEmpty()) {
            System.out.println("You have no appointments to reschedule.");
            return -1;
        }
    
        String line = "+------------+------------+------------+------------+------------+------------+";
        System.out.println(line);
        System.out.format("| %-10s | %-10s | %-10s | %-10s | %-10s | %-10s |%n",
                "Slot No.", "Appt ID", "Doctor ID", "Date", "Time", "Status");
        System.out.println(line);

        // Print rows with color-coded status
        for (int i = 0; i < reschedulableAppointments.size(); i++) {
            String[] appointment = reschedulableAppointments.get(i);
            AppointmentStatus status = AppointmentStatus.valueOf(appointment[5].toUpperCase());
            String coloredStatus = padColoredString(status.showStatusByColor(), 10);

            System.out.format("| %-10d | %-10s | %-10s | %-10s | %-10s | %s |%n",
                i + 1,                // Slot No.
                appointment[0],       // Appt ID
                appointment[2],       // Doctor ID
                appointment[3],       // Date
                appointment[4],       // Time
                coloredStatus         // Status with color and padding
            );
        }
        System.out.println(line);

        // Get user input for slot selection
        int selectedSlot = validationHelper.validateIntRange("Enter the slot number of the appointment you wish to reschedule: ", 1, reschedulableAppointments.size());
        return selectedSlot;
    }
    

    /**
     * Reschedules an appointment for the patient by canceling the selected appointment 
     * and adding it back to the availability list.
     *
     * @param patientID The ID of the logged-in patient.
     * @param selectedSlot The slot number of the appointment to reschedule.
     * @return true if the appointment was successfully rescheduled, false otherwise.
     */
    public boolean rescheduleAppointment(String patientID, int selectedSlot) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        List<String[]> reschedulableAppointments = new ArrayList<>();
    
        // Filter appointments that are reschedulable (PENDING or CONFIRMED)
        for (String[] appointment : appointments) {
            if (appointment.length >= 6 && appointment[1].equalsIgnoreCase(patientID)) {
                AppointmentStatus status = AppointmentStatus.valueOf(appointment[5].toUpperCase());
                if (status == AppointmentStatus.PENDING || status == AppointmentStatus.CONFIRMED) {
                    reschedulableAppointments.add(appointment);
                }
            }
        }
    
        if (selectedSlot < 1 || selectedSlot > reschedulableAppointments.size()) {
            System.out.println("Error: Invalid slot number.");
            return false;
        }
    
        // Get the appointment to reschedule based on the selected slot
        String[] appointmentToReschedule = reschedulableAppointments.get(selectedSlot - 1); // Adjust for 0-based index
    
        // Remove the appointment from the main appointments list
        appointments.removeIf(appointment -> appointment[0].equals(appointmentToReschedule[0]));
    
        // Add the canceled slot back to Availability_List
        String[] newAvailability = {
            appointmentToReschedule[2], // Doctor ID
            appointmentToReschedule[3], // Date
            appointmentToReschedule[4]  // Time
        };
        List<String[]> availability = availabilityCsvHelper.readCSV();
        availability.add(newAvailability);
    
        // Write updated data back to CSV files
        availabilityCsvHelper.writeEntries(availability);
        appointmentCsvHelper.writeEntries(appointments);
    
        System.out.println("Appointment " + appointmentToReschedule[0] + " has been successfully canceled and added back to available slots.");
        return true;
    }
    
    /**
     * Displays the outcomes of past appointments for the currently logged-in patient.
     *
     * @param patientID The ID of the logged-in patient.
     */
    public void viewPastAppointmentOutcomes(String patientID) {
        ApptCsvHelper apptOutcomeHelper = new ApptCsvHelper();
        List<String[]> outcomeData = apptOutcomeHelper.readCSV();
    
        // Filter outcomes belonging to the patient
        List<String[]> patientOutcomes = new ArrayList<>();
        for (String[] record : outcomeData) {
            if (record.length >= 2 && record[1].equalsIgnoreCase(patientID)) {
                patientOutcomes.add(record);
            }
        }
    
        // If no outcomes found, print a message and return
        if (patientOutcomes.isEmpty()) {
            System.out.println("No past appointment outcomes found for patient ID: " + patientID);
            return;
        }
    
        // Define column headers without 'Dosage'
        String[] headers = {
            "Appt ID", "Patient ID", "Dr ID", "Date", "Time", 
            "Service", "Medicine Name", "Notes"
        };
    
        // Determine column widths dynamically, except for 'Dosage'
        int[] columnWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length(); // Start with header length
        }
        for (String[] record : patientOutcomes) {
            for (int i = 0; i < record.length; i++) {
                // Ensure we skip dosage which is typically at index 7 in the original setup
                if (i < 7) { // Skip dosage
                    columnWidths[i] = Math.max(columnWidths[i], record[i].length());
                } else if (i > 7) { // Adjust for shifting left due to skipping 'Dosage'
                    columnWidths[i - 1] = Math.max(columnWidths[i - 1], record[i].length());
                }
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
                String value = "-";
                if (i < 7 && i < record.length) { // Skip dosage
                    value = record[i].isEmpty() ? "-" : record[i];
                } else if (i >= 7 && i + 1 < record.length) { // Adjust for shifting left due to skipping 'Dosage'
                    value = record[i + 1].isEmpty() ? "-" : record[i + 1];
                }
                System.out.printf(" %-"+columnWidths[i]+"s |", value);
            }
            System.out.println();
        }
        System.out.println(separator);
    }

    /**
     * Pads a colored string to a specific width, accounting for ANSI color codes.
     * 
     * @param str The colored string containing ANSI codes
     * @param width The desired width of the visible text
     * @return The padded string with proper spacing
     */
    private String padColoredString(String str, int width) {
        String plainText = str.replaceAll("\u001B\\[[;\\d]*m", "");
        int padding = width - plainText.length();
        int lastResetIndex = str.lastIndexOf("\u001B[0m");
        
        if (lastResetIndex >= 0) {
            return str.substring(0, lastResetIndex) + " ".repeat(padding) + str.substring(lastResetIndex);
        } else {
            return String.format("%-" + width + "s", str);
        }
    }
}