package HMS.src.appointment;

public enum AppointmentOutcome {
    SUCCESS,   // Appointment was successful
    FAILED,    // Appointment failed (e.g., no show, or doctor unavailable)
    RESCHEDULED // Appointment was rescheduled
}