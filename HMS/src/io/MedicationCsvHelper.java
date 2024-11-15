package HMS.src.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class MedicationCsvHelper extends BaseCsvHelper{

    protected String FILE_NAME = "Medicine_List.csv";

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
     * Utilizes the base class method to write an entries to the CSV file.
     * @param medications A List of String arrays where each array represents a prescription entry.
     */ 
    public void updateMedications(List<String[]> medications) {
        writeEntries(medications);
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
