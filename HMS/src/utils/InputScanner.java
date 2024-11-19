package HMS.src.utils;

import java.util.Scanner;

/**
 * Singleton utility class for managing console input operations.
 * Provides a centralized scanner instance to handle user input
 * throughout the application, ensuring resource efficiency and ease of use.
 */
public class InputScanner {

    /**
     * The scanner instance for reading user input from the console.
     */
    private final Scanner scanner;

    /**
     * Singleton instance of the InputScanner class.
     */
    private static InputScanner singleton = null;

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the scanner for console input.
     */
    private InputScanner() {
        scanner = new Scanner(System.in);
    }

    /**
     * Retrieves the singleton instance of the InputScanner class.
     * If no instance exists, a new one is created.
     *
     * @return The singleton instance of InputScanner.
     */
    public static InputScanner getInstance() {
        if (singleton == null) {
            singleton = new InputScanner();
        }
        return singleton;
    }

    /**
     * Reads the next integer value from the input.
     *
     * @return The integer value entered by the user.
     */
    public int nextInt() {
        return scanner.nextInt();
    }

    /**
     * Reads the next line of input as a string.
     *
     * @return The entire line entered by the user.
     */
    public String nextLine() {
        return scanner.nextLine();
    }

    /**
     * Reads the next token of input as a string.
     *
     * @return The next token entered by the user.
     */
    public String next() {
        return scanner.next();
    }

    /**
     * Closes the scanner instance to free up system resources.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Reads a boolean value from the input. 
     * Prompts the user to enter 'true' or 'false' until a valid input is provided.
     *
     * @return The boolean value entered by the user.
     */
    public boolean nextBoolean() {
        System.out.print("Please enter 'true' or 'false': ");
        String input = scanner.nextLine().toLowerCase();

        // Keep prompting the user until valid input is received
        while (!input.equals("true") && !input.equals("false")) {
            System.out.print("Invalid input. Please enter 'true' or 'false': ");
            input = scanner.nextLine().toLowerCase();
        }

        return Boolean.parseBoolean(input);
    }
}
