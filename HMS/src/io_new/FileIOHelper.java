package HMS.src.io_new;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileIOHelper {

    /**
     * Initializes a directory at the specified path "HMS/data". Creates it if it does not  exist.
     * @return A File object representing the directory.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static File init() {
        File folder = new File("HMS/data");
        if (!folder.exists()) {
            folder.mkdir();  // Create the directory if it does not exist
        }
        return folder;  // Return the File object for the directory
    }

    /**
     * Checks if a file exists at the given path.
     * @param filePath The path of the file to check.
     * @return true if the file exists, false otherwise.
     */
    public static boolean fileExists(String fileName) {
        File folder = init();
        File file = new File(folder.getAbsolutePath() + "/" + fileName);
        return file.exists();
    }


}