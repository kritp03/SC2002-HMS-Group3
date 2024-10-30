package HMS.src.management;

import HMS.src.MedicalRecord.*;
import HMS.src.appointment.*;
import java.util.ArrayList;
import java.util.List;


public class Doctor extends User{
    private String doctorID;
    private List<Appointment> appointments;
    private List<Slot> availability;
    private List<MedicalRecord> patientRecords;
    

    // Constructors
    public Doctor(String doctorID, String name, String emailId ){
        super(doctorID, name, Role.DOCTOR, "kritpyy@gmail" );
        this.doctorID = doctorID;
        this.appointments = new ArrayList<>();
        this.patientRecords = new ArrayList<>();
        this.availability = new ArrayList<>();
        
    }
    // to show prescription, diagnosis and treatment
    public MedicalRecord viewPatientMedicalRecords(String patientID) {
        for (MedicalRecord record : patientRecords) {
            //correct id
            if (record.getPatientID().equals(patientID)) {
                return record;
            }
        }
        return null; 
    }

    public List<Slot> viewAvailableSlots() {
        List<Slot> availability = new ArrayList<>();
        for (Slot slot : availability){
            if(slot.isAvailable()){
                availability.add(slot);
            }
        }
        return availability;
    }


    public boolean updatePatientRecords (String patientID, Diagnosis diagnosis, Treatment treatment, Prescription prescription){
        MedicalRecord record = viewPatientMedicalRecords(patientID);
        if(record != null){
            record.addDiagnosis(diagnosis);
            record.addTreatment(treatment);
            record.addPrescription(prescription);
            return true;
        }
        return false;
    }
    
        
    public boolean acceptAppointmentRequest(String appointmentID) {
        for (Appointment appointment : appointments) {
            // check if appointment ID is the same
            if (appointment.getAppointmentID().equals(appointmentID)) {
                Slot slot = appointment.getSlot();
                // check if the slot is available
                // if yes, update both appt & slot status
                if (slot.isAvailable()){
                    appointment.setStatus(AppointmentStatus.CONFIRMED); 
                    slot.book(appointment);
                    return true;
                }
            }
        }
        //else, if apptID not even found
        System.out.println("No appointmentID found with the ID: " + appointmentID);
        return false;
    }        
    
    public boolean declineAppointmentRequest (String appointmentID){
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentID().equals(appointmentID)) {
                if (appointment.getStatus() != AppointmentStatus.DECLINED) {
                    appointment.setStatus(AppointmentStatus.DECLINED); 
                    Slot slot = appointment.getSlot();
                    // slot becomes unavailable as well
                    slot.cancel();
                    return true;
                }else{
                    System.out.println("Appointment ID " + appointmentID + " has already been declined.");
                    return false;
            }
        }
    }
        System.out.println("No appointmentID found with the ID: " + appointmentID);
        return false;
    }
    
    public List<Slot> setAvailability(List<Slot> slots){
        for (Slot newSlot: slots){
            for (Slot existingSlot:this.availability){
                if(newSlot.overlaps(existingSlot)){
                    throw new IllegalArgumentException("New slot overlaps with existing slot: " + existingSlot);
                }
            }
        }
        this.availability = new ArrayList<>(slots);
        return this.availability;
    } 

    public boolean updateSlotAvailability(Slot slot, boolean availability){
        for (Slot s : this.availability){
            if (s.getStartTime().equals(slot.getStartTime()) && s.getEndTime().equals(slot.getEndTime())) {
                s.setAvailability(availability);
                return true;
            }
        }
        System.out.println("Slot " + slot.getStartTime() + " to " + slot.getEndTime() + " is not found");
        return false;
    }
}

