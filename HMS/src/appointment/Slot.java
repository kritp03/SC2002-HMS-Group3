package HMS.src.appointment;

import java.time.LocalDateTime;

public class Slot {
    private final LocalDateTime dateTime; // Start time including date
    private final LocalDateTime endDateTime;
    private boolean isAvailable;  // true if slot is available, false if booked

    // Constructor
    public Slot(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.endDateTime = dateTime.plusHours(1); // 1 hour interval
        this.isAvailable = true;  // Initially, the slot is available
    }

    // Getters
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
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
        return "Slot: " + dateTime + " - " + endDateTime + (isAvailable ? " (Available)" : " (Booked)");
    }
}