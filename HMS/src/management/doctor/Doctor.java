package HMS.src.management.doctor;

import HMS.src.appointment.*;
import HMS.src.management.*;
import HMS.src.medicalrecordsPDT.*;
import HMS.src.prescription.Prescription;
import HMS.src.slots.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class Doctor extends User{
    private final String doctorID;
    private final SlotManager slotManager;



    public String getDoctorID() {
        return doctorID;
    }

    public SlotManager getSlotManager() {
        return slotManager;
    }

    // Constructors
    public Doctor(String doctorID, String name, String emailId, int age, Gender gender){
        super(doctorID, name, Role.DOCTOR, emailId, age, gender);
        this.doctorID = doctorID;
        this.slotManager = new SlotManager();  // Each doctor gets their own SlotManager
        
    }
    
    public void viewAvailableSlots() {
        System.out.println("Available slots for Dr. " + name + " , " + doctorID + " :");
        slotManager.printSlots();
    }
    

    // Method to mark a slot as unavailable but default slots should all be available 
    public void setUnavailable(LocalTime startTime) {
        slotManager.setAvailability(startTime, false);
    }


    //MEDICAL RECORD

    public void addDiagnosis(String patientID, String diagnosis) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.addDiagnosis(patientID, diagnosis);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }
        
    public void addTreatment(String patientID, String treatment) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.addTreatment(patientID,treatment);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    public void addPrescription(String patientID, String prescription) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.addPrescription(patientID, prescription);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    // service type
    public void setServiceType(String patientID, ServiceType serviceType) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.setServiceType(serviceType);
            System.out.println("Appointment type " + serviceType + " set for patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }
    
    public void addAppointmentOutcome(String patientID, ServiceType serviceType, LocalDate date, 
                                      String diagnosis, String treatment, String prescription) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.setServiceType(serviceType);  // Set appointment type and date
            record.addDiagnosis(patientID, diagnosis);
            record.addTreatment(patientID,treatment);
            record.addPrescription(patientID,prescription);
            System.out.println("Appointment outcome added for patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

        // Method to update appointment status
    public void updateAppointmentStatus(Appointment appointment, AppointmentStatus status) {
        appointment.setStatus(status);
        System.out.println("Updated appointment status to: " + status);
    }

    // Method to record appointment outcome
    public void recordAppointmentOutcome(Appointment appointment, ServiceType serviceType, String diagnosis,
                                         ArrayList<Prescription> prescriptions, String consultationNotes) {
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            AppointmentOutcome outcome = new AppointmentOutcome(
                appointment.getAppointmentID(), serviceType, diagnosis, prescriptions, consultationNotes);
            appointment.setOutcome(outcome);
            System.out.println("Recorded appointment outcome: " + outcome);
        } else {
            System.out.println("Cannot record outcome. Appointment is not completed.");
        }
    }
}

