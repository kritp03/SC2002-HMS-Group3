package HMS.src.utils;

import java.util.Scanner;

public class InputScanner {

    private final Scanner scanner;
    private static InputScanner singleton = null;

    private InputScanner() {
        scanner = new Scanner(System.in);
    }

    public static InputScanner getInstance() {
        if (singleton == null) {
            singleton = new InputScanner();
        }
        return singleton;
    }

    public int nextInt() {
        return scanner.nextInt();
    }   

    public String nextLine() {
        return scanner.nextLine();
    }

    public String next(){
        return scanner.next();
    }

    public void close() {
        scanner.close();
    }
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
