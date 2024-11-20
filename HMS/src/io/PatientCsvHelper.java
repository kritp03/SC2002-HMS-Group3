package HMS.src.io;

import java.io.File;
import java.util.List;

public class PatientCsvHelper extends BaseCsvHelper {

    protected String FILE_NAME = "Patient_List.csv";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    /**
     * Reads a CSV file and parses each row into an array of strings.
     * 
     * @param fileName The path to the CSV file.
     * @return A List containing string arrays, each representing a row from the CSV
     *         file.
     */
    public List<String[]> readCSV() {
        return readEntries();
    }

    /**
     * Utilizes the base class method to write an entries to the CSV file.
     * 
     * @param id           Represents the PatientID to be updated
     * @param updatedEntry A String array representing the updated entry.
     */
    public void updateContactInfo(String id, String[] updatedEntry) {
        updateEntry(id, updatedEntry);
    }

    /**
     * Gets the complete file path for the CSV file managed by this helper.
     * 
     * @return String representing the full file path.
     */
    @Override
    public String getFilePath() {
        return new File(basePath, FILE_NAME).getAbsolutePath();
    }

}
