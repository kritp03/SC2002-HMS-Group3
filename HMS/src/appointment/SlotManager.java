package HMS.src.appointment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlotManager {
    private static Map<String, List<Slot>> doctorSlots; // Map doctor to their slots
    
        public SlotManager() {
            SlotManager.doctorSlots = new HashMap<>();
        }
    
        // Initialize slots for a given doctor for the full year
        public void initializeDoctorSlots(String doctorID) {
            List<Slot> slots = new ArrayList<>();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusYears(1);
    
            while (!startDate.isAfter(endDate)) {
                LocalTime startTime = LocalTime.of(9, 0);
                for (int i = 0; i < 8; i++) {
                    slots.add(new Slot(startDate.atTime(startTime)));
                    startTime = startTime.plusHours(1); // 1 hour interval
                }
                startDate = startDate.plusDays(1);
            }
    
            doctorSlots.put(doctorID, slots);
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
}
