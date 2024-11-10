package HMS.src.appointment;

import HMS.src.management.*;
import HMS.src.slots.*;
import java.time.LocalTime;
import java.util.List;


public class Appointment {
    private final Patient patientID;  // Full Patient object
    private final Doctor doctorID;    // Full Doctor object
    private final Slot slot;        // Associated Slot
    private AppointmentStatus status;

    public enum AppointmentStatus {
        PENDING, CONFIRMED, DECLINED
    }

    // Constructor
    public Appointment(Patient patientID, Doctor doctorID, Slot slot) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.slot = slot;
        this.status = AppointmentStatus.PENDING;  // Default status is PENDING
    }

    // to book appt
    public boolean bookAppointment(Patient patient, Doctor doctor, LocalTime startTime, SlotManager slotManager) {
        // Retrieve slot list
        List<Slot> slots = slotManager.getSlots();

        // Find the slot that matches the requested start time
        for (Slot wantedslot : slots) {
            if (slot.getStartTime().equals(startTime) && wantedslot.isAvailable()) {  // Check availability
                slotManager.setAvailability(startTime, false);  // Set the slot to unavailable
                Appointment appointment = new Appointment(patient, doctor, wantedslot);
                appointment.setStatus(AppointmentStatus.CONFIRMED);  // Set status to CONFIRMED
                System.out.println("Appointment booked successfully for " + startTime);
                return true;
            }
        }
        System.out.println("The selected slot is unavailable.");
        return false;
    }
    
    // Getters and Setters
    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Slot getSlot() {
        return slot;
    }

    public Patient getPatient() {
        return patientID;
    }

    public Doctor getDoctor() {
        return doctorID;
    }

    @Override
    public String toString() {
        return ", Patient: " + patientID + ", Doctor: " + doctorID +
               ", Status: " + status + ", Slot: " + slot;
    }
}
