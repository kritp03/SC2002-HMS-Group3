// package HMS.src.management;

// import HMS.src.misc_classes.Gender;
// import java.util.UUID;

// public class User {
//     protected UUID userID;
//     protected String name;
//     protected Role role;
//     private Gender gender;
//     protected String emailId;
//     private String password;
//     private int age;

//     public User(String userID, String name, Role role, String emailId, int age, Gender gender) {
//         this.userID = UUID.fromString(userID);
//         this.name = name;
//         this.role = role;
//         this.gender = gender;
//         if(emailId.contains("@"))
//         {
//             this.emailId = emailId;
//         }

//         else
//         {
//             this.emailId = "hms@hms.com";
//         }
//         this.age = age;
//         this.password = "password";
//     }

//     public UUID getUserID() {
//         return userID;
//     }

//     public void setUserID(String userID) {
//         this.userID = UUID.fromString(userID);
//     }

//     public String getName() {
//         return name;
//     }

//     public void setName(String name) {
//         this.name = name;
//     }

//     public Role getRole() {
//         return role;
//     }

//     public void setRole(Role role) {
//         this.role = role;
//     }

//     private boolean setPassword(String password)
//     {
//         this.password = password;
//         return true;
//     }

//     public boolean changePassword(String oldPassword, String newPassword)
//     {
//         if (oldPassword.equals(this.password))
//         {
//             setPassword(newPassword);
//         }

//         return false;
//     }

//     public boolean changeEmailId(String newEmailId,String oldEmailId)
//     {
//         if (this.emailId.equals(oldEmailId))
//         {
//             if(newEmailId.contains("@"))
//             {
//                 this.emailId = newEmailId;
//                 return true;
//             }
//         }

//         return false;
//     }

// }

package HMS.src.management;

import HMS.src.misc_classes.Gender;
import java.util.UUID;

public class User {
    protected String userID;
    protected String name;
    protected Role role;
    private Gender gender;
    protected String emailId;
    private String password;
    private int age;

    public User(String userID, String name, Role role, String emailId, int age, Gender gender) {
        this.userID = userID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        if(emailId.contains("@"))
        {
            this.emailId = emailId;
        }

        else
        {
            this.emailId = "hms@hms.com";
        }
        this.age = age;
        this.password = "password";
    }

    @Override
    public String toString() {
        return String.format("User ID: %s, Name: %s, Role: %s, Email: %s, Age: %d, Gender: %s", 
                             userID, name, role, emailId, age, gender);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getEmailId() {
        return emailId;
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