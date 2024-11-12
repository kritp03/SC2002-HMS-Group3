package HMS.src.medication;

import java.time.LocalDate;

public class ReplenishmentRequest {
    private String requestID;
    private String medicationID;
    private int quantity;
    private LocalDate date;
    private ReplenishmentRequestStatus status;
    private String approvedBy;

    public ReplenishmentRequest(String requestID, String medicationID, int quantity, LocalDate date) {
        this.requestID = requestID;
        this.medicationID = medicationID;
        this.quantity = quantity;
        this.date = date;
        this.status = ReplenishmentRequestStatus.PENDING;  
        this.approvedBy = "someone";  
    }

    public String getRequestID() {
        return requestID;
    }

    public String getMedicationID() {
        return medicationID;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReplenishmentRequestStatus getStatus() {
        return status;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setStatus(ReplenishmentRequestStatus status) {
        this.status = status;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void approveRequest(String approverName) {
        setStatus(ReplenishmentRequestStatus.APPROVED);
        setApprovedBy(approverName);
    }

    public void rejectRequest(String approverName) {
        setStatus(ReplenishmentRequestStatus.REJECTED);
        setApprovedBy(approverName);
    }

    public void fulfillRequest() {
        setStatus(ReplenishmentRequestStatus.FULFILLED);
    }
}

enum ReplenishmentRequestStatus {
    PENDING,
    APPROVED,
    REJECTED,
    FULFILLED;

    public String showStatusByColor(){
        switch (this){
            case PENDING:
                return "\u001B[33m" + this + "\u001B[0m";
            case APPROVED:
                return "\u001B[32m" + this + "\u001B[0m";
            case REJECTED:
                return "\u001B[31m" + this + "\u001B[0m";
            case FULFILLED:
                return "\u001B[36m" + this + "\u001B[0m";
            default:
                return this.toString();
        }
    }
}