package HMS.src.doctor;

import HMS.src.appointment.Appointment;
import HMS.src.appointment.AppointmentOutcome;
import HMS.src.appointment.AppointmentStatus;
import HMS.src.appointment.ServiceType;
import HMS.src.management.*;
import HMS.src.medicalrecordsPDT.*;
import HMS.src.prescription.Prescription;
import HMS.src.slots.*;
import java.time.LocalTime;
import java.util.ArrayList;


public class Doctor extends User{
    private final String doctorID;
    private final SlotManager slotManager;



    // Constructors
    public Doctor(String doctorID, String name, String emailId, int age, Gender gender){
        super(doctorID, name, Role.DOCTOR, "kritpyy@gmail", age, gender);
        this.doctorID = doctorID;
        this.slotManager = new SlotManager();  // Each doctor gets their own SlotManager
        
    }
    
    public void viewAvailableSlots() {
        System.out.println("Available slots for Dr. " + doctorID + ":");
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
            record.addDiagnosis(diagnosis);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }
        
    public void addTreatment(String patientID, String treatment) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.addTreatment(treatment);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    public void addPrescription(String patientID, String prescription) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            record.addPrescription(prescription);
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
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

    // accept or decline appt request
    public void acceptAppointment(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " accepted by Dr. " + doctorID);
        } else {
            System.out.println("Cannot accept appointment. Current status: " + appointment.getStatus());
        }
    }

    // Method to decline an appointment request
    public void declineAppointment(Appointment appointment) {
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.DECLINED);
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " declined by Dr. " + doctorID);
        } else {
            System.out.println("Cannot decline appointment. Current status: " + appointment.getStatus());
        }
    }
}

