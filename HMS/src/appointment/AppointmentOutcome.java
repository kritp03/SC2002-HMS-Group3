// package HMS.src.appointment;

// public enum AppointmentOutcome {
//     SUCCESS,   // Appointment was successful
//     FAILED,    // Appointment failed (e.g., no show, or doctor unavailable)
//     RESCHEDULED // Appointment was rescheduled
// }

package HMS.src.appointment;

import HMS.src.prescription.Prescription;
import java.util.ArrayList;

public class AppointmentOutcome {
    private String appointmentID;                // Appointment ID
    private ServiceType serviceType;             // Type of service (e.g., Consultation)
    private String diagnosis;                    // Diagnosis made during the appointment
    private ArrayList<Prescription> prescriptions;  // List of prescriptions given
    private String consultationNotes;            // Notes from the consultation

    public AppointmentOutcome(String appointmentID, ServiceType serviceType, String diagnosis,
                              ArrayList<Prescription> prescriptions, String consultationNotes) {
        this.appointmentID = appointmentID;
        this.serviceType = serviceType;
        this.diagnosis = diagnosis;
        this.prescriptions = prescriptions;
        this.consultationNotes = consultationNotes;
    }

    // Getters and setters
    public String getAppointmentID() {
        return appointmentID;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
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
        return String.format("AppointmentOutcome [ID: %s, Service Type: %s, Diagnosis: %s, Prescriptions: %s, Consultation Notes: %s]",
                appointmentID, serviceType, diagnosis, prescriptions, consultationNotes);
    }
}