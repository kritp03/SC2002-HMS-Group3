package HMS.src.medicalrecordsPDT;

import HMS.src.appointment.ServiceType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private final String patientID;
    private final String recordID; // Unique identifier for each record
    private ServiceType serviceType;
    private final LocalDate appointmentDate;
    private final List<MedicalEntry> entries = new ArrayList<>();

    // Constructor
    public MedicalRecord(String recordID, String patientID, LocalDate date) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.appointmentDate = date;
    }

    public void addEntry(MedicalEntry entry) {
        entries.add(entry);
        System.out.println("Added entry for patient ID " + patientID);
    }

    // Getters for Record Details

    public MedicalEntry getLatestEntry() {
        if (!entries.isEmpty()) {
            return entries.get(entries.size() - 1);
        }
        return null;
    }
    
    public String getRecordID() {
        return recordID;
    }

    public String getPatientID() {
        return patientID;
    }

    public List<MedicalEntry> getEntries() {
        return entries;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getDiagnosis() {
        // Aggregate diagnoses from entries
        StringBuilder diagnoses = new StringBuilder();
        for (MedicalEntry entry : entries) {
            if (entry.getDiagnosis() != null) {
                diagnoses.append(entry.getDiagnosis()).append("; ");
            }
        }
        return diagnoses.toString().trim();
    }

    public String getTreatment() {
        // Aggregate treatments from entries
        StringBuilder treatments = new StringBuilder();
        for (MedicalEntry entry : entries) {
            if (entry.getTreatment() != null) {
                treatments.append(entry.getTreatment()).append("; ");
            }
        }
        return treatments.toString().trim();
    }

    public String getPrescriptions() {
        // Aggregate prescriptions from entries
        StringBuilder prescriptions = new StringBuilder();
        for (MedicalEntry entry : entries) {
            for (String prescription : entry.getPrescriptions().keySet()) {
                prescriptions.append(prescription).append(", ");
            }
        }
        return prescriptions.toString().replaceAll(", $", ""); // Remove trailing comma
    }

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

    public void addDiagnosis(String diagnosis) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).setDiagnosis(diagnosis);
        } else {
            MedicalEntry newEntry = new MedicalEntry();
            newEntry.setDiagnosis(diagnosis);
            entries.add(newEntry);
        }
    }

    public void addPrescription(String prescription) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).addPrescription(prescription);
        } else {
            MedicalEntry newEntry = new MedicalEntry();
            newEntry.addPrescription(prescription);
            entries.add(newEntry);
        }
    }

    public void addTreatment(String treatment) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).setTreatment(treatment);
        } else {
            MedicalEntry newEntry = new MedicalEntry();
            newEntry.setTreatment(treatment);
            entries.add(newEntry);
        }
    }

}
