package HMS.src.io_new;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ApptCsvHelper extends BaseCsvHelper{

    protected String FILE_NAME = "Appt_Outcome.csv";

    @Override
    protected String getFileName() {
        return FILE_NAME; 
    }

    /**
     * Reads a CSV file and parses each row into an array of strings.
     * @param fileName The path to the CSV file.
     * @return A List containing string arrays, each representing a row from the CSV file.
     */
    public List<String[]> readCSV() {
        return readEntries();
    }

    /**
     * Gets the complete file path for the CSV file managed by this helper.
     * @return String representing the full file path.
     */
    @Override
    public String getFilePath() {
        return new File(basePath, FILE_NAME).getAbsolutePath();
    }

    

}
