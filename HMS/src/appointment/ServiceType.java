package HMS.src.appointment;

/**
 * Enum representing the types of services provided during appointments.
 */
public enum ServiceType {
    CONSULTATION,      /**< General consultation with the doctor. */
    FOLLOW_UP,         /**< Follow-up consultation for ongoing treatment or check-ups. */
    LAB_TEST,          /**< Laboratory tests such as blood tests or diagnostics. */
    IMAGING,           /**< Imaging services like X-rays, MRIs, or CT scans. */
    VACCINATION,       /**< Administration of vaccines. */
    MINOR_PROCEDURE,   /**< Minor medical procedures performed in the clinic. */
    SURGERY;           /**< Surgical procedures, including both minor and major operations. */
}

