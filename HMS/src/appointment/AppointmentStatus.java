package HMS.src.appointment;

/**
 * Enum representing the status of an appointment.
 * Each status has an associated color for display purposes.
 */
public enum AppointmentStatus {
    PENDING,    /**< Indicates the appointment is pending and awaiting confirmation. */
    CONFIRMED,  /**< Indicates the appointment has been confirmed. */
    CANCELLED,  /**< Indicates the appointment has been cancelled. */
    DECLINED,   /**< Indicates the appointment request was declined. */
    COMPLETED;  /**< Indicates the appointment has been completed. */

    /**
     * Displays the appointment status with associated color.
     * Colors are applied using ANSI escape codes for terminal outputs.
     * 
     * @return A string representation of the status with its associated color.
     */
    public String showStatusByColor() {
        switch (this) {
            case PENDING:
                return "\u001B[33m" + this + "\u001B[0m"; // Yellow
            case CONFIRMED:
                return "\u001B[32m" + this + "\u001B[0m"; // Green
            case CANCELLED:
                return "\u001B[31m" + this + "\u001B[0m"; // Red
            case DECLINED:
                return "\u001B[31m" + this + "\u001B[0m"; // Red
            case COMPLETED:
                return "\u001B[36m" + this + "\u001B[0m"; // Cyan
            default:
                return this.toString();
        }
    }
}

