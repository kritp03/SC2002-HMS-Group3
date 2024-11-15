// package HMS.src.appointment;

// import HMS.src.management.*;
// import HMS.src.slots.*;
// import java.time.LocalTime;
// import java.util.List;


// public class Appointment {
//     private final Patient patientID;  // Full Patient object
//     private final Doctor doctorID;    // Full Doctor object
//     private final Slot slot;        // Associated Slot
//     private AppointmentStatus status;

//     public enum AppointmentStatus {
//         PENDING, CONFIRMED, DECLINED
//     }

//     // Constructor
//     public Appointment(Patient patientID, Doctor doctorID, Slot slot) {
//         this.patientID = patientID;
//         this.doctorID = doctorID;
//         this.slot = slot;
//         this.status = AppointmentStatus.PENDING;  // Default status is PENDING
//     }

//     // to book appt
//     public boolean bookAppointment(Patient patient, Doctor doctor, LocalTime startTime, SlotManager slotManager) {
//         // Retrieve slot list
//         List<Slot> slots = slotManager.getSlots();

//         // Find the slot that matches the requested start time
//         for (Slot wantedslot : slots) {
//             if (slot.getStartTime().equals(startTime) && wantedslot.isAvailable()) {  // Check availability
//                 slotManager.setAvailability(startTime, false);  // Set the slot to unavailable
//                 Appointment appointment = new Appointment(patient, doctor, wantedslot);
//                 appointment.setStatus(AppointmentStatus.CONFIRMED);  // Set status to CONFIRMED
//                 System.out.println("Appointment booked successfully for " + startTime);
//                 return true;
//             }
//         }
//         System.out.println("The selected slot is unavailable.");
//         return false;
//     }
    
//     // Getters and Setters
//     public AppointmentStatus getStatus() {
//         return status;
//     }

//     public void setStatus(AppointmentStatus status) {
//         this.status = status;
//     }

//     public Slot getSlot() {
//         return slot;
//     }

//     public Patient getPatient() {
//         return patientID;
//     }

//     public Doctor getDoctor() {
//         return doctorID;
//     }

//     @Override
//     public String toString() {
//         return ", Patient: " + patientID + ", Doctor: " + doctorID +
//                ", Status: " + status + ", Slot: " + slot;
//     }
// }

package HMS.src.appointment;

import HMS.src.management.doctor.Doctor;
import HMS.src.management.patient.Patient;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private String appointmentID;        // Unique ID for the appointment
    private Patient patient;             // Associated patient
    private Doctor doctor;               // Associated doctor
    private LocalDate date;              // Date of appointment
    private LocalTime time;              // Time of appointment
    private AppointmentStatus status;    // Status of the appointment
    private AppointmentOutcome outcome;  // Outcome of the appointment, if completed

    public Appointment(String appointmentID, Patient patient, Doctor doctor, LocalDate date, LocalTime time) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.status = AppointmentStatus.PENDING;  // Default status
        this.outcome = null;                      // Outcome is null until appointment is completed
    }

    // Getters and setters
    public String getAppointmentID() {
        return appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public AppointmentOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(AppointmentOutcome outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return String.format("Appointment [ID: %s, Patient ID: %s, Doctor ID: %s, Status: %s, Date: %s, Time: %s, Outcome: %s]",
                appointmentID, patient.getUserID(), doctor.getUserID(), status, date, time, outcome != null ? outcome : "N/A");
    }
}