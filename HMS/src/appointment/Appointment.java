package HMS.src.appointment;

import HMS.src.user.Doctor;
import HMS.src.user.Patient;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {
    private final String appointmentID;        // Unique ID for the appointment
    private final Patient patient;             // Associated patient
    private final Doctor doctor;               // Associated doctor
    private LocalDate date;              // Date of appointment
    private LocalTime time;              // Time of appointment
    private AppointmentStatus status;    // Status of the appointment
    private AppointmentOutcome outcome;  // Outcome of the appointment, if completed

    /**
     * Constructor for creating a new appointment.
     * @param appointmentID
     * @param patient
     * @param doctor
     * @param date
     * @param time
     */
    public Appointment(String appointmentID, Patient patientID, Doctor doctorID, LocalDate date, LocalTime time) {
        this.appointmentID = appointmentID;
        this.patient = patientID;
        this.doctor = doctorID;
        this.date = date;
        this.time = time;
        this.status = AppointmentStatus.PENDING;  // Default status
        this.outcome = null;                      // Outcome is null until appointment is completed
    }

    /**
     * Getters and setters
     * @return
     */

     /**
      * Get the appointment ID.
      * @return appointmentID
      */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Get the associated patient.
     * @return patient
     */
    public Patient getPatient() {
        return patient;
    }

    /**
     * Get the associated doctor.
     * @return doctor
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Get the date of the appointment.
     * @return date
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Set the date of the appointment.
     * @param date
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Get the time of the appointment.
     * @return time
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Set the time of the appointment.
     * @param time
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Get the status of the appointment.
     * @return status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the appointment.
     * @param status
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Get the outcome of the appointment.
     * @return outcome
     */
    public AppointmentOutcome getOutcome() {
        return outcome;
    }

    /**
     * Set the outcome of the appointment.
     * @param outcome
     */
    public void setOutcome(AppointmentOutcome outcome) {
        this.outcome = outcome;
    }

    /**
     * Override the toString method to display the appointment details.
     * @return String
     */
    @Override
    public String toString() {
        return String.format("Appointment [ID: %s, Patient ID: %s, Doctor ID: %s, Status: %s, Date: %s, Time: %s, Outcome: %s]",
                appointmentID, patient.getUserID(), doctor.getDoctorID(), status, date, time, outcome != null ? outcome : "N/A");
    }
}