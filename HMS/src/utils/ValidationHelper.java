// package HMS.src.utils;

// import java.util.InputMismatchException;

// import HMS.src.exceptions.InvalidValueException;

// public class ValidationHelper {

//     public static int validateInt(String msg) {
//         InputScanner sc = InputScanner.getInstance();
//         boolean success = false;
//         int input = 0;
//         do {
//             try {
//                 System.out.print(msg);
//                 input = sc.nextInt();
//                 if (input >= 0)
//                     success = true;
//                 else
//                     System.out.println("Negative values are invalid.");
//             } catch (InputMismatchException e) {
//                 System.out.println("Please enter a valid integer.\n");
//                 sc.nextLine();
//             }
//         } while (!success);
//         return input;
//     }

//     public static int validateIntRange(String msg, int start, int end) {
//         InputScanner sc = InputScanner.getInstance();
//         boolean success = false;
//         int input = 0;
//         do {
//             try {
//                 input = validateInt(msg);
//                 if (start <= input && input <= end) {
//                     success = true;
//                 } else {
//                     throw new InvalidValueException("Invalid range! Please re-enter.\n");
//                 }
//             } catch (InvalidValueException e) {
//                 System.out.println(e.getMessage());
//                 sc.nextLine();
//             }
//         } while (!success);
//         return input;
//     }

// }

package HMS.src.utils;
import java.util.InputMismatchException;
import HMS.src.exceptions.InvalidValueException;

public class ValidationHelper {

    // Method to validate integer input with a prompt
    public static int validateInt(String msg) {
        InputScanner sc = InputScanner.getInstance();
        boolean success = false;
        int input = 0;
        do {
            try {
                System.out.print(msg);
                input = sc.nextInt();
                if (input >= 0)
                    success = true;
                else
                    System.out.println("Negative values are invalid.");
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer.\n");
                sc.nextLine();
            }
        } while (!success);
        return input;
    }

    // Method to validate integer input within a range
    public static int validateIntRange(String msg, int start, int end) {
        InputScanner sc = InputScanner.getInstance();
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

    // New Method to validate string input (non-empty)
    public static String validateString(String msg) {
        InputScanner sc = InputScanner.getInstance();
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

    // New Method to validate boolean input (Y/N)
    public static boolean validateBoolean(String msg) {
        InputScanner sc = InputScanner.getInstance();
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
}
