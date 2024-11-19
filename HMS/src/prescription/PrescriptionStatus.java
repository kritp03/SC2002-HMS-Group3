package HMS.src.prescription;

/**
 * Enum representing the status of a prescription.
 */
public enum PrescriptionStatus {
    /**
     * Prescription has been dispensed.
     */
    DISPENSED,

    /**
     * Prescription is pending approval or processing.
     */
    PENDING,

    /**
     * Prescription has been cancelled.
     */
    CANCELLED;

    /**
     * Returns a string representation of the status with corresponding color codes.
     * 
     * <ul>
     *     <li><span style="color: green;">DISPENSED</span>: Green</li>
     *     <li><span style="color: yellow;">PENDING</span>: Yellow</li>
     *     <li><span style="color: red;">CANCELLED</span>: Red</li>
     * </ul>
     *
     * @return The status name with its corresponding color.
     */
    public String showStatusByColor() {
        switch (this) {
            case DISPENSED:
                return "\u001B[32m" + this + "\u001B[0m"; // Green color for DISPENSED
            case PENDING:
                return "\u001B[33m" + this + "\u001B[0m"; // Yellow color for PENDING
            case CANCELLED:
                return "\u001B[31m" + this + "\u001B[0m"; // Red color for CANCELLED
            default:
                return this.toString();
        }
    }
}
