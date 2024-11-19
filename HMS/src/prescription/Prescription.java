package HMS.src.prescription;

/**
 * Represents a prescription for a patient, including details such as the ID, name, quantity, and status.
 */
public class Prescription {
    private String prescriptionID; // Unique identifier for the prescription
    private String name; // Name of the medication
    private int quantity; // Quantity prescribed
    private PrescriptionStatus status; // Status of the prescription (e.g., PENDING, DISPENSED)

    /**
     * Constructs a new Prescription object.
     *
     * @param prescriptionID The unique ID of the prescription.
     * @param name           The name of the medication.
     * @param quantity       The quantity prescribed.
     * @param status         The status of the prescription.
     */
    public Prescription(String prescriptionID, String name, int quantity, PrescriptionStatus status) {
        this.prescriptionID = prescriptionID;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
    }

    /**
     * Gets the unique ID of the prescription.
     *
     * @return The prescription ID.
     */
    public String getPrescriptionID() {
        return prescriptionID;
    }

    /**
     * Sets the unique ID of the prescription.
     *
     * @param prescriptionID The new prescription ID.
     */
    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    /**
     * Gets the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medication.
     *
     * @param name The new name of the medication.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the quantity of medication prescribed.
     *
     * @return The quantity of medication.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of medication prescribed.
     *
     * @param quantity The new quantity of medication.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the status of the prescription.
     *
     * @return The prescription status.
     */
    public PrescriptionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the prescription.
     *
     * @param status The new prescription status.
     */
    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
}

