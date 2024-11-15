package HMS.src.user;

import HMS.src.appointment.*;
import HMS.src.io.ApptCsvHelper;
import HMS.src.prescription.Prescription;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DoctorManager {

    private SlotManager slotManager;
    private ApptCsvHelper apptCsvHelper;  // Helper for writing Appointment Outcomes to CSV

    // Constructor to initialize SlotManager and ApptCsvHelper
    public DoctorManager(SlotManager slotManager, ApptCsvHelper apptCsvHelper) {
        this.slotManager = slotManager;
        this.apptCsvHelper = apptCsvHelper;
    }

    // View available slots for the doctor
    public void viewAvailableSlots(String doctorID) {
        System.out.println("Available slots for Dr. " + doctorID + ":");
        slotManager.printSlots();
    }

    // Method to mark a slot as unavailable
    public void setUnavailable(LocalTime startTime) {
        slotManager.setAvailability(startTime, false);
    }

    // Method to record the outcome of an appointment
public void recordAppointmentOutcome(Appointment appointment, ServiceType serviceType, String diagnosis, 
                                     ArrayList<Prescription> prescriptions, String consultationNotes) {
    if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
        // Get the appointment ID
        String appointmentID = appointment.getAppointmentID();
        LocalDate appointmentDate = appointment.getDate(); 
        AppointmentOutcome outcome = new AppointmentOutcome(appointmentID, appointmentDate, serviceType, diagnosis, prescriptions, consultationNotes);
        appointment.setOutcome(outcome);

        // Write the outcome to the CSV
        writeAppointmentOutcomeToCSV(outcome);  // You need to implement this method to handle CSV writing
        System.out.println("Recorded appointment outcome for Appointment ID: " + appointment.getAppointmentID());
    } else {
        System.out.println("Cannot record outcome. Appointment is not completed.");
    }
}



    // Method to accept an appointment
    public void acceptAppointment(Appointment appointment, String doctorID) {
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " accepted by Dr. " + doctorID);
        } else {
            System.out.println("Cannot accept appointment. Current status: " + appointment.getStatus());
        }
    }

    // Method to decline an appointment
    public void declineAppointment(Appointment appointment, String doctorID) {
        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.DECLINED);
            System.out.println("Appointment ID " + appointment.getAppointmentID() + " declined by Dr. " + doctorID);
        } else {
            System.out.println("Cannot decline appointment. Current status: " + appointment.getStatus());
        }
    }

    // Helper method to write appointment outcome to CSV
    private void writeAppointmentOutcomeToCSV(AppointmentOutcome outcome) {
        List<String[]> appointmentOutcomes = new ArrayList<>();
        String appointmentID = outcome.getAppointmentID();
        String serviceType = outcome.getServiceType().toString(); 
        String appointmentDate = outcome.getAppointmentDate().toString();
        String consultationNotes = outcome.getConsultationNotes();

        // Convert the appointment outcome to a string array
        String[] outcomeData = { appointmentID, serviceType, appointmentDate, consultationNotes };
        appointmentOutcomes.add(outcomeData);

        // Write the list of appointment outcomes to the CSV
        apptCsvHelper.writeEntries(appointmentOutcomes);
        System.out.println("Appointment outcome written to CSV for Appointment ID: " + appointmentID);
    }
}
