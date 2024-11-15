package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.medicalrecordsPDT.*;
import java.time.LocalDate;

public class DoctorManager {

    // Set the service type for a patient's medical record
    public void setServiceType(String patientID, ServiceType serviceType) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.setServiceType(serviceType);
            System.out.println("Appointment type " + serviceType + " set for patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    // Add appointment outcome to the patient's medical record
    public void addAppointmentOutcome(String patientID, ServiceType serviceType, LocalDate date,
                                      String diagnosis, String treatment, String prescription) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.setServiceType(serviceType);
            record.addDiagnosis(diagnosis);
            record.addTreatment(treatment);
            record.addPrescription(prescription);
            System.out.println("Appointment outcome added for patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    // Update the status of an appointment
    public void updateAppointmentStatus(Appointment appointment, AppointmentStatus status) {
        appointment.setStatus(status);
        System.out.println("Updated appointment status to: " + status);
    }

    
}
