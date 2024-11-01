package HMS.src.management;

import HMS.src.MedicalRecord.*;
import HMS.src.appointment.*;
import HMS.src.misc_classes.*;
import HMS.src.pharmacy.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

public class Patient extends User
{
    private final String name;
    private final String patientId;
    private LocalDate dateOfBirth;
    private Gender gender;
    private ContactInformation contactinfo;
    private final String bloodType;
    private List<MedicalRecord> medicalRecord;
    private List<Appointment> appointment;

    public Patient(String name, String patientId, LocalDate dateOfBirth, Gender gender, ContactInformation contactInfo, String bloodType, List<MedicalRecord> medicalRecord, List<Appointment> appointment)
    {
        super(patientId,name,Role.PATIENT,contactInfo.getEmailId());
        this.name = name;
        this.patientId = patientId;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactinfo = contactInfo;
        this.bloodType = bloodType;
        this.medicalRecord = medicalRecord;
        this.appointment = appointment;
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

    public void changeNextOfKinName(String newNextOfKinName)
    {
        contactinfo.changeNextOfKinName(newNextOfKinName);
    }

    public void changeNextOfKinPhoneNumber(String newPhoneNumber)
    {
        contactinfo.changeNextOfKinPhoneNumber(newPhoneNumber);
    }

    public List<Slot> viewAvailableAppointment(String doctorName)
    {
<<<<<<< HEAD
         // Find the doctor by name
        for (Doctor doctor : allDoctors) {
            if (doctor.getName().equalsIgnoreCase(doctorName)) {
                // Return the available slots if the doctor is found
                return doctor.viewAvailableSlots();
            }
        }
        
        // If no doctor with the specified name is found, return an empty list
        System.out.println("Doctor with name " + doctorName + " not found.");
        return new ArrayList<>();
    
=======
        List <Appointment> availableAppointments = new ArrayList<>();

        

>>>>>>> b1a88a4567b4dbcdc12fa4f73c4e8d890eb00e1b
    }

    public List<Appointment> scheduleAppointment(Doctor doctor, LocalDate apptDate,LocalTime apptTime)
    {

    }

    public List<Appointment> rescheduleAppointment(Doctor newDoctor, Doctor oldDoctor, LocalDate oldApptDate, LocalDate newApptDate, LocalTime oldApptTime, LocalTime newApptTime)
    {

    }

    public List<Appointment> cancelAppointment(Doctor doctor, LocalDate apptDate, LocalTime apptTime)
    {

    }

    public List<AppointmentStatus> viewAppointmentStatus()
    {
        
    }

    public List<AppointmentOutcome> viewAppointmentOutcome()
    {

    }

    public List<Prescription> viewPrescription()
    {
        
    }
}