package HMS.src.management;

import java.util.UUID;

public class User1
{
    private final UUID userId;
    private final String name;
    private String password;
    private final Role role;
    private String emailId;


    public User1(String name, Role role,String emailId)
    {
        if(emailId.contains("@"))
        {
            this.userId = UUID.fromString(emailId.substring(0, emailId.indexOf("@")));
            this.emailId = emailId;
        }

        else
        {
            this.userId = UUID.fromString(name);
            this.emailId = "kritchanatppy@gmail.com";
        }
        this.name = name;
        this.password = "password";
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

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getEmailId() {
        return emailId;
    }

    
}