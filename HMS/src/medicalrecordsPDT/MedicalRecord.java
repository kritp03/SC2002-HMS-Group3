package HMS.src.medicalrecordsPDT;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecord {
    private final String patientID;
    private final List<String> diagnoses = new ArrayList<>();
    private final List<String> treatments = new ArrayList<>();
    private final List<String> prescriptions = new ArrayList<>();

    // Constructor
    public MedicalRecord(String patientID) {
        this.patientID = patientID;
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

    // Method to display the medical record (toString)
    @Override
    public String toString() {
        return "Medical Record for " + patientID + ":\n" +
                "Diagnoses: " + diagnoses + "\n" +
                "Treatments: " + treatments + "\n" +
                "Prescriptions: " + prescriptions;
    }


}
