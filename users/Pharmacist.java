package users;

public class Pharmacist 
{
    private String pharmacistId;

    public Pharmacist(String pharmacistId)
    {
        this.pharmacistId = pharmacistId;

    }

    public OutcomeRecord viewAppointmentOutcomeRecord(String appointmentId)
    {
        
    }

    public boolean updateStatusofPresecription(String appointmentId,Status status)
    {
        return true;
    }

    public List<Medication> viewMedicalInventory()
    {
        return List<Medication>;
    }
}
