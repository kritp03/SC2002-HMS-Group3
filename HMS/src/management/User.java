package HMS.src.management;

import java.util.UUID;

public class User {
    protected UUID userID;
    protected String name;
    protected Role role;
    protected String emailId;
    private String password;

    public User(String userID, String name, Role role, String emailId) {
        this.userID = UUID.fromString(userID);
        this.name = name;
        this.role = role;
        if(emailId.contains("@"))
        {
            this.emailId = emailId;
        }

        else
        {
            this.emailId = "hms@hms.com";
        }

        this.password = "password";
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = UUID.fromString(userID);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    private boolean setPassword(String password)
    {
        this.password = password;
        return true;
    }

    public boolean changePassword(String oldPassword, String newPassword)
    {
        if (oldPassword.equals(this.password))
        {
            setPassword(newPassword);
        }

        return false;
    }

    public boolean changeEmailId(String newEmailId,String oldEmailId)
    {
        if (this.emailId.equals(oldEmailId))
        {
            if(newEmailId.contains("@"))
            {
                this.emailId = newEmailId;
                return true;
            }
        }

        return false;
    }

}

enum Role {
    PATIENT,
    DOCTOR,
    PHARMACIST,
    ADMINISTRATOR
}
