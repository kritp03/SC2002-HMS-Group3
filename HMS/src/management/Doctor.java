package HMS.src.management;

import HMS.src.MedicalRecord.*;
import HMS.src.appointment.*;
import HMS.src.misc_classes.*;
import HMS.src.slots.Slot;
import HMS.src.slots.SlotManager;
import java.util.ArrayList;
import java.util.List;


public class Doctor extends User{
    private final String doctorID;
    private List<Slot> availability;
    private final List<Appointment> appointments;
    private final List<MedicalRecord> patientRecords;
    

    // Constructors
    public Doctor(String doctorID, String name, String emailId, int age, Gender gender){
        super(doctorID, name, Role.DOCTOR, "kritpyy@gmail", age, gender);
        this.doctorID = doctorID;
        this.appointments = new ArrayList<>();
        this.patientRecords = new ArrayList<>();
        this.availability = new ArrayList<>();
        
    }
    
    public void viewAvailableSlots() {
        for (Slot slot : availability) {
            System.out.println(slot);
        }
    }

    public void setAvailability(SlotManager slotManager) {
        this.availability = slotManager.getSlots(); // Copy the list of available slots
    }

    // Accept appointment request
    public boolean acceptAppointmentRequest(Appointment appointment) {
        if (appointment.getSlot().isAvailable()) {
            appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
            appointment.getSlot().setAvailability(false); // Mark the slot as unavailable
            appointments.add(appointment);
            return true;
        }
        return false; // Slot already taken or unavailable
    }

    // Decline appointment request
    public void declineAppointmentRequest(Appointment appointment) {
        appointment.setStatus(Appointment.AppointmentStatus.DECLINED);
    }
    

}

