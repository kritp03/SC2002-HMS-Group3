package HMS.src.pharmacy;

public class Prescription {
    private String prescriptionID;
    private String name;
    private int quantity;
    private PrescriptionStatus status;

    public Prescription(String prescriptionID, String name, int quantity, PrescriptionStatus status) {
        this.prescriptionID = prescriptionID;
        this.name = name;
        this.quantity = quantity;
        this.status = status;
    }

    public String getPrescriptionID() {
        return prescriptionID;
    }

    public void setPrescriptionID(String prescriptionID) {
        this.prescriptionID = prescriptionID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PrescriptionStatus getStatus() {
        return status;
    }

    public void setStatus(PrescriptionStatus status) {
        this.status = status;
    }
}

enum PrescriptionStatus {
    DISPENSED, PENDING, CANCELLED;
}
