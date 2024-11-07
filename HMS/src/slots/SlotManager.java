package HMS.src.slots;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SlotManager {
    private final List<Slot> slots;

    public SlotManager() {
        this.slots = new ArrayList<>();
        createTimeSlots();
    }

    // time slots from 9am -> 5pm, 1 hour intervals
    private void createTimeSlots() {
        LocalTime startTime = LocalTime.of(9, 0);
        for (int i = 0; i < 8; i++) {
            slots.add(new Slot(startTime));
            startTime = startTime.plusHours(1);  // 1 hour interval
        }
    }

    public void setAvailability(LocalTime startTime, boolean isAvailable) {
        for (Slot slot : slots) {
            if (slot.getStartTime().equals(startTime)) {
                slot.setAvailability(isAvailable);
                System.out.println("Slot " + startTime + " updated to " + (isAvailable ? "Available" : "Unavailable"));
                return;
            }
        }
        System.out.println("Slot " + startTime + " not found.");
    }

    // Print all slots (useful for displaying available slots)
    public void printSlots() {
        for (Slot slot : slots) {
            System.out.println(slot);
        }
    }

    // Return list of all slots
    public List<Slot> getSlots() {
        return slots;
    }
}
