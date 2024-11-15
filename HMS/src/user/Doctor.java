package HMS.src.user;


public class Doctor extends User{
    private final String doctorID;

    // Constructors
    public Doctor(String doctorID, String name, String emailId, int age, Gender gender) {
        super(doctorID, name, Role.DOCTOR, emailId, age, gender);  // use the emailId from parameter
        this.doctorID = doctorID;
    }
    
    public String getDoctorID() {
        return doctorID;
    }
    
}

