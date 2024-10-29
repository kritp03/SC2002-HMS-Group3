package HMS.src.management;

public class User {
    protected String userID;
    protected String name;
    protected Role role;

    public User(String userID, String name, Role role) {
        this.userID = userID;
        this.name = name;
        this.role = role;
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

}

enum Role {
    PATIENT,
    DOCTOR,
    PHARMACIST,
    ADMINISTRATOR
}
