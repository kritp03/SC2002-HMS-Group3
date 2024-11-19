package HMS.src.medicalrecordsPDT;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a medical entry that includes diagnosis, treatment, and prescriptions.
 */
public class MedicalEntry {
    private String diagnosis; // The diagnosis for the medical condition
    private String treatment; // The treatment plan for the diagnosis
    private Map<String, String> prescriptions = new HashMap<>(); // Prescriptions with their status

    /**
     * Default constructor for creating an empty medical entry.
     */
    public MedicalEntry() {
    }

    /**
     * Constructor for creating a medical entry with diagnosis and treatment.
     *
     * @param diagnosis The diagnosis for the medical condition.
     * @param treatment The treatment plan for the diagnosis.
     */
    public MedicalEntry(String diagnosis, String treatment) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    /**
     * Sets the diagnosis for the medical entry.
     *
     * @param diagnosis The diagnosis for the medical condition.
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Sets the treatment plan for the medical entry.
     *
     * @param treatment The treatment plan for the diagnosis.
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * Sets the prescriptions for the medical entry.
     *
     * @param prescriptions A map of prescriptions with their statuses.
     */
    public void setPrescriptions(Map<String, String> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     * Adds a new prescription with a default status of "PENDING".
     *
     * @param medicationName The name of the medication to be prescribed.
     */
    public void addPrescription(String medicationName) {
        prescriptions.put(medicationName, "PENDING");
    }

    /**
     * Updates the status of an existing prescription.
     *
     * @param medicationName The name of the medication whose status needs to be updated.
     * @param status         The new status for the prescription.
     */
    public void updatePrescriptionStatus(String medicationName, String status) {
        if (prescriptions.containsKey(medicationName)) {
            prescriptions.put(medicationName, status);
        } else {
            System.out.println("Prescription not found: " + medicationName);
        }
    }

    /**
     * Gets the diagnosis for the medical entry.
     *
     * @return The diagnosis.
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Gets the treatment plan for the medical entry.
     *
     * @return The treatment plan.
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Gets the prescriptions for the medical entry.
     *
     * @return A map of prescriptions with their statuses.
     */
    public Map<String, String> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Returns a string representation of the medical entry.
     *
     * @return A formatted string containing the diagnosis, treatment, and prescriptions.
     */
    @Override
    public String toString() {
        return "Diagnosis: " + diagnosis + "\n" +
                "Treatment: " + treatment + "\n" +
                "Prescriptions: " + prescriptions + "\n";
    }
}

