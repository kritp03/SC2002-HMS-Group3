package HMS.src.appointment;

import HMS.src.prescription.Prescription;
import HMS.src.user.Doctor;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Represents the outcome of an appointment, including details such as service type, diagnosis, and prescriptions.
 */
public class AppointmentOutcome {
    private final String appointmentID; // Appointment ID
    private LocalDate appointmentDate; // Field to store the date of the appointment
    private String serviceType; // Type of service (e.g., Consultation)
    private String diagnosis; // Diagnosis made during the appointment
    private ArrayList<Prescription> prescriptions; // List of prescriptions given
    private String consultationNotes; // Notes from the consultation
    private final Doctor doctor; // Doctor associated with the appointment

    /**
     * Constructor for creating a new appointment outcome.
     * 
     * @param appointmentID     Unique identifier for the appointment.
     * @param appointmentDate   Date of the appointment.
     * @param serviceType       Type of service provided (e.g., Consultation, Surgery).
     * @param diagnosis         Diagnosis determined during the appointment.
     * @param prescriptions     List of prescriptions issued during the appointment.
     * @param consultationNotes Notes from the consultation.
     * @param doctor            Doctor responsible for the appointment.
     */
    public AppointmentOutcome(String appointmentID, LocalDate appointmentDate, String serviceType,
            String diagnosis, ArrayList<Prescription> prescriptions, String consultationNotes, Doctor doctor) {
        this.appointmentID = appointmentID;
        this.serviceType = serviceType;
        this.diagnosis = diagnosis;
        this.appointmentDate = appointmentDate;
        this.prescriptions = prescriptions;
        this.consultationNotes = consultationNotes;
        this.doctor = doctor;
    }

    /**
     * Gets the doctor associated with the appointment.
     * 
     * @return Doctor associated with the appointment.
     */
    public Doctor getDoctor() {
        return doctor;
    }

    /**
     * Gets the appointment ID.
     * 
     * @return Appointment ID.
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * Gets the appointment date.
     * 
     * @return Appointment date.
     */
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    /**
     * Sets the appointment date.
     * 
     * @param appointmentDate New date of the appointment.
     */
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * Gets the service type.
     * 
     * @return Type of service provided.
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the service type.
     * 
     * @param serviceType New service type for the appointment.
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Gets the diagnosis.
     * 
     * @return Diagnosis made during the appointment.
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * Sets the diagnosis.
     * 
     * @param diagnosis New diagnosis for the appointment.
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * Gets the list of prescriptions.
     * 
     * @return List of prescriptions issued during the appointment.
     */
    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * Sets the list of prescriptions.
     * 
     * @param prescriptions New list of prescriptions for the appointment.
     */
    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     * Gets the consultation notes.
     * 
     * @return Notes from the consultation.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Sets the consultation notes.
     * 
     * @param consultationNotes New notes for the appointment.
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Returns a string representation of the appointment outcome.
     * 
     * @return String representation of the appointment outcome.
     */
    @Override
    public String toString() {
        return String.format(
                "AppointmentOutcome [ID: %s, Date: %s, Service Type: %s, Diagnosis: %s, Prescriptions: %s, Consultation Notes: %s]",
                appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes);
    }
}
