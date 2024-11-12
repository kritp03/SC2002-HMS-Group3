package HMS.src.utils;

import java.util.InputMismatchException;

import HMS.src.exceptions.InvalidValueException;

public class ValidationHelper {

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

}
