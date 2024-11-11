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
}
