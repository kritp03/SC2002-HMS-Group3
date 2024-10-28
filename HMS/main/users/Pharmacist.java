package HMS.main.users;

import HMS.main.appointment.*;
import HMS.main.misc_classes.*;

public class Pharmacist 
{
    private String pharmacistId;

    public Pharmacist(String pharmacistId)
    {
        this.pharmacistId = pharmacistId;

    }

    public AppointmentOutcomeRecord viewAppointmentOutcomeRecord(String appointmentId)
    {
        
    }

    public boolean updateStatusofPresecription(String appointmentId,AppointmentStatus status)
    {
        return true;
    }

    public List<Medication> viewMedicalInventory()
    {
        return List<Medication>;
    }
}
