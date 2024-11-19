package HMS.src.io;

import java.io.File;
import java.util.List;

/**
 * Helper class for managing availability records in a CSV file.
 * Provides methods for reading, writing, updating, and removing availability data.
 */
public class AvailabilityCsvHelper extends BaseCsvHelper {

    /**
     * The specific CSV file name for availability records.
     */
    protected String FILE_NAME = "Availability_List.csv";

    /**
     * Returns the name of the CSV file.
     * @return The name of the CSV file as a String.
     */
    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    /**
     * Reads all availability records from the CSV file.
     * @return A list of String arrays containing availability data.
     */
    public List<String[]> readCSV() {
        return readEntries();
    }

    /**
     * Gets the complete file path for the CSV file.
     * @return A String representing the full file path.
     */
    @Override
    public String getFilePath() {
        return new File(basePath, FILE_NAME).getAbsolutePath();
    }

    /**
     * Adds a new availability record to the CSV file.
     * @param availability The availability data to add as a String array.
     */
    public void addAvailability(String[] availability) {
        addEntry(availability);
    }

    /**
     * Removes an availability record by doctor ID and date.
     * @param doctorID The doctor ID used to identify the record.
     * @param date The date used to identify the record.
     */
    public void removeAvailability(String doctorID, String date) {
        List<String[]> entries = readEntries();
        entries.removeIf(entry -> entry[0].equals(doctorID) && entry[1].equals(date));
        writeEntries(entries);
    }

    /**
     * Updates an availability record for a specific doctor and date.
     * @param doctorID The doctor ID used to identify the record.
     * @param date The date used to identify the record.
     * @param newEntry The updated availability data as a String array.
     */
    public void updateAvailability(String doctorID, String date, String[] newEntry) {
        List<String[]> entries = readEntries();
        for (int i = 0; i < entries.size(); i++) {
            String[] entry = entries.get(i);
            if (entry[0].equals(doctorID) && entry[1].equals(date)) {
                entries.set(i, newEntry);
                break;
            }
        }
        writeEntries(entries);
    }

    /**
     * Retrieves all availability records for a specific doctor.
     * @param doctorID The doctor ID to filter records.
     * @return A list of String arrays containing availability data for the doctor.
     */
    public List<String[]> getAvailabilityByDoctor(String doctorID) {
        return getEntriesByFilter(entry -> entry[0].equals(doctorID));
    }
}

