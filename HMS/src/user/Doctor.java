package HMS.src.user;

import HMS.src.appointment.Appointment;
import HMS.src.appointment.AppointmentOutcome;
import HMS.src.appointment.AppointmentStatus;
import HMS.src.appointment.ServiceType;
import HMS.src.appointment.SlotManager;
import HMS.src.medicalrecordsPDT.*;
import HMS.src.prescription.Prescription;
import HMS.src.user.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class Doctor extends User{

    private final String doctorID;
    private final SlotManager slotManager;

   public String getDoctorID() {
      return this.doctorID;
   }

   public SlotManager getSlotManager() {
      return this.slotManager;
   }

   public Doctor(String var1, String var2, String var3, int var4, Gender var5) {
      super(var1, var2, Role.DOCTOR, var3, var4, var5);
      this.doctorID = var1;
      this.slotManager = new SlotManager();
   }

   public void viewAvailableSlots() {
      System.out.println("Available slots for Dr. " + this.name + " , " + this.doctorID + " :");
      this.slotManager.printSlots();
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
            record.addDiagnosis(diagnosis);
            record.addTreatment(treatment);
            record.addPrescription(prescription);
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

