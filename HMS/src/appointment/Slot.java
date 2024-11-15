package HMS.src.appointment;

import java.time.LocalTime;


public class Slot {
    private final LocalTime startTime;
    private final LocalTime endTime;
    private boolean isAvailable;  // true if slot is available, false if booked

    // Constructor
    public Slot(LocalTime startTime) {
        this.startTime = startTime;
        this.endTime = startTime.plusHours(1); // 1 hour interval
        this.isAvailable = true;  // Initially, the slot is available
    }

    // Getters
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Set availability of the slot (available or not)
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    @Override
    public String toString() {
        return "Slot: " + startTime + " - " + endTime + (isAvailable ? " (Available)" : " (Booked)");
    }
}

