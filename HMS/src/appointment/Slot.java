package HMS.src.appointment;

import java.time.LocalDateTime;

/**
 * Represents a time slot for appointments.
 * Each slot is defined by a start time, an end time, and its availability status.
 */
public class Slot {
    private final LocalDateTime dateTime; /**< Start time of the slot, including date. */
    private final LocalDateTime endDateTime; /**< End time of the slot, calculated as one hour after the start time. */
    private boolean isAvailable; /**< Indicates whether the slot is available for booking (true) or already booked (false). */

    /**
     * Constructor to initialize a time slot with a start time.
     * The slot duration is automatically set to one hour.
     *
     * @param dateTime The start time of the slot, including the date.
     */
    public Slot(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        this.endDateTime = dateTime.plusHours(1); // 1-hour interval
        this.isAvailable = true; // Initially, the slot is available
    }

    /**
     * Gets the start time of the slot.
     *
     * @return The start time of the slot as a {@code LocalDateTime}.
     */
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets the end time of the slot.
     *
     * @return The end time of the slot as a {@code LocalDateTime}.
     */
    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    /**
     * Checks whether the slot is available for booking.
     *
     * @return {@code true} if the slot is available, {@code false} if it is booked.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Sets the availability status of the slot.
     *
     * @param isAvailable {@code true} to mark the slot as available, {@code false} to mark it as booked.
     */
    public void setAvailability(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Provides a string representation of the slot, including its start and end times and availability status.
     *
     * @return A formatted string describing the slot.
     */
    @Override
    public String toString() {
        return "Slot: " + dateTime + " - " + endDateTime + (isAvailable ? " (Available)" : " (Booked)");
    }
}
