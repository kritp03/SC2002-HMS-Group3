package HMS.src.user;

import HMS.src.appointment.*;
import java.util.List;

public class Doctor extends User{
    private String doctorID;
    private SlotManager slotManager;
    private List<Appointment> appointments;

    // Constructor
    // public Doctor(String doctorID, String name, SlotManager slotManager) {
    //     this.doctorID = doctorID;
    //     this.name = name;
    //     this.slotManager = slotManager;
    //     this.appointments = new ArrayList<>();
    // }

    public Doctor(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.DOCTOR, emailId, age, gender);
    }

    

    // Getters
    public String getDoctorID() {
        return doctorID;
    }

    @Override
    public String getName() {
        return name;
    }

    public SlotManager getSlotManager() {
        return slotManager;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

}
