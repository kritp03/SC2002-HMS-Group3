package HMS.src.controller;

import HMS.src.appointment.AppointmentStatus;
import HMS.src.appointment.*;
import HMS.src.appointment.Slot;

public class AppointmentController extends Appointment{
    public String getAppointmentID() {
        return appointmentID;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public Slot getSlot(){
        return slot;
    }

    // if scheduled appt is cancelled, need to update status of both appt and slot
    public void cancel(){
        this.status = AppointmentStatus.CANCELLED;
        slot.cancel();
    }
}
