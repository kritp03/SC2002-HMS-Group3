package HMS.src.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Base class for CSV handling, providing common functionalities to all CSV helpers.
 */
public abstract class BaseCsvHelper {

    protected String basePath = "HMS/data"; 

    /**
     * Each subclass should implement this to return the specific file name it manages.
     * @return The file name of the specific CSV file.
     */
    protected abstract String getFileName();

    /**
     * Gets the complete file path for the CSV file managed by this helper.
     * This method can be overridden by subclasses to specify different file paths.
     * @return String representing the full file path.
     */
    public String getFilePath() {
        // Default implementation - subclasses can override this to customize the file path
        return new File(basePath, getFileName()).getAbsolutePath();
    }

    /**
     * Reads the CSV file and returns a list of string arrays, each representing a row from the CSV file.
     * @return A List containing string arrays, each representing a row from the CSV file.
     */
    public List<String[]> readEntries() {
        List<String[]> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                entries.add(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1)); // Properly handles values in quotes
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entries;
    }

    /**
     * Writes the given list of string arrays to the CSV file.
     * @param entries
     */
    protected void writeEntries(List<String[]> entries) {
        try (PrintWriter writer = new PrintWriter(new File(getFilePath()))) {
            for (String[] entry : entries) {
                writer.println(String.join(",", entry));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new entry to the CSV file.
     * @param newEntry
     */
    public void addEntry(String[] newEntry) {
        List<String[]> entries = readEntries();
        entries.add(newEntry);
        writeEntries(entries);
    }

    /**
     * Removes an entry from the CSV file by ID.
     * @param id
     */
    public void removeEntryById(String id) {
        List<String[]> entries = readEntries();
        List<String[]> updatedEntries = entries.stream()
                                               .filter(e -> !e[0].equals(id)) // Assumes the ID is in the first column
                                               .collect(Collectors.toList());
        writeEntries(updatedEntries);
    }

    /**
     * Updates an entry in the CSV file by ID.
     * @param id
     * @param newEntry
     */
    public void updateEntry(String id, String[] newEntry) {
        List<String[]> entries = readEntries();
        List<String[]> updatedEntries = entries.stream()
                                               .map(e -> e[0].equals(id) ? newEntry : e)
                                               .collect(Collectors.toList());
        writeEntries(updatedEntries);
    }



    /**
     * Gets an entry from the CSV file by ID.
     * @param id
     * @return The entry as a string array, or null if not found.
     */
    public String[] getEntryById(String id) {
        List<String[]> entries = readEntries();
        return entries.stream()
                      .filter(e -> e[0].equals(id))
                      .findFirst()
                      .orElse(null);
    }

    /**
     * Gets all entries from the CSV file.
     * @return A list of string arrays, each representing a row from the CSV file.
     */
    public List<String[]> getEntriesByFilter(Filter<String[]> filter) {
        List<String[]> entries = readEntries();
        return entries.stream()
                      .filter(filter::matches)
                      .collect(Collectors.toList());
    }

    /**
     * Functional interface for filtering entries.
     * @param <T>
     */
    @FunctionalInterface
    public interface Filter<T> {
        boolean matches(T item);
    }

    
}