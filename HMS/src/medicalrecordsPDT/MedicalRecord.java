package HMS.src.medicalrecordsPDT;

import HMS.src.appointment.ServiceType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MedicalRecord {
    private final String patientID;
    private ServiceType serviceType; 
    private final LocalDate appointmentDate;
    private final List<String> diagnoses = new ArrayList<>();
    private final List<String> treatments = new ArrayList<>();
    private final Map<String, String> prescriptions = new HashMap<>();

    // Constructor
    public MedicalRecord(String patientID, LocalDate date) {
        this.patientID = patientID;
        this.appointmentDate = date;
    }

    //adders
    public void addDiagnosis(String diagnosis) {
        diagnoses.add(diagnosis);
    }

    public void addTreatment(String treatment) {
        treatments.add(treatment);
    }

    // for doctor to prescribed medications, initially set to pending 
    public void addPrescription(String medicationName) {
        prescriptions.put(medicationName, "PENDING");
    }

    //pharmacist can call this to update the prescription status after dispensing the medication
    public void updatePrescriptionStatus(String medicationName, String status) {
        if (prescriptions.containsKey(medicationName)) {
            prescriptions.put(medicationName, status);
        } else {
            System.out.println("Prescription not found: " + medicationName);
        }
    }



    // getters
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public String getPatientID() {
        return patientID;
    }

    public List<String> getDiagnoses() {
        return diagnoses;
    }

    public List<String> getTreatments() {
        return treatments;
    }

    public List<String> getPrescriptions() {
        return prescriptions;
    }

    public void setServiceType (ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    // Getter for appointment type
    public ServiceType getServiceType() {
        return serviceType;
    }


    @Override
    public String toString() {
        return "Medical Record for " + patientID + ":\n" +
                "Service Type: " + (serviceType != null ? serviceType : "Not set") + "\n" +
                "Appointment Date: " + (appointmentDate != null ? appointmentDate : "Not set") + "\n" +
                "Diagnoses: " + diagnoses + "\n" +
                "Treatments: " + treatments + "\n" +
                "Prescriptions: " + prescriptions;
    }

}