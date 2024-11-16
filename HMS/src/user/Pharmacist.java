package HMS.src.user;

import HMS.src.appointment.AppointmentManager;
import HMS.src.authorisation.PasswordManager;
import HMS.src.medication.MedicationManager;
import HMS.src.prescription.PrescriptionManager;
import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionStatus;
import java.util.List;

public class Pharmacist extends User {
    private List<Prescription> prescriptions;
    private AppointmentManager appointmentManager = new AppointmentManager();
    private MedicationManager medicationManager = new MedicationManager();
    private PrescriptionManager prescriptionManager = new PrescriptionManager();
    private PasswordManager passwordManager = new PasswordManager();

    private int age;

    public Pharmacist(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.PHARMACIST, emailId, age, gender);
    }

    public void dispenseMedication(Prescription prescription) {
        // prescription.updateStatus(PrescriptionStatus.DISPENSED);
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        System.out.println("Medication dispensed: " + prescription.getName());
    }

    public void viewApptOutcomeRecord(){
        appointmentManager.viewApptOutcomeRecord();
    }

    public void viewMedicationInventory(){
        medicationManager.viewMedicationInventory();
    }

    public void updatePrescriptionStatus(){
        prescriptionManager.runPrescriptionUpdateProcess();
    }

    public void changePassword(){
        passwordManager.changePassword();
    }
}