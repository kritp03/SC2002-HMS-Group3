package HMS.src.user;

/**
 * Represents a user in the Hospital Management System (HMS).
 * Each user has a unique ID, a name, a role, and personal details such as age, gender, and email.
 * The class also provides methods to manage user attributes like passwords and email addresses.
 */
public class User {

    /**
     * Unique identifier for the user.
     */
    protected String userID;

    /**
     * Name of the user.
     */
    protected String name;

    /**
     * Role of the user (e.g., Patient, Doctor, Pharmacist, Administrator).
     */
    protected Role role;

    /**
     * Gender of the user.
     */
    private Gender gender;

    /**
     * Email address of the user.
     */
    protected String emailId;

    /**
     * Password for the user's account.
     */
    private String password;

    /**
     * Age of the user.
     */
    private int age;

    /**
     * Constructs a new User instance.
     *
     * @param userID   The unique identifier for the user.
     * @param name     The name of the user.
     * @param role     The role of the user (e.g., Patient, Doctor).
     * @param emailId  The email address of the user.
     *                 If the email address is invalid, it defaults to "hms@hms.com".
     * @param age      The age of the user.
     * @param gender   The gender of the user.
     */
    public User(String userID, String name, Role role, String emailId, int age, Gender gender) {
        this.userID = userID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.emailId = emailId.contains("@") ? emailId : "hms@hms.com";
        this.age = age;
        this.password = "password";
    }

    /**
     * Returns a string representation of the user.
     *
     * @return A formatted string with the user's details.
     */
    @Override
    public String toString() {
        return String.format("User ID: %s, Name: %s, Role: %s, Email: %s, Age: %d, Gender: %s", 
                             userID, name, role, emailId, age, gender);
    }

    // Getters and Setters

    /**
     * Retrieves the user's unique ID.
     *
     * @return The user ID.
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user's unique ID.
     *
     * @param userID The new user ID.
     */
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Retrieves the user's name.
     *
     * @return The user's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the user's name.
     *
     * @param name The new name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the user's role.
     *
     * @return The user's role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Updates the user's role.
     *
     * @param role The new role.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Retrieves the user's email address.
     *
     * @return The user's email address.
     */
    public String getEmailId() {
        return emailId;
    }

    /**
     * Updates the user's email address.
     *
     * @param newEmailId The new email address.
     * @param oldEmailId The current email address for verification.
     * @return {@code true} if the email was successfully updated; {@code false} otherwise.
     */
    public boolean changeEmailId(String newEmailId, String oldEmailId) {
        if (this.emailId.equals(oldEmailId) && newEmailId.contains("@")) {
            this.emailId = newEmailId;
            return true;
        }
        return false;
    }

    /**
     * Retrieves the user's age.
     *
     * @return The user's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Updates the user's age.
     *
     * @param age The new age.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Retrieves the user's gender.
     *
     * @return The user's gender.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Updates the user's gender.
     *
     * @param gender The new gender.
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    // Password Management

    /**
     * Updates the user's password (private helper method).
     *
     * @param password The new password.
     * @return {@code true} to indicate the password was updated successfully.
     */
    private boolean setPassword(String password) {
        this.password = password;
        return true;
    }

    /**
     * Changes the user's password.
     *
     * @param oldPassword The current password.
     * @param newPassword The new password.
     * @return {@code true} if the password was successfully changed; {@code false} otherwise.
     */
    public boolean changePassword(String oldPassword, String newPassword) {
        if (oldPassword.equals(this.password)) {
            return setPassword(newPassword);
        }
        return false;
    }
}
