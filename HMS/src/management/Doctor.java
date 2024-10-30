package HMS.src.management;

import HMS.src.appointment.*;
import HMS.src.MedicalRecord.*;

import java.util.ArrayList;
import java.util.List;


public class Doctor extends User{
    private String doctorID;
    private List<Appointment> appointments;
    private List<Appointment> availability;
    private List<MedicalRecord> patientRecords;
    

    // Constructors
    public Doctor(String doctorID, String name, String emailId ){
        super(doctorID, name, Role.DOCTOR, "kritpyy@gmail.com" );
        this.doctorID = doctorID;
        this.appointments = new ArrayList<>();
        this.patientRecords = new ArrayList<>();
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

