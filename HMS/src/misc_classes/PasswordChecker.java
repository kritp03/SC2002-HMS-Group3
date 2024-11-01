package HMS.src.misc_classes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordChecker
{
    public static boolean isSecurePassword(String password)
    {
        List <String> errors = new ArrayList<>();
        if(password.length() < 10)
        {
            errors.add("Length of password has to be at least 10!!");
        }

        Pattern specialChar = Pattern.compile("[^a-zA-Z0-9]");
        Matcher specialCharMatcher = specialChar.matcher(password);
        if(!specialCharMatcher.find())
        {
            errors.add("Password must include at least one special character!!");
        }

        Pattern alphaNumeric = Pattern.compile("[a-zA-Z0-9]+");
        Matcher alphaNumericMatcher = alphaNumeric.matcher(password);
        if(!alphaNumericMatcher.find())
        {
            errors.add("Password must contain both alphabets and numerical characters!!");
        }

        if(!errors.isEmpty())
        {
            for(String error: errors)
            {
                System.out.println(error);
            }
            return false;
        }

        return true;
    }
}