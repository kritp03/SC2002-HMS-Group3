package HMS.src.appointment;
import HMS.src.management.*;
import java.time.*;

public class Appointment
{
    private String appointmentID;
    private Patient patient;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private AppointmentOutcome outcome;

    // Constructor
    public Appointment(String appointmentID, Patient patient, Doctor doctor, LocalDate date, Slot slot) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.slot = slot; 
        this.status = AppointmentStatus.PENDING; //default set to pending
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Slot getSlot(){
        return slot;
    }
}
