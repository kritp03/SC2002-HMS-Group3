package HMS.src.MedicalRecord;

import HMS.src.pharmacy.*;
import java.util.List;


//needs to include diagnosis, treatment and prescription
public class MedicalRecord {
    private List<Diagnosis> diagnosis;
    private List<Treatment> treatment;
    private List<Prescription> prescription;

//constructor
    public MedicalRecord (Diagnosis diagnosis, Treatment treatment, Prescription  prescription)
    {
        this.diagnosis = (List<Diagnosis>) diagnosis;
        this.treatment = (List<Treatment>)treatment;
        this.prescription = (List<Prescription>)prescription;
    }

}
