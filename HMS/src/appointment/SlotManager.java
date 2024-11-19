package HMS.src.appointment;

import HMS.src.io.AppointmentCsvHelper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages time slots for doctors and their appointments.
 * Handles functionalities like initializing slots, setting unavailability, and printing schedules.
 */
public class SlotManager {
    private static Map<String, List<Slot>> doctorSlots = new HashMap<>(); /**< Maps doctor IDs to their respective slots. */
    private static final AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper(); /**< Helper to interact with the appointment CSV file. */

    /**
     * Constructor for SlotManager.
     * Ensures the doctorSlots map is initialized.
     */
    public SlotManager() {
        if (doctorSlots == null) {
            doctorSlots = new HashMap<>();
        }
    }

    /**
     * Initializes slots for a specific doctor based on data from the CSV file.
     *
     * @param doctorID The ID of the doctor whose slots are to be initialized.
     */
    public static void initializeDoctorSlotsFromCSV(String doctorID) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        doctorSlots.putIfAbsent(doctorID, new ArrayList<>());

        for (String[] appointment : appointments) {
            if (appointment.length < 6) continue; // Ensure data integrity

            String apptDoctorID = appointment[2]; // Doctor ID from CSV
            if (!apptDoctorID.equalsIgnoreCase(doctorID)) continue; // Skip if not this doctor

            try {
                LocalDate date = LocalDate.parse(appointment[3], DateTimeFormatter.ofPattern("d/M/yyyy"));
                LocalTime startTime = LocalTime.parse(appointment[4].split("-")[0], DateTimeFormatter.ofPattern("HHmm"));
                LocalDateTime slotTime = LocalDateTime.of(date, startTime);

                Slot slot = new Slot(slotTime);
                slot.setAvailability(false); // Mark as booked based on the appointment
                doctorSlots.get(doctorID).add(slot);

            } catch (Exception e) {
                System.out.println("Error parsing appointment data: " + Arrays.toString(appointment));
            }
        }
    }

    /**
     * Sets a specific slot as unavailable for a given doctor and time.
     *
     * @param doctorID The ID of the doctor.
     * @param dateTime The date and time of the slot to be set as unavailable.
     */
    public static void setUnavailability(String doctorID, LocalDateTime dateTime) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();
        boolean slotFound = false;

        for (String[] appointmentData : appointments) {
            try {
                LocalDateTime slotDateTime = LocalDateTime.parse(appointmentData[0], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

                if (slotDateTime.equals(dateTime)) {
                    appointmentData[1] = Boolean.toString(false); // Set the slot to unavailable
                    slotFound = true;

                    appointmentCsvHelper.addAppointment(appointmentData); // Update the modified slot
                    System.out.println("Slot " + dateTime + " is updated to Unavailable");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error processing slot data: " + Arrays.toString(appointmentData));
            }
        }

        if (!slotFound) {
            System.out.println("Slot is already unavailable or not found");
        }
    }

    /**
     * Prints all slots for a specific doctor.
     *
     * @param doctorID The ID of the doctor whose slots are to be printed.
     */
    public static void printSlots(String doctorID) {
        List<Slot> slots = doctorSlots.get(doctorID);
        if (slots != null) {
            for (Slot slot : slots) {
                System.out.println(slot);
            }
        } else {
            System.out.println("No slots available for doctor " + doctorID);
        }
    }

    /**
     * Retrieves all slots for a specific doctor.
     *
     * @param doctorID The ID of the doctor.
     * @return A list of slots associated with the given doctor.
     */
    public List<Slot> getSlots(String doctorID) {
        return doctorSlots.getOrDefault(doctorID, new ArrayList<>());
    }

    /**
     * Prints the complete schedule for a specific doctor.
     *
     * @param doctorID The ID of the doctor whose schedule is to be printed.
     */
    public static void printFullSchedule(String doctorID) {
        if (!doctorSlots.containsKey(doctorID) || doctorSlots.get(doctorID).isEmpty()) {
            System.out.println("No slots found for Dr. " + doctorID);
            return;
        }

        List<Slot> slots = doctorSlots.get(doctorID);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");

        System.out.println("Complete schedule for Dr. " + doctorID + ":");
        int slotNumber = 1;

        List<String[]> appointments = appointmentCsvHelper.readCSV(); // Read from Appt_List.csv

        for (Slot slot : slots) {
            String date = slot.getDateTime().toLocalDate().format(dateFormatter);
            String startTime = slot.getDateTime().toLocalTime().format(timeFormatter);
            String endTime = slot.getEndDateTime().toLocalTime().format(timeFormatter);

            String appointmentStatus = "AVAILABLE"; // Default status if no matching appointment is found

            if (!slot.isAvailable()) {
                for (String[] appointment : appointments) {
                    if (appointment.length >= 6 
                        && appointment[2].equals(doctorID) 
                        && LocalDate.parse(appointment[3], DateTimeFormatter.ofPattern("d/M/yyyy")).equals(slot.getDateTime().toLocalDate())
                        && appointment[4].split("-")[0].equals(startTime)) {
                        appointmentStatus = appointment[5]; // Use the status from the CSV
                        break;
                    }
                }
            }

            System.out.printf("Slot %d: %s %s-%s (%s)%n", slotNumber++, date, startTime, endTime, appointmentStatus);
        }
    }
}
