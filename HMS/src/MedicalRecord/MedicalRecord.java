package HMS.src.MedicalRecord;

import HMS.src.pharmacy.*;
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
        this.diagnosis = (List<Diagnosis>) diagnosis;
        this.treatment = (List<Treatment>)treatment;
        this.prescription = (List<Prescription>)prescription;
    }


    public String getPatientID(String patientID){
        return patientID;
    }


    public MedicalRecord() {
        //TODO Auto-generated constructor stub
    }

}
