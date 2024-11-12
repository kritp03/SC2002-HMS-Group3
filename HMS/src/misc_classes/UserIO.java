package HMS.src.misc_classes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserIO 
{
    private static final Scanner sc = new Scanner(System.in);
    

    public static int getChoice(int lowestChoice, int highestChoice)
    {
        int choice;
        do {
            while(!sc.hasNextInt())
            {
                System.out.println("Invalid Choice! Please enter a valid integer.");
                sc.nextLine();
                System.out.print("Select a choice: ");
            }
            choice = sc.nextInt();
            sc.nextLine();

            if (choice < lowestChoice || choice > highestChoice)
            {
                System.out.println("Choice are between " + lowestChoice + " and " + highestChoice + " only! ");
            }
        } while (choice < lowestChoice || choice > highestChoice);

        return choice;
    }

    public static LocalDate getDate()
    {
        LocalDate date = null;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("YYYY-MM-DD"); 

        boolean isValid = false;
        while (!isValid) 
        {
            String input = sc.nextLine();

            try {
                date = LocalDate.parse(input, dateFormatter);
                isValid = true;
            } catch (Exception e) {
                System.out.print("Invalid date format. Please enter a date in the format YYYY-MM-DD: ");
            }
        }

        return date;
    }

    public static int getInt()
    {
        int response;
        while (true) 
        {
            try 
            {
                response = sc.nextInt();
                sc.nextLine();
                break;
            } catch (InputMismatchException e) 
            {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next();
            }
        }
        return response;
    }

    public static String getString()
    {
        return sc.nextLine();
    }

    public static boolean getBoolean()
    {
        while (true)
        {
            String input = getString();
            if(input.equalsIgnoreCase("Y"))
            {
                return true;
            }
            if(input.equalsIgnoreCase("N"))
            {
                return false;
            }
            System.out.println("Invalid input. Please enter Y or y for yes, N or n for no.");
        }
    }

    public static void close() 
    {
        sc.close();
    }
    
}
