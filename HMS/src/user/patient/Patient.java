package HMS.src.user.patient;

import HMS.src.appointment.*;
import HMS.src.medicalrecordsPDT.*;
import HMS.src.user.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;


public class Patient extends User
{
    private final String patientId;
    private LocalDate dateOfBirth;
    private ContactInformation contactinfo;
    private final String bloodType;
    private List<MedicalRecord> medicalRecord;
    private List<Appointment> appointment;

    public Patient(String name, String patientId, LocalDate dateOfBirth, Gender gender, ContactInformation contactInfo, String bloodType, List<MedicalRecord> medicalRecord, List<Appointment> appointment)
    {
        super(patientId,name,Role.PATIENT,contactInfo.getEmailId(),calculateAge(dateOfBirth), gender);
        this.patientId = patientId;
        this.dateOfBirth = dateOfBirth;
        this.contactinfo = contactInfo;
        this.bloodType = bloodType;
        this.medicalRecord = medicalRecord;
        this.appointment = appointment;
    }

    public Patient(String patientID, String name, LocalDate dateOfBirth, Gender gender, String bloodType, ContactInformation contactEmail)
    {
        super(patientID, name, Role.PATIENT,contactEmail.getEmailId(),calculateAge(dateOfBirth), gender);
        this.bloodType = bloodType;
        this.patientId = patientID;
    }

    public List<MedicalRecord> viewMedicalRecords()
    {
        return this.medicalRecord;
    }
 
    public void changePhoneNumber(String newPhoneNumber)
    {
        contactinfo.changePhoneNumber(newPhoneNumber);
    }

    public void changeEmailId(String newEmailId)
    {
        contactinfo.changeEmailId(newEmailId);
    }

    public void changeAddress(String newAdress)
    {
        contactinfo.changeAddress(newAdress);
    }

    public void changeNextOfKinname(String newNextOfKinName)
    {
        contactinfo.changeNextOfKinName(newNextOfKinName);
    }

    public void changeNextOfKinPhoneNumber(String newPhoneNumber)
    {
        contactinfo.changeNextOfKinPhoneNumber(newPhoneNumber);
    }

    private static int calculateAge(LocalDate dateOfBirth) {
        if (dateOfBirth == null) {
            return 0; 
        }
        LocalDate today = LocalDate.now();
        return Period.between(dateOfBirth, today).getYears();
    }

    // public List<Slot> viewAvailableAppointment(String doctorname)
    // {
    //     List <Appointment> availableAppointments = new ArrayList<>();

        

    // }

    // public List<Appointment> scheduleAppointment(Doctor doctor, LocalDate apptDate,LocalTime apptTime)
    // {

    // }

    // public List<Appointment> rescheduleAppointment(Doctor newDoctor, Doctor oldDoctor, LocalDate oldApptDate, LocalDate newApptDate, LocalTime oldApptTime, LocalTime newApptTime)
    // {

    // }

    // public List<Appointment> cancelAppointment(Doctor doctor, LocalDate apptDate, LocalTime apptTime)
    // {

    // }

    // public List<AppointmentStatus> viewAppointmentStatus()
    // {
        
    // }

    // public List<AppointmentOutcome> viewAppointmentOutcome()
    // {

    // }

    // public List<Prescription> viewPrescription()
    // {
        
    // }
}