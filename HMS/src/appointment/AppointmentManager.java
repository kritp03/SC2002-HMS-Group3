package HMS.src.appointment;

import HMS.src.io.ApptCsvHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages appointments in the system, including viewing appointment outcomes and managing appointment records.
 */
public class AppointmentManager {
    ApptCsvHelper apptCsvHelper = new ApptCsvHelper();
    private static final Map<String, Appointment> allAppointments = new HashMap<>();

    /**
     * Views all appointment outcome records from the CSV file.
     * Prints each record in a formatted structure if data exists.
     */
    public void viewApptOutcomeRecord() {
        List<String[]> apptOutcome = apptCsvHelper.readCSV();
    
        if (apptOutcome.isEmpty() || apptOutcome.size() == 1) { // Check if only header or completely empty
            System.out.println("No appointment outcomes found.");
            return;
        }
    
        // Define headers and determine the width of each column based on content
        String[] headers = { "Appointment ID", "Patient ID", "Dr ID", "Date of Appointment", "Appointment Time", "Service", "Medicine Name", "Dosage", "Notes" };
        int[] maxWidths = new int[headers.length];
    
        // Set initial column widths based on header lengths
        for (int i = 0; i < headers.length; i++) {
            maxWidths[i] = headers[i].length();
        }
    
        // Adjust column widths based on data
        for (int i = 1; i < apptOutcome.size(); i++) {
            String[] row = apptOutcome.get(i);
            for (int j = 0; j < row.length; j++) {
                if (row[j] != null && maxWidths[j] < row[j].length()) {
                    maxWidths[j] = row[j].length();
                }
            }
        }
    
        // Print table header line
        System.out.print("+");
        for (int width : maxWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    
        // Print header row
        System.out.print("|");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" %-" + maxWidths[i] + "s |", headers[i]);
        }
        System.out.println();
    
        // Print header underlining
        System.out.print("+");
        for (int width : maxWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    
        // Print each data row
        for (int i = 1; i < apptOutcome.size(); i++) {
            System.out.print("|");
            String[] row = apptOutcome.get(i);
            for (int j = 0; j < row.length; j++) {
                String toPrint = (row[j] == null ? "" : row[j]);
                System.out.printf(" %-" + maxWidths[j] + "s |", toPrint);
            }
            System.out.println();
        }
    
        // End table with a line
        System.out.print("+");
        for (int width : maxWidths) {
            System.out.print("-".repeat(width + 2) + "+");
        }
        System.out.println();
    }

    /**
     * Adds a new appointment to the system.
     * 
     * @param appointment The {@link Appointment} object representing the appointment to add.
     */
    public static void addAppointment(Appointment appointment) {
        allAppointments.put(appointment.getAppointmentID(), appointment);
    }

    /**
     * Retrieves an appointment by its ID.
     * 
     * @param appointmentID The unique identifier of the appointment.
     * @return The {@link Appointment} object if found, otherwise null.
     */
    public static Appointment getAppointmentByID(String appointmentID) {
        return allAppointments.get(appointmentID);
    }
}
