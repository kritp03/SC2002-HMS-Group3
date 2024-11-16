
package HMS.src.appointment;

import HMS.src.prescription.Prescription;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointmentOutcome {
    private final String appointmentID; // Appointment ID
    private LocalDate appointmentDate; // Field to store the date of the appointment
    private String serviceType; // Type of service (e.g., Consultation)
    private String diagnosis; // Diagnosis made during the appointment
    private ArrayList<Prescription> prescriptions; // List of prescriptions given
    private String consultationNotes; // Notes from the consultation

    /**
     * Constructor for creating a new appointment outcome.  
     * @param appointmentID
     * @param appointmentDate
     * @param diagnosisserviceType
     * @param diagnosis
     * @param prescriptions
     * @param consultationNotes
     */
    public AppointmentOutcome(String appointmentID, LocalDate appointmentDate, String diagnosisserviceType,
            String diagnosis, ArrayList<Prescription> prescriptions, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.serviceType = diagnosisserviceType;
        this.diagnosis = diagnosis;
        this.appointmentDate = appointmentDate;
        this.prescriptions = prescriptions;
        this.consultationNotes = consultationNotes;
    }

    /**
     * get the appointment ID
     * @return appointmentID
     */
    public String getAppointmentID() {
        return appointmentID;
    }

    /**
     * get the appointment date
     * @return appointmentDate
     */
    public LocalDate getAppointmentDate() {
        return appointmentDate; //
    }

    /**
     * get the service type
     * @return serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * get the diagnosis
     * @return diagnosis
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * set the appointment date
     * @param appointmentDate
     */
    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    /**
     * set the appointment ID
     * @param appointmentID
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * set the diagnosis
     * @param diagnosis
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * get the prescriptions
     * @return prescriptions
     */
    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     * set the prescriptions
     * @param prescriptions
     */
    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     * get the consultation notes
     * @return consultationNotes
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * set the consultation notes
     * @param consultationNotes
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Returns a string representation of the appointment outcome.
     */
    @Override
    public String toString() {
        return String.format(
                "AppointmentOutcome [ID: %s, Date: %s, Service Type: %s, Diagnosis: %s, Prescriptions: %s, Consultation Notes: %s]",
                appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes);
    }

}