package HMS.src.medicalrecordsPDT;

import HMS.src.appointment.ServiceType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private final String patientID;
    private ServiceType serviceType;
    private final LocalDate appointmentDate;
    private final List<MedicalEntry> entries = new ArrayList<>();

    // Constructor
    public MedicalRecord(String patientID, LocalDate date) {
        this.patientID = patientID;
        this.appointmentDate = date;
    }

    public void addEntry(MedicalEntry entry) 
    {
        entries.add(entry);
        System.out.println("Added entry for patient ID " + patientID);
    }
    // Method to add a new entry with diagnosis and treatment
    public void addEntry(String diagnosis, String treatment) {
        entries.add(new MedicalEntry(diagnosis, treatment));
        System.out.println("Added new entry with diagnosis and treatment.");
    }

    // Method to add a prescription to the latest entry
    public void addPrescriptionToLatestEntry(String medicationName) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).addPrescription(medicationName);
            System.out.println("Prescription added to the latest entry.");
        } else {
            System.out.println("No entries found. Add a diagnosis and treatment first.");
        }
    }

    // Update prescription status in the latest entry
    public void updatePrescriptionStatusInLatestEntry(String medicationName, String status) {
        if (!entries.isEmpty()) {
            entries.get(entries.size() - 1).updatePrescriptionStatus(medicationName, status);
            System.out.println("Updated prescription status in the latest entry.");
        } else {
            System.out.println("No entries found. Cannot update prescription status.");
        }
    }

    // Getters
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getPatientID() {
        return patientID;
    }

    public List<MedicalEntry> getEntries() {
        return entries;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void addDiagnosis(String patientID, String diagnosis) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }
        
        MedicalRecordManager.addEntryToRecord(patientID, diagnosis, "");  // Adds diagnosis with an empty treatment for now
        System.out.println("Diagnosis added to the latest entry for patient ID: " + patientID);
    }


    public void addTreatment(String patientID, String treatment) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null && !record.getEntries().isEmpty()) {
            // Get the latest entry and add treatment
            MedicalRecordManager.addEntryToRecord(patientID, "", treatment);  // Adds treatment with an empty diagnosis for now
            System.out.println("Treatment added to the latest entry for patient ID: " + patientID);
        } else {
            System.out.println("No entries found in the medical record for patient ID: " + patientID);
        }
    }


public void addPrescription(String patientID, String prescription) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }

        MedicalRecordManager.addPrescriptionToLatestEntry(patientID, prescription);
        System.out.println("Prescription added to the latest entry for patient ID: " + patientID);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Medical Record for ").append(patientID).append(":\n");
        sb.append("Service Type: ").append(serviceType != null ? serviceType : "Not set").append("\n");
        sb.append("Appointment Date: ").append(appointmentDate != null ? appointmentDate : "Not set").append("\n");

        for (int i = 0; i < entries.size(); i++) {
            sb.append("Entry ").append(i + 1).append(":\n");
            sb.append(entries.get(i).toString());
        }
        
        return sb.toString();
    }
}
