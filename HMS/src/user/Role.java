package HMS.src.user;

/**
 * Represents the roles available in the Hospital Management System.
 * These roles define the type of user and their access level within the system.
 */
public enum Role {
    /**
     * Role for patients.
     */
    PATIENT,

    /**
     * Role for doctors.
     */
    DOCTOR,

    /**
     * Role for pharmacists.
     */
    PHARMACIST,

    /**
     * Role for administrators.
     */
    ADMINISTRATOR;

    /**
     * Returns the {@code Role} instance matching the provided role name,
     * ignoring case considerations.
     *
     * @param roleName The name of the role (case-insensitive).
     * @return The corresponding {@link Role} instance.
     * @throws IllegalArgumentException if no matching role is found.
     */
    public static Role valueOfIgnoreCase(String roleName) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + roleName);
    }
}
