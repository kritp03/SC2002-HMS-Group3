package HMS.src.controller;

import HMS.src.appointment.*;
import HMS.src.slots.Slot;

public class AppointmentController 
{
    private Appointment appointment;
    
    public String returnAppointmentId()
    {
        return appointment.getAppointmentID();
    }

    public AppointmentStatus returnStatus()
    {
        return appointment.getStatus();
    }

    public Slot returnSlot()
    {
        return appointment.getSlot();
    }

    
}
