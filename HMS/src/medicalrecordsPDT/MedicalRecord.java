package HMS.src.medicalrecordsPDT;

import HMS.src.appointment.ServiceType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a medical record for a patient, including service type, appointment date, and medical entries.
 */
public class MedicalRecord {
    private final String patientID; // The ID of the patient
    private final String recordID; // Unique identifier for each record
    private ServiceType serviceType; // The type of service (e.g., Consultation, Surgery)
    private final LocalDate appointmentDate; // The date of the appointment
    private final List<MedicalEntry> entries = new ArrayList<>(); // List of medical entries for this record

    /**
     * Constructor to initialize a medical record.
     *
     * @param recordID       The unique record ID.
     * @param patientID      The ID of the patient.
     * @param date           The date of the appointment.
     */
    public MedicalRecord(String recordID, String patientID, LocalDate date) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.appointmentDate = date;
    }

    /**
     * Adds a new medical entry to the record.
     *
     * @param entry The medical entry to add.
     */
    public void addEntry(MedicalEntry entry) {
        entries.add(entry);
        System.out.println("Added entry for patient ID " + patientID);
    }

    /**
     * Retrieves the latest medical entry from the record.
     *
     * @return The latest medical entry, or null if there are no entries.
     */
    public MedicalEntry getLatestEntry() {
        if (!entries.isEmpty()) {
            return entries.get(entries.size() - 1);
        }
        return null;
    }

    /**
     * Gets the record ID.
     *
     * @return The record ID.
     */
    public String getRecordID() {
        return recordID;
    }

    /**
     * Gets the patient ID associated with this record.
     *
     * @return The patient ID.
     */
    public String getPatientID() {
        return patientID;
    }

    /**
     * Gets all medical entries in this record.
     *
     * @return A list of medical entries.
     */
    public List<MedicalEntry> getEntries() {
        return entries;
    }

    /**
     * Gets the service type for the record.
     *
     * @return The service type.
     */
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * Sets the service type for the record.
     *
     * @param serviceType The service type to set.
     */
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Gets the appointment date.
     *
     * @return The appointment date.
     */
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Aggregates and retrieves all diagnoses from the medical entries.
     *
     * @return A string containing all diagnoses.
     */
    public String getDiagnosis() {
        StringBuilder diagnoses = new StringBuilder();
        for (MedicalEntry entry : entries) {
            if (entry.getDiagnosis() != null) {
                diagnoses.append(entry.getDiagnosis()).append("; ");
            }
        }
        return diagnoses.toString().trim();
    }

    /**
     * Aggregates and retrieves all treatments from the medical entries.
     *
     * @return A string containing all treatments.
     */
    public String getTreatment() {
        StringBuilder treatments = new StringBuilder();
        for (MedicalEntry entry : entries) {
            if (entry.getTreatment() != null) {
                treatments.append(entry.getTreatment()).append("; ");
            }
        }
        return treatments.toString().trim();
    }

    /**
     * Aggregates and retrieves all prescriptions from the medical entries.
     *
     * @return A string containing all prescriptions.
     */
    public String getPrescriptions() {
        StringBuilder prescriptions = new StringBuilder();
        for (MedicalEntry entry : entries) {
            for (String prescription : entry.getPrescriptions().keySet()) {
                prescriptions.append(prescription).append(", ");
            }
        }
        return prescriptions.toString().replaceAll(", $", ""); // Remove trailing comma
    }

    /**
     * Adds a diagnosis to the latest entry, or creates a new entry if none exist.
     *
     * @param diagnosis The diagnosis to add.
     */
    public void addDiagnosis(String diagnosis) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).setDiagnosis(diagnosis);
        } else {
            MedicalEntry newEntry = new MedicalEntry();
            newEntry.setDiagnosis(diagnosis);
            entries.add(newEntry);
        }
    }

    /**
     * Adds a prescription to the latest entry, or creates a new entry if none exist.
     *
     * @param prescription The prescription to add.
     */
    public void addPrescription(String prescription) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).addPrescription(prescription);
        } else {
            MedicalEntry newEntry = new MedicalEntry();
            newEntry.addPrescription(prescription);
            entries.add(newEntry);
        }
    }

    /**
     * Adds a treatment to the latest entry, or creates a new entry if none exist.
     *
     * @param treatment The treatment to add.
     */
    public void addTreatment(String treatment) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).setTreatment(treatment);
        } else {
            MedicalEntry newEntry = new MedicalEntry();
            newEntry.setTreatment(treatment);
            entries.add(newEntry);
        }
    }

    /**
     * Returns a string representation of the medical record.
     *
     * @return A string containing record details and all entries.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Medical Record for ").append(patientID).append(":\n");
        sb.append("Service Type: ").append(serviceType != null ? serviceType : "Not set").append("\n");
        sb.append("Appointment Date: ").append(appointmentDate != null ? appointmentDate : "Not set").append("\n");
        sb.append("Record ID: ").append(recordID).append("\n");
        for (int i = 0; i < entries.size(); i++) {
            sb.append("Entry ").append(i + 1).append(":\n");
            sb.append(entries.get(i).toString());
        }
        return sb.toString();
    }
}
