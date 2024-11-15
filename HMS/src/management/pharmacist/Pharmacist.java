package HMS.src.management.pharmacist;

import HMS.src.management.Gender;
import HMS.src.management.Role;
import HMS.src.management.User;
import HMS.src.prescription.Prescription;
import HMS.src.prescription.PrescriptionStatus;
import java.util.List;

public class Pharmacist extends User {
    private List<Prescription> prescriptions;

    private int age;

    public Pharmacist(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.PHARMACIST, emailId, age, gender);
    }

    public void dispenseMedication(Prescription prescription) {
        // prescription.updateStatus(PrescriptionStatus.DISPENSED);
        prescription.setStatus(PrescriptionStatus.DISPENSED);
        System.out.println("Medication dispensed: " + prescription.getName());
    }
}