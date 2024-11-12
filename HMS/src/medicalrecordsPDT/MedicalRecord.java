package HMS.src.medicalrecordsPDT;

import HMS.src.appointment.ServiceType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class MedicalRecord {
    private final String patientID;
    private ServiceType serviceType; 
    private LocalDate appointmentDate;
    private final List<String> diagnoses = new ArrayList<>();
    private final List<String> treatments = new ArrayList<>();
    private final List<String> prescriptions = new ArrayList<>();

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

    public void addPrescription(String prescription) {
        prescriptions.add(prescription);
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