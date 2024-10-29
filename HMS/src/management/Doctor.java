package HMS.src.management;
import HMS.src.appointment.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Doctor {
    private String doctorID;
    private List<Appointment> appointments;
    private Map<String, MedicalRecord>  MedicalRecord;
    private List<Appointment> availability;


    // Constructors
    public Doctor(String doctorID){
        this.doctorID = doctorID;
        this.appointments = new ArrayList<>();
        this.medicalRecord = new MedicalRecord();
        this.outcomeRecord = new HashMap<>();
        this.availability = new ArrayList<>();
        
    }
    // show prescription, diagnosis and treatment
    public MedicalRecord viewPatientMedicalRecords(String patientID){
        return MedicalRecord.get(patientID, new MedicalRecord());
    }

    public boolean updatePatientsMedicalRecord (int patientID, MedicalRecord record){

    } 
    
    public List<Appointment> viewPersonalSchedule(){
        return new ArrayList<>(appointments);

    }
        
    public boolean acceptAppointmentRequest (int appointmentID){

    }
    
    public boolean declineAppointmentRequest (int appointmentID){

    }
    
    public List<Appointment> setAvailability (List<Appointment> slots){

    }
    
    public boolean recordAppointmentOutcome (int appointmentID, AppointmentOutcome outcome){

    }
    

}

