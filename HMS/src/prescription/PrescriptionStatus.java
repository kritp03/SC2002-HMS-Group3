package HMS.src.prescription;

public enum PrescriptionStatus {
    DISPENSED, 
    PENDING, 
    CANCELLED;

    public String showStatusByColor(){
        switch(this){
            case DISPENSED:
                return "\u001B[32m" + this + "\u001B[0m";
            case PENDING:
                return "\u001B[33m" + this + "\u001B[0m";
            case CANCELLED:
                return "\u001B[31m" + this + "\u001B[0m";
            default:
                return this.toString();
        }
    }
}