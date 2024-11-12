package HMS.src.exceptions;

public class ItemNotFoundException extends Exception {
    /**
     * Constructs a new ItemNotFoundException with the specified detail message.
     *
     * @param message The detail message to be printed when an ItemNotFoundException exception occurs.
     */
    public ItemNotFoundException(String message) {
        super(message);
    }
}
