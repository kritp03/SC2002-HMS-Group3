package HMS.src.authorisation;

/**
 * Interface defining the core password management operations in the HMS system.
 * Follows the Interface Segregation Principle by providing focused password-related operations.
 */
public interface IPasswordManager {
    /**
     * Changes the password of the currently logged in user.
     */
    void changePassword();

    /**
     * Authenticates the user with the given userId and hashed password.
     * @param userId The user ID to authenticate
     * @param hashedPassword The hashed password to verify
     * @return true if authentication successful, false otherwise
     */
    boolean authenticate(String userId, String hashedPassword);

    /**
     * Prompts the user to enter a password with the given prompt.
     * @param prompt The prompt to display to the user
     * @return The entered password
     */
    String getPassword(String prompt);

    /**
     * Hashes the given password using a secure hashing algorithm.
     * @param password The password to hash
     * @return The hashed password
     */
    String hashPassword(String password);
}
