package HMS.src.user;

import HMS.src.appointment.AppointmentManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.medication.MedicationManager;
import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionManager;
import HMS.src.prescription.PrescriptionStatus;
import java.util.List;

/**
 * Represents a Pharmacist in the Hospital Management System.
 * Extends the {@link User} class and includes pharmacist-specific functionalities
 * such as managing prescriptions, viewing appointment outcomes, and managing medications.
 */
public class Pharmacist extends User {
    private List<Prescription> prescriptions; // List of prescriptions assigned to the pharmacist
    private AppointmentManager appointmentManager = new AppointmentManager(); // Manages appointments
    private MedicationManager medicationManager = new MedicationManager(); // Manages medications
    private PrescriptionManager prescriptionManager = new PrescriptionManager(); // Manages prescriptions
    private PasswordManager passwordManager = new PasswordManager(); // Manages passwords

    /**
     * Constructs a new {@code Pharmacist} object with the specified details.
     *
     * @param userID   The unique identifier for the pharmacist.
     * @param name     The name of the pharmacist.
     * @param emailId  The email ID of the pharmacist.
     * @param age      The age of the pharmacist.
     * @param gender   The gender of the pharmacist.
     */
    public Pharmacist(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.PHARMACIST, emailId, age, gender);
    }

    /**
     * Dispenses medication for a given prescription.
     *
     * @param prescription The {@link Prescription} to be dispensed.
     */
    public void dispenseMedication(Prescription prescription) {
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        System.out.println("Medication dispensed: " + prescription.getName());
    }

    /**
     * Views the appointment outcome record.
     * Delegates the operation to {@link AppointmentManager}.
     */
    public void viewApptOutcomeRecord() {
        appointmentManager.viewApptOutcomeRecord();
    }

    /**
     * Views the medication inventory.
     * Delegates the operation to {@link MedicationManager}.
     */
    public void viewMedicationInventory() {
        medicationManager.viewMedicationInventory();
    }

    /**
     * Updates the status of a prescription.
     * Delegates the operation to {@link PrescriptionManager}.
     */
    public void updatePrescriptionStatus() {
        prescriptionManager.runPrescriptionUpdateProcess();
    }

    /**
     * Allows the pharmacist to change their password.
     * Delegates the operation to {@link PasswordManager}.
     */
    public void changePassword() {
        passwordManager.changePassword();
    }
}
