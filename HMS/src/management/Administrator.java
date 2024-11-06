package HMS.src.management;

import HMS.src.misc_classes.*;

public class Administrator extends User
{
    public Administrator(String userID, String name, String emailId, int age, Gender gender)
    {
        super(userID, name, Role.PHARMACIST, emailId, age, gender);
    }
}
