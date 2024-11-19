package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.medicalrecordsPDT.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Represents a patient in the Hospital Management System.
 * Extends the {@link User} class and contains patient-specific details 
 * such as medical records, appointments, and contact information.
 */
public class Patient extends User {
    private final String patientId; // Unique identifier for the patient
    private LocalDate dateOfBirth; // Date of birth of the patient
    private ContactInformation contactinfo; // Contact details for the patient
    private final String bloodType; // Blood type of the patient
    private List<MedicalRecord> medicalRecord; // List of medical records associated with the patient
    private List<Appointment> appointment; // List of appointments associated with the patient

    /**
     * Constructs a new {@code Patient} object with all details.
     * 
     * @param name          The name of the patient.
     * @param patientId     The unique identifier for the patient.
     * @param dateOfBirth   The date of birth of the patient.
     * @param gender        The gender of the patient.
     * @param contactInfo   The contact information of the patient.
     * @param bloodType     The blood type of the patient.
     * @param medicalRecord The list of medical records associated with the patient.
     * @param appointment   The list of appointments associated with the patient.
     */
    public Patient(String name, String patientId, LocalDate dateOfBirth, Gender gender, ContactInformation contactInfo,
            String bloodType, List<MedicalRecord> medicalRecord, List<Appointment> appointment) {
        super(patientId, name, Role.PATIENT, contactInfo.getEmailId(), calculateAge(dateOfBirth), gender);
        this.patientId = patientId;
        this.dateOfBirth = dateOfBirth;
        this.contactinfo = contactInfo;
        this.bloodType = bloodType;
        this.medicalRecord = medicalRecord;
        this.appointment = appointment;
    }

    /**
     * Constructs a new {@code Patient} object with essential details.
     * 
     * @param patientID   The unique identifier for the patient.
     * @param name        The name of the patient.
     * @param dateOfBirth The date of birth of the patient.
     * @param gender      The gender of the patient.
     * @param bloodType   The blood type of the patient.
     * @param contactInfo The contact information of the patient.
     */
    public Patient(String patientID, String name, LocalDate dateOfBirth, Gender gender, String bloodType,
            ContactInformation contactInfo) {
        super(patientID, name, Role.PATIENT, contactInfo.getEmailId(), calculateAge(dateOfBirth), gender);
        this.patientId = patientID;
        this.dateOfBirth = dateOfBirth;
        this.contactinfo = contactInfo;
        this.bloodType = bloodType;
    }

    /**
     * Retrieves the list of medical records for the patient.
     * 
     * @return A list of {@link MedicalRecord} objects associated with the patient.
     */
    public List<MedicalRecord> viewMedicalRecords() {
        return this.medicalRecord;
    }

    /**
     * Updates the patient's phone number.
     * 
     * @param newPhoneNumber The new phone number to set.
     */
    public void changePhoneNumber(String newPhoneNumber) {
        contactinfo.changePhoneNumber(newPhoneNumber);
    }

    /**
     * Updates the patient's email ID.
     * 
     * @param newEmailId The new email ID to set.
     */
    public void changeEmailId(String newEmailId) {
        contactinfo.changeEmailId(newEmailId);
    }

    /**
     * Updates the patient's address.
     * 
     * @param newAddress The new address to set.
     */
    public void changeAddress(String newAddress) {
        contactinfo.changeAddress(newAddress);
    }

    /**
     * Updates the name of the patient's next of kin.
     * 
     * @param newNextOfKinName The new name of the next of kin.
     */
    public void changeNextOfKinname(String newNextOfKinName) {
        contactinfo.changeNextOfKinName(newNextOfKinName);
    }

    /**
     * Updates the phone number of the patient's next of kin.
     * 
     * @param newPhoneNumber The new phone number of the next of kin.
     */
    public void changeNextOfKinPhoneNumber(String newPhoneNumber) {
        contactinfo.changeNextOfKinPhoneNumber(newPhoneNumber);
    }

    /**
     * Calculates the age of the patient based on their date of birth.
     * 
     * @param dateOfBirth The date of birth of the patient.
     * @return The age of the patient in years.
     */
    private static int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        LocalDate today = LocalDate.now();
        return Period.between(dateOfBirth, today).getYears();
    }
}
