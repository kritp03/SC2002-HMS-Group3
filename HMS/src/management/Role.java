package HMS.src.management;

public enum Role 
{
    PATIENT,
    DOCTOR,
    PHARMACIST,
    ADMINISTRATOR;
    
public static Role valueOfIgnoreCase(String roleName) 
    {
        for (Role role : Role.values()) 
        {
            if (role.name().equalsIgnoreCase(roleName)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + roleName);
    }
}
