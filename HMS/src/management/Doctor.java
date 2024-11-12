package HMS.src.management;

import HMS.src.appointment.ServiceType;
import HMS.src.medicalrecordsPDT.*;
import HMS.src.misc_classes.*;
import HMS.src.slots.*;
import java.time.LocalDate;
import java.time.LocalTime;


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
}

