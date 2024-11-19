package HMS.src.exceptions;

/**
 * Custom exception class to indicate that a list is empty when it should not be.
 */
public class EmptyListException extends Exception {

    /**
     * Constructor for the EmptyListException class.
     *
     * @param message The detailed error message describing the cause of the exception.
     */
    public EmptyListException(String message) {
        super(message);
    }
}
