package HMS.src.io_new;

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
    // public static List<String[]> readCSV(String fileName) {
    //     List<String[]> data = new ArrayList<>();
    //     try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
    //         String line;
    //         while ((line = br.readLine()) != null) {
    //             String[] row = line.split(","); // Splits each line into parts using comma as delimiter.
    //             data.add(row);
    //         }
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    //     return data;
    // }
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
