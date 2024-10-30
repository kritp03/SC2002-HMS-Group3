package HMS.src.appointment;
import java.time.LocalTime;


public class Slot {
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isAvailable;


    //constructors
    public Slot(LocalTime startTime, LocalTime endTime){
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = true;
    }

    //getters
    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }
    public void cancel(){
        isAvailable = true;
    }
    public void book(){
        if (isAvailable){
            isAvailable = false;
        }
        else{
            //exceptions
            throw new IllegalStateException("Slot is already booked.");
        }
    }
}
