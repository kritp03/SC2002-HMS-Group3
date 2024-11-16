package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.io.AppointmentCsvHelper;
import HMS.src.io.MedicalRecordCsvHelper;
import HMS.src.medicalrecordsPDT.*;
import HMS.src.prescription.Prescription;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorManager {
    private final Map<String, Doctor> doctors;
    private final Map<String, MedicalEntry> currentEntries;  // Track current entries for each patient
    private SlotManager slotManager;
    private AppointmentCsvHelper apptCsvHelper = new AppointmentCsvHelper(); 
    private MedicalRecordCsvHelper medicalRecordCsvHelper = new MedicalRecordCsvHelper();

    public DoctorManager(SlotManager slotManager, AppointmentCsvHelper apptCsvHelper) {
        this.slotManager = slotManager;
        this.apptCsvHelper = apptCsvHelper;
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
            slotManager.printSlots();
        } else {
            System.out.println("Doctor with ID " + doctorID + " not found.");
        }
    }

    // Set a specific slot as unavailable for a doctor
    public void setDoctorSlotUnavailable(String doctorID, LocalTime startTime) {
        Doctor doctor = findDoctorById(doctorID);
        if (doctor != null) {
            slotManager.setUnavailable(startTime);
            List<String[]> apptCsvhelper = AppointmentCsvHelper.readCSV();


    }
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

    
    

    public void addDoctorDiagnosis(String patientID, String diagnosis) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }
    
        // Debugging: Check if an entry already exists
        System.out.println("Current entries before adding diagnosis: " + currentEntries.keySet());
        MedicalEntry entry = currentEntries.getOrDefault(patientID, new MedicalEntry());
        entry.setDiagnosis(diagnosis);  // Set diagnosis
        currentEntries.put(patientID, entry);  // Store/update current entry for patient
        System.out.println("Diagnosis added to the current entry for patient ID: " + patientID);
        System.out.println("Current entries after adding diagnosis: " + currentEntries.keySet());
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
    public void finalizeCurrentEntry(String patientID) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }
        MedicalEntry entry = currentEntries.get(patientID);
        if (entry != null) {
            List<MedicalRecord> records = MedicalRecordManager.getMedicalRecords(patientID);
    
            if (records.isEmpty()) {
                MedicalRecord newRecord = new MedicalRecord("R" + System.currentTimeMillis(), patientID, LocalDate.now());
                newRecord.addEntry(entry);
                MedicalRecordManager.addMedicalRecord(patientID, newRecord);
            } else {
                MedicalRecord latestRecord = records.get(records.size() - 1);
                latestRecord.addEntry(entry);
            }
    
            // Remove the finalized entry from the current entries map
            currentEntries.remove(patientID);
    
            // Save changes to CSV (if needed)
            List<String[]> appointments = apptCsvHelper.readCSV();
            for (String[] appointment : appointments) {
                if (appointment[0].equals(patientID)) {
                    appointment[3] = "Finalized"; // Assuming the 4th column tracks the status
                    apptCsvHelper.updateAppointment(appointment);
                    break;
                }
            }
    
            System.out.println("Finalized entry for patient ID " + patientID);
        } else {
            System.out.println("No current entry found for patient ID " + patientID);
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
            System.out.println("Medical Records for Patient ID: " + patientID);
            System.out.println(record);
        } else {
            System.out.println("No medical records found for patient ID: " + patientID);
        }
    }

    public void updatePatientMedicalRecord(String patientID, String diagnosis, String treatment, String prescription) {
        if (!MedicalRecordManager.patientExists(patientID)) {
            System.out.println("Patient with ID " + patientID + " does not exist.");
            return;
        }
    
        MedicalRecord record = MedicalRecordManager.getMedicalRecord(patientID);
    
        if (record == null) {
            System.out.println("No medical record found for patient ID: " + patientID);
            return;
        }
    
        if (diagnosis != null && !diagnosis.isEmpty()) {
            record.addDiagnosis(diagnosis);
            System.out.println("Diagnosis added to record.");
        }
    
        if (treatment != null && !treatment.isEmpty()) {
            record.addTreatment(treatment);
            System.out.println("Treatment added to record.");
        }
    
        if (prescription != null && !prescription.isEmpty()) {
            record.addPrescription(prescription);
            System.out.println("Prescription added to record with status PENDING.");
        }
    }

    // Method to mark a slot as unavailable
    public void setUnavailable(LocalTime startTime) {
        slotManager.setAvailability(startTime, false);
    }

    // Method to record the outcome of an appointment
    public void recordAppointmentOutcome(Appointment appointment, ServiceType serviceType, String diagnosis, 
                                         ArrayList<Prescription> prescriptions, String consultationNotes) {
        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            // Get the appointment ID
            String appointmentID = appointment.getAppointmentID();
            LocalDate appointmentDate = appointment.getDate();
            AppointmentOutcome outcome = new AppointmentOutcome(appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes);
            appointment.setOutcome(outcome);

            // Write the outcome to the CSV using ApptCsvHelper
            writeAppointmentOutcomeToCSV(outcome);
            System.out.println("Recorded appointment outcome for Appointment ID: " + appointment.getAppointmentID());
        } else {
            System.out.println("Cannot record outcome. Appointment is not completed.");
        }
    }

    // Method to accept an appointment
    public void acceptAppointment(Appointment appointment, String doctorID) {
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " accepted by Dr. " + doctorID);
        } else {
            System.out.println("Cannot accept appointment. Current status: " + appointment.getStatus());
        }
    }

    // Method to decline an appointment
    public void declineAppointment(Appointment appointment, String doctorID) {
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.DECLINED);
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " declined by Dr. " + doctorID);
        } else {
            System.out.println("Cannot decline appointment. Current status: " + appointment.getStatus());
        }
    }

    // Helper method to write appointment outcome to CSV using ApptCsvHelper
    private void writeAppointmentOutcomeToCSV(AppointmentOutcome outcome) {
        List<String[]> appointmentOutcomes = new ArrayList<>();
        String appointmentID = outcome.getAppointmentID();
        String serviceType = outcome.getServiceType().toString();
        String appointmentDate = outcome.getAppointmentDate().toString();
        String consultationNotes = outcome.getConsultationNotes();

        // Prescriptions and their statuses
        StringBuilder medications = new StringBuilder();
        StringBuilder medicationStatuses = new StringBuilder();

        // Add medication details (if any) to the output
        for (Prescription prescription : outcome.getPrescriptions()) {
            medications.append(prescription.getName()).append(";");
            medicationStatuses.append(prescription.getStatus().toString()).append(";");
        }

        // Convert the appointment outcome to a string array, including medications and their statuses
        String[] outcomeData = {
            appointmentID, 
            serviceType, 
            appointmentDate, 
            consultationNotes, 
            medications.toString(), 
            medicationStatuses.toString()
        };
        appointmentOutcomes.add(outcomeData);

        // Use ApptCsvHelper to write data to CSV
        apptCsvHelper.writeEntries(appointmentOutcomes);
    }
    

    // List all doctors
    public List<Doctor> listAllDoctors() {
        return new ArrayList<>(doctors.values());
    }
}

