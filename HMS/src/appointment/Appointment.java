package HMS.src.appointment;

import HMS.src.management.*;
import HMS.src.slots.Slot;

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
