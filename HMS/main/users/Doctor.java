package HMS.main.users;

import HMS.main.appointment.Appointment;

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
        this.appointments = new ArrayList<Appointment>();
        this.medicalRecord = new MedicalRecord();
        this.outcomeRecord = new HashMap<>();
        this.availability = new ArrayList<>();
        
    }
    public MedicalRecords viewPatientMedicalRecords(String patientID){
        return MedicalRecord.get(patientID, new MedicalRecord());
    }

    public boolean updatePatientsMedicalRecords (int patientID, MedicalRecord record){

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
    
    public boolean recordAppointmentOutcome (int appointmentID, OutcomeRecord outcome){

    }
    

}

