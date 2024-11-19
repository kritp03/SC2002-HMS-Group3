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

        if (apptOutcome.isEmpty()) {
            System.out.println("No appointment outcomes found.");
            return;
        }

        // Loop through and print each outcome record
        for (int i = 1; i < apptOutcome.size(); i++) {
            String[] row = apptOutcome.get(i);
            System.out.println("Appointment ID: " + row[0]);
            System.out.println("--------------------");
            System.out.println("Patient ID: " + row[1]);
            System.out.println("Dr ID: " + row[2]);
            System.out.println("Date of Appointment: " + row[3]);
            System.out.println("Appointment Time: " + row[4]);
            System.out.println("Service: " + row[5]);
            System.out.println("Medicine Name: " + row[6]);
            System.out.println("Dosage: " + row[7]);
            System.out.println("Notes: " + row[8]);
            System.out.println();
        }
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
