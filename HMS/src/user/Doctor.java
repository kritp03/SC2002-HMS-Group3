package HMS.src.user;

import HMS.src.appointment.*;
import java.util.List;

/**
 * Represents a doctor in the Hospital Management System.
 * Extends the base {@code User} class and includes additional attributes
 * and methods specific to a doctor.
 */
public class Doctor extends User {
    private String doctorID; // Unique identifier for the doctor
    private SlotManager slotManager; // Manages the doctor's appointment slots
    private List<Appointment> appointments; // List of appointments assigned to the doctor

    /**
     * Constructs a {@code Doctor} object.
     *
     * @param userID  The unique ID of the doctor.
     * @param name    The name of the doctor.
     * @param emailId The email address of the doctor.
     * @param age     The age of the doctor.
     * @param gender  The gender of the doctor.
     */
    public Doctor(String userID, String name, String emailId, int age, Gender gender) {
        super(userID, name, Role.DOCTOR, emailId, age, gender);
    }

    /**
     * Gets the unique doctor ID.
     *
     * @return The doctor's ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Gets the name of the doctor.
     *
     * @return The doctor's name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the slot manager for managing the doctor's appointment slots.
     *
     * @return The {@code SlotManager} associated with the doctor.
     */
    public SlotManager getSlotManager() {
        return slotManager;
    }

    /**
     * Gets the list of appointments assigned to the doctor.
     *
     * @return A {@code List} of {@code Appointment} objects.
     */
    public List<Appointment> getAppointments() {
        return appointments;
    }
}
