package HMS.src.appointment;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Slot {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;
    private List<Appointment> appointments;

    //constructors
    public Slot(LocalTime startTime, LocalTime endTime){
        if (startTime.isBefore(endTime)){
            this.startTime = startTime;
            this.endTime = endTime;
            this.isAvailable = true;
            this.appointments = new ArrayList<>();
    }   else {
            throw new IllegalArgumentException("End time must be after start time.");
        }
}


    //getters
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAvailability(boolean availability) {
        this.isAvailable = availability;
    }
    

    // cancel slot
    public void cancel(){
        isAvailable = true;
        appointments.clear();
    }

    //book slot
    public void book(Appointment appointment){
        if (!isAvailable){
            throw new IllegalStateException("Slot is already booked.");
        }
        //else set to false
        isAvailable = false;
        appointments.add(appointment);
    }

    public void updateTime(LocalTime newStartTime, LocalTime newEndTime) {
        if (newStartTime.isBefore(newEndTime)) {
            this.startTime = newStartTime;
            this.endTime = newEndTime;
        } else {
            throw new IllegalArgumentException("End time must be after start time.");
        }
    }
    public boolean overlaps(Slot other) {
        return this.startTime.isBefore(other.endTime) && this.endTime.isAfter(other.startTime);
    }

    @Override
    public String toString() {
        return "Slot from " + startTime + " to " + endTime + (isAvailable ? " (Available)" : " (Booked)");
    }



}
