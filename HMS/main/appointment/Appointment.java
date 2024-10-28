package HMS.main.appointment;

import HMS.main.users.*;
import java.time.*;

public class Appointment
{
    private String appointmentID;
    private Patient patient;
    private Doctor Doctor;
    private LocalDate date;
    private LocalTime time;
    private AppointmentStatus status;
    private AppointmentOutcome outcome;
}