package HMS.src.utils;

/**
 * Interface defining the contract for input validation.
 * @param <T> The type of value to be validated
 * @param <R> The type of value to be returned after validation
 */
public interface IValidator<T, R> {
    /**
     * Validates the input and returns the validated value.
     *
     * @param input The input to validate
     * @param prompt The prompt message to display
     * @return The validated value
     */
    R validate(T input, String prompt);
}
