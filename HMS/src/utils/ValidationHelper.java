package HMS.src.utils;

import HMS.src.exceptions.InvalidValueException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;

/**
 * Utility class for input validation.
 * Implements various validation strategies while adhering to the Open-Closed Principle.
 */
public class ValidationHelper implements IValidator<String, Object> {
    private static ValidationHelper instance;
    private final InputScanner sc;

    public ValidationHelper() {
        sc = InputScanner.getInstance();
    }

    public static ValidationHelper getInstance() {
        if (instance == null) {
            instance = new ValidationHelper();
        }
        return instance;
    }

    @Override
    public Object validate(String input, String prompt) {
        // This method is not directly used as we have specialized validation methods
        throw new UnsupportedOperationException("Please use specific validation methods");
    }

    /**
     * Validates and returns a positive integer input from the user.
     *
     * @param msg The message to prompt the user for input.
     * @return A valid positive integer entered by the user.
     */
    public int validateInt(String msg) {
        boolean success = false;
        int input = 0;
        do {
            try {
                System.out.print(msg);
                input = sc.nextInt();
                if (input >= 0) {
                    success = true;
                } else {
                    System.out.println("Negative values are invalid.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.\n");
                sc.nextLine();
            }
        } while (!success);
        return input;
    }

    /**
     * Validates and returns an integer input within a specified range.
     *
     * @param msg   The message to prompt the user for input.
     * @param start The start of the valid range (inclusive).
     * @param end   The end of the valid range (inclusive).
     * @return A valid integer within the specified range.
     */
    public int validateIntRange(String msg, int start, int end) {
        boolean success = false;
        int input = 0;
        do {
            try {
                input = validateInt(msg);
                if (start <= input && input <= end) {
                    success = true;
                } else {
                    throw new InvalidValueException("Invalid range! Please re-enter.\n");
                }
            } catch (InvalidValueException e) {
                System.out.println(e.getMessage());
                sc.nextLine();
            }
        } while (!success);
        return input;
    }

    /**
     * Validates and returns a non-empty string input from the user.
     *
     * @param msg The message to prompt the user for input.
     * @return A valid non-empty string entered by the user.
     */
    public String validateString(String msg) {
        String input;
        do {
            System.out.print(msg);
            input = sc.nextLine();
            if (input.trim().isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid string.");
            }
        } while (input.trim().isEmpty());
        return input;
    }

    /**
     * Validates and returns a boolean input from the user, interpreted as "Y" for true and "N" for false.
     *
     * @param msg The message to prompt the user for input.
     * @return {@code true} if the user inputs "Y", {@code false} if the user inputs "N".
     */
    public boolean validateBoolean(String msg) {
        String input;
        do {
            System.out.print(msg + " (Y/N): ");
            input = sc.nextLine().trim().toUpperCase();
            if (input.equals("Y")) {
                return true;
            } else if (input.equals("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        } while (true);
    }

    /**
     * Validates and returns a date input from the user in the "yyyy-MM-dd" format.
     *
     * @param msg The message to prompt the user for input.
     * @return A valid {@link LocalDate} entered by the user.
     */
    public LocalDate validateDate(String msg) {
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean success = false;

        do {
            try {
                System.out.print(msg);
                String input = sc.nextLine().trim();
                date = LocalDate.parse(input, formatter);
                success = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in 'yyyy-MM-dd' format.");
            }
        } while (!success);

        return date;
    }

    /**
     * Validates and returns a valid email address.
     *
     * @param msg The message to prompt the user for input.
     * @return A valid email address entered by the user.
     */
    public String validateEmail(String msg) {
        String email;
        boolean isValid = false;

        do {
            System.out.print(msg);
            email = sc.nextLine().trim();
            
            if (email == null || email.isEmpty()) {
                System.out.println("Email cannot be empty. Please try again.");
                continue;
            }
            
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.(com)$";
            if (!email.matches(emailRegex)) {
                System.out.println("Invalid email format. Please enter a valid email address with a proper domain (e.g., .com).");
                continue;
            }
            
            isValid = true;
        } while (!isValid);
        
        return email;
    }

    /**
     * Validates and returns an age input between 18 and 100 inclusive.
     *
     * @param msg The message to prompt the user for input.
     * @return A valid age between 18 and 100.
     */
    public int validateAge(String msg) {
        boolean success = false;
        int age = 0;
        do {
            try {
                System.out.print(msg);
                age = sc.nextInt();
                if (age >= 18 && age <= 100) {
                    success = true;
                } else {
                    System.out.println("Age must be between 18 and 100 years old.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.\n");
                sc.nextLine();
            }
        } while (!success);
        return age;
    }
}
