package HMS.src.MedicalRecord;

import java.util.ArrayList;
import java.util.List;


//needs to include diagnosis, treatment and prescription
public class MedicalRecord {
    private String patientID;
    private List<Diagnosis> diagnosis;
    private List<Treatment> treatment;
    private List<Prescription> prescription;

//constructor
    public MedicalRecord(String patientID)
    {
        this.patientID = patientID;
        this.diagnosis = new ArrayList<>();
        this.treatment = new ArrayList<>();
        this.prescription = new ArrayList<>();
        // this.diagnosis = (List<Diagnosis>) diagnosis;
        // this.treatment = (List<Treatment>)treatment;
        // this.prescription = (List<Prescription>)prescription;
    }

    //getters
    public String getPatientID() {
        return patientID;
    }

    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    public List<Treatment> getTreatment() {
        return treatment;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void addDiagnosis(Diagnosis diagnosis){
        this.diagnosis.add(diagnosis);
    }

    public void addTreatment(Treatment treatment){
        this.treatment.add(treatment);
    }

    public void addPrescription(Prescription prescription){
        this.prescription.add(prescription);
    }


}
