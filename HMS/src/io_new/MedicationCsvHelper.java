package HMS.src.io_new;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import HMS.src.medication.Medication;


public class MedicationCsvHelper{
    /**
     * Reads a CSV file and parses each row into an array of strings.
     * @param fileName The path to the CSV file.
     * @return A List containing string arrays, each representing a row from the CSV file.
     */
    public static List<String[]> readCSV(String fileName) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(","); // Splits each line into parts using comma as delimiter.
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }


}
