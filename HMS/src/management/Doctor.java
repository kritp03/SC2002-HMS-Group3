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
        super(doctorID, name, Role.DOCTOR, "kritpyy@gmail" );
        this.doctorID = doctorID;
        this.appointments = new ArrayList<>();
        this.patientRecords = new ArrayList<>();
        this.availability = new ArrayList<>();
        
    }
    // show prescription, diagnosis and treatment
    public MedicalRecord viewPatientMedicalRecords(String patientID) {
        for (MedicalRecord record : patientRecords) {
            if (record.getPatientID(patientID).equals(patientID)) {
                return record;
            }
        }
        return null; // or throw an exception if not found
    }

    public boolean updatePatientsMedicalRecord(String patientID, MedicalRecord updatedRecord) {
        for (int i = 0; i < patientRecords.size(); i++) {
            MedicalRecord record = patientRecords.get(i);
            if (record.getPatientID(patientID).equals(patientID)) {
                patientRecords.set(i, updatedRecord);
                return true;
            }
        }
        return false;
    }
        
    public List<Appointment> viewAvailableAppointments() {
        return new ArrayList<>(appointments);
    }
        
    public boolean acceptAppointmentRequest(String appointmentID) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                Slot slot = appointment.getSlot();
                if (slot.isIsAvailable()){
                appointment.setStatus(AppointmentStatus.CONFIRMED); 
                slot.book();
                return true;
                }
            }
        }
        return false;
    }        
    
    public boolean declineAppointmentRequest (String appointmentID){
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                if(appointment.getStatus() != AppointmentStatus.DECLINED){
                    appointment.setStatus(AppointmentStatus.DECLINED); 
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<Appointment> setAvailability (List<Appointment> slots){

    }
    
    public boolean recordAppointmentOutcome (int appointmentID, AppointmentOutcome outcome){

    }
    

}

