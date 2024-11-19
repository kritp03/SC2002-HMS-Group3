package HMS.src.exceptions;

/**
 * Custom exception class to indicate that an invalid value has been provided.
 */
public class InvalidValueException extends Exception {

    /**
     * Constructor for the InvalidValueException class.
     *
     * @param message The detailed error message describing the cause of the exception.
     */
    public InvalidValueException(String message) {
        super(message);
    }
}

