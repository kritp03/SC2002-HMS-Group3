package HMS.src.user;

import HMS.src.appointment.*;
import java.util.ArrayList;
import java.util.List;

public class Doctor {
    private final String doctorID;
    private final String name;
    private final SlotManager slotManager;
    private final List<Appointment> appointments;

    // Constructor
    public Doctor(String doctorID, String name, SlotManager slotManager) {
        this.doctorID = doctorID;
        this.name = name;
        this.slotManager = slotManager;
        this.appointments = new ArrayList<>();
    }

    // Getters
    public String getDoctorID() {
        return doctorID;
    }

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
