package HMS.main.users;

import HMS.main.misc_classes.*;
import HMS.main.appointment.*;

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
}