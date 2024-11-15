
package HMS.src.appointment;

import HMS.src.prescription.Prescription;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointmentOutcome {
    private String appointmentID;                // Appointment ID
    private LocalDate appointmentDate;  // Field to store the date of the appointment
    private ServiceType serviceType;             // Type of service (e.g., Consultation)
    private String diagnosis;                    // Diagnosis made during the appointment
    private ArrayList<Prescription> prescriptions;  // List of prescriptions given
    private String consultationNotes;            // Notes from the consultation


public AppointmentOutcome(String appointmentID, LocalDate appointmentDate, ServiceType diagnosisserviceType, String diagnosis, ArrayList<Prescription> prescriptions, String consultationNotes) {
    this.appointmentID = appointmentID;
    this.serviceType = diagnosisserviceType;
    this.diagnosis = diagnosis;
    this.appointmentDate = appointmentDate;
    this.prescriptions = prescriptions;
    this.consultationNotes = consultationNotes;
    }

    // Getters and setters
    public String getAppointmentID() {
        return appointmentID;
    }
    public LocalDate getAppointmentDate() {
        return appointmentDate;  //
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    @Override
    public String toString() {
        return String.format("AppointmentOutcome [ID: %s, Date: %s, Service Type: %s, Diagnosis: %s, Prescriptions: %s, Consultation Notes: %s]",
                appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes);
    }

}