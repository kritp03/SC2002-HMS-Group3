// package HMS-new.src.management;

// import java.util.List;

// public class Pharmacist extends User {
//     private List<Prescription> prescriptions;

//     public Pharmacist(String userID, String name, Role role) {
//         super(userID, name, role);
//     }

//     public void dispenseMedication(Prescription prescription) {
//         prescription.updateStatus(PrescriptionStatus.DISPENSED);
//         System.out.println("Medication dispensed: " + prescription.getName());
//     }
// }