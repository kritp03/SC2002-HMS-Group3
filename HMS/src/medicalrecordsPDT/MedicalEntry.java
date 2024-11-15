package HMS.src.medicalrecordsPDT;

import java.util.HashMap;
import java.util.Map;

public class MedicalEntry {

    public MedicalEntry() 
    {
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setPrescriptions(Map<String, String> prescriptions) {
        this.prescriptions = prescriptions;
    }

    private String diagnosis;
    private String treatment;
    private Map<String, String> prescriptions = new HashMap<>();

    public MedicalEntry(String diagnosis, String treatment) {
        this.diagnosis = diagnosis;
        this.treatment = treatment;
    }

    // Methods to add and update prescriptions
    public void addPrescription(String medicationName) {
        prescriptions.put(medicationName, "PENDING");
    }

    public void updatePrescriptionStatus(String medicationName, String status) {
        if (prescriptions.containsKey(medicationName)) {
            prescriptions.put(medicationName, status);
        } else {
            System.out.println("Prescription not found: " + medicationName);
        }
    }

    // Getters
    public String getDiagnosis() {
        return diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public Map<String, String> getPrescriptions() {
        return prescriptions;
    }

    
    @Override
    public String toString() {
        return "Diagnosis: " + diagnosis + "\n" +
                "Treatment: " + treatment + "\n" +
                "Prescriptions: " + prescriptions + "\n";
    }
}
