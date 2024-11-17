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

public class SlotManager {
        private static Map<String, List<Slot>> doctorSlots = new HashMap<>();
        private static final AppointmentCsvHelper appointmentCsvHelper = new AppointmentCsvHelper();
        
        public SlotManager()
        {
            if (doctorSlots == null) {
                doctorSlots = new HashMap<>();
            }
        }
    
        // Initialize slots for a given doctor for the full year
        public static void initializeDoctorSlotsFromCSV(String doctorID) {
        List<String[]> appointments = appointmentCsvHelper.readCSV();

        // Initialize the slots map if not done
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

    
        // Set availability for a specific slot
        public static void setAvailability(String doctorID, LocalDateTime dateTime, boolean isAvailable) {
            List<Slot> slots = doctorSlots.get(doctorID);
            if (slots != null) {
                for (Slot slot : slots) {
                    if (slot.getDateTime().equals(dateTime)) {
                        slot.setAvailability(isAvailable);
                        System.out.println("Slot " + dateTime + " updated to " + (isAvailable ? "Available" : "Unavailable"));
                        return;
                    }
                }
            }
            System.out.println("Slot " + dateTime + " for doctor " + doctorID + " is unavailable.");
        }
    
        // Print all slots for a specific doctor
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

    // Return all slots for a specific doctor
    public List<Slot> getSlots(String doctorID) {
        return doctorSlots.getOrDefault(doctorID, new ArrayList<>());
    }

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
    
        List<String[]> appointments = new AppointmentCsvHelper().readCSV(); // Read from Appt_List.csv
    
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
    
            // Print the slot in the specified format
            System.out.printf("Slot %d: %s %s-%s (%s)%n", slotNumber++, date, startTime, endTime, appointmentStatus);
        }
    }
}
