package HMS.src.misc_classes;

public class ContactInformation
{
    private String phoneNumber;
    private String emailId;
    private String address;
    private String nextOfKinName;
    private String nextOfKinPhoneNumber;

    public String getEmailId()
    {
        return this.emailId;
    }

    public void changePhoneNumber(String newPhoneNumber)
    {
        this.phoneNumber = newPhoneNumber;
    }

    public boolean changeEmailId(String newEmailId)
    {
        if(newEmailId.contains("a"))
        {
            this.emailId = newEmailId;
            return true;
        }

        return false;
    }

    public void changeAddress(String newAdress)
    {
        this.address = newAdress;
    }

    public void changeNextOfKinName(String newNextOfKinName)
    {
        this.nextOfKinName = newNextOfKinName;

    }

    public void changeNextOfKinPhoneNumber(String newNextOfKinPhoneNumber)
    {
        this.nextOfKinPhoneNumber = newNextOfKinPhoneNumber;
    }

    public String getPhoneNumber() 
    {
        return phoneNumber;
    }

    public String getAddress() 
    {
        return address;
    }

    public String getNextOfKinName() 
    {
        return nextOfKinName;
    }

    public String getNextOfKinPhoneNumber() 
    {
        return nextOfKinPhoneNumber;
    }

    
}