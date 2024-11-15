package HMS.src.management.doctor;

import HMS.src.appointment.*;
import HMS.src.medicalrecordsPDT.MedicalEntry;
import HMS.src.medicalrecordsPDT.MedicalRecord;
import HMS.src.medicalrecordsPDT.MedicalRecordManager;
import HMS.src.prescription.Prescription;
import HMS.src.slots.Slot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorManager {
    private final Map<String, Doctor> doctors;
    private final Map<String, MedicalEntry> currentEntries;  // Track current entries for each patient

    public DoctorManager() {
        this.doctors = new HashMap<>();
        this.currentEntries = new HashMap<>();
    }

    // Add a doctor to the manager
    public void addDoctor(Doctor doctor) {
        doctors.put(doctor.getUserID(), doctor);
    }

    // Find a doctor by ID
    public Doctor findDoctorById(String doctorID) {
        return doctors.get(doctorID);
    }

    // Find a doctor by name
    public Doctor findDoctorByName(String doctorName) {
        for (Doctor doctor : doctors.values()) {
            if (doctor.getName().equalsIgnoreCase(doctorName)) {
                return doctor;
            }
        }
        System.out.println("Doctor with name " + doctorName + " not found.");
        return null;
    }

    // View available slots for a specific doctor
    public void viewAvailableSlots(String doctorID) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.viewAvailableSlots();
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Set a specific slot as unavailable for a doctor
    public void setDoctorSlotUnavailable(String doctorID, LocalTime startTime) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.setUnavailable(startTime);
            System.out.println("Marked slot at " + startTime + " as unavailable for Dr. " + doctor.getName());
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Add a diagnosis to the current entry for a patient
    public void addDoctorDiagnosis(String patientID, String diagnosis) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }

        MedicalEntry entry = currentEntries.getOrDefault(patientID, new MedicalEntry());
        entry.setDiagnosis(diagnosis);  // Set diagnosis
        currentEntries.put(patientID, entry);  // Store/update current entry for patient
        System.out.println("Diagnosis added to the current entry for patient ID: " + patientID);
    }

    // Add a treatment to the current entry for a patient
    public void addDoctorTreatment(String patientID, String treatment) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }

        MedicalEntry entry = currentEntries.getOrDefault(patientID, new MedicalEntry());
        entry.setTreatment(treatment);  // Set treatment
        currentEntries.put(patientID, entry);  // Store/update current entry for patient
        System.out.println("Treatment added to the current entry for patient ID: " + patientID);
    }

    // Add a prescription to the current entry for a patient
    public void addDoctorPrescription(String patientID, String medicationName) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }

        MedicalEntry entry = currentEntries.getOrDefault(patientID, new MedicalEntry());
        entry.addPrescription(medicationName);  // Add prescription to current entry
        currentEntries.put(patientID, entry);  // Store/update current entry for patient
        System.out.println("Prescription added to the current entry for patient ID: " + patientID);
    }

    // Finalize and add the current entry to the patient's medical record
    public void finalizeCurrentEntry(String patientID) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }

        MedicalEntry entry = currentEntries.get(patientID);
        if (entry != null) {
            MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
            record.addEntry(entry);  // Add completed entry to the medical record
            currentEntries.remove(patientID);  // Clear the current entry
            System.out.println("Finalized entry added to medical record for patient ID: " + patientID);
        } else {
            System.out.println("No current entry to finalize for patient ID: " + patientID);
        }
    }

    // Set all slots as available for a doctor
    public void setDoctorAvailability(String doctorID, boolean isAvailable) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            for (Slot slot : doctor.getSlotManager().getSlots()) {
                doctor.getSlotManager().setAvailability(slot.getStartTime(), isAvailable);
            }
            System.out.println("All slots updated to " + (isAvailable ? "Available" : "Unavailable") + " for Dr. " + doctor.getName());
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Add diagnosis, treatment, or prescription to a patient's medical record via a specific doctor
    public void addDoctorDiagnosis(String doctorID, String patientID, String diagnosis) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.addDiagnosis(patientID, diagnosis);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    public void addDoctorTreatment(String doctorID, String patientID, String treatment) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.addTreatment(patientID, treatment);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    public void addDoctorPrescription(String doctorID, String patientID, String prescription) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.addPrescription(patientID, prescription);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Set the service type for a patient
    public void setDoctorServiceType(String doctorID, String patientID, ServiceType serviceType) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.setServiceType(patientID, serviceType);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Add appointment outcome for a patient
    public void addAppointmentOutcome(String doctorID, String patientID, ServiceType serviceType, LocalDate date,
                                      String diagnosis, String treatment, String prescription) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.addAppointmentOutcome(patientID, serviceType, date, diagnosis, treatment, prescription);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Update the status of an appointment
    public void updateDoctorAppointmentStatus(String doctorID, Appointment appointment, AppointmentStatus status) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.updateAppointmentStatus(appointment, status);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Record appointment outcome
    public void recordAppointmentOutcome(String doctorID, Appointment appointment, ServiceType serviceType,
                                         String diagnosis, ArrayList<Prescription> prescriptions, String consultationNotes) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            doctor.recordAppointmentOutcome(appointment, serviceType, diagnosis, prescriptions, consultationNotes);
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    public void viewPatientMedicalRecords(String patientID) {
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
        if (record != null) {
            System.out.println(record);  // Uses the toString() method from MedicalRecord to display details
        } else {
            System.out.println("No medical record found for patient ID: " + patientID);
        }
    }

    public void updatePatientMedicalRecord(String patientID, String diagnosis, String treatment, String prescription) {
        if (!MedicalRecordManager.patientExists(patientID)) 
        {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }

        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);

        if (diagnosis != null && !diagnosis.isEmpty()) 
        {
            record.addDiagnosis(patientID,diagnosis);  // Adds to the existing list of diagnoses
            System.out.println("Diagnosis added to record.");
        }

        if (treatment != null && !treatment.isEmpty()) 
        {
            record.addTreatment(patientID, treatment);  // Adds to the existing list of treatments
            System.out.println("Treatment added to record.");
        }

        if (prescription != null && !prescription.isEmpty()) 
        {
            record.addPrescription(patientID, prescription);  // Adds to the prescription list with default status "PENDING"
            System.out.println("Prescription added to record with status PENDING.");
        }
    }

    // List all doctors
    public List<Doctor> listAllDoctors() {
        return new ArrayList<>(doctors.values());
    }
}
