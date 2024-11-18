package HMS.src.appointment;

public enum AppointmentStatus {
    PENDING,   // default
    CONFIRMED, 
    CANCELLED,
    DECLINED,
    COMPLETED  ;

    public String showStatusByColor(){
        switch (this) {
            case PENDING:
                return "\u001B[33m" + this + "\u001B[0m";
            case CONFIRMED:
                return "\u001B[32m" + this + "\u001B[0m";
            case CANCELLED:
                return "\u001B[31m" + this + "\u001B[0m";
            case DECLINED:
                return "\u001B[31m" + this + "\u001B[0m";
            case COMPLETED:
                return "\u001B[36m" + this + "\u001B[0m";
            default:
                return this.toString();
        }
    }
}
