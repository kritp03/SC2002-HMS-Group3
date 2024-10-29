package HMS.main.users;

import java.time.LocalDate;
import java.util.List;

public class Patient
{
    private String patientId;
    private LocalDate dateOfBirth;
    private Gender gender;
    private ContactInformation contactinfo;
    private String bloodType;
    private List<MedicalRecord> medicalRecord;
    private List<Appointment> appointments;

    public List<MedicalRecord> viewMedicalRecords()
    {
        return this.medicalRecord;
    }
}