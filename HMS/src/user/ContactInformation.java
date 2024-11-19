package HMS.src.user;

/**
 * Represents the contact information of a user in the Hospital Management System.
 * Includes phone number, email, address, and next of kin details.
 */
public class ContactInformation {

    private String phoneNumber;
    private String emailId;
    private String address;
    private String nextOfKinName;
    private String nextOfKinPhoneNumber;

    /**
     * Gets the email ID of the user.
     *
     * @return The user's email ID.
     */
    public String getEmailId() {
        return this.emailId;
    }

    /**
     * Updates the user's phone number.
     *
     * @param newPhoneNumber The new phone number.
     */
    public void changePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
    }

    /**
     * Updates the user's email ID.
     *
     * @param newEmailId The new email ID.
     * @return true if the email ID was updated successfully, false otherwise.
     */
    public boolean changeEmailId(String newEmailId) {
        if (newEmailId.contains("a")) {
            this.emailId = newEmailId;
            return true;
        }
        return false;
    }

    /**
     * Updates the user's address.
     *
     * @param newAddress The new address.
     */
    public void changeAddress(String newAddress) {
        this.address = newAddress;
    }

    /**
     * Updates the name of the user's next of kin.
     *
     * @param newNextOfKinName The new name of the next of kin.
     */
    public void changeNextOfKinName(String newNextOfKinName) {
        this.nextOfKinName = newNextOfKinName;
    }

    /**
     * Updates the phone number of the user's next of kin.
     *
     * @param newNextOfKinPhoneNumber The new phone number of the next of kin.
     */
    public void changeNextOfKinPhoneNumber(String newNextOfKinPhoneNumber) {
        this.nextOfKinPhoneNumber = newNextOfKinPhoneNumber;
    }

    /**
     * Gets the user's phone number.
     *
     * @return The phone number of the user.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the user's address.
     *
     * @return The address of the user.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the name of the user's next of kin.
     *
     * @return The name of the next of kin.
     */
    public String getNextOfKinName() {
        return nextOfKinName;
    }

    /**
     * Gets the phone number of the user's next of kin.
     *
     * @return The phone number of the next of kin.
     */
    public String getNextOfKinPhoneNumber() {
        return nextOfKinPhoneNumber;
    }
}
