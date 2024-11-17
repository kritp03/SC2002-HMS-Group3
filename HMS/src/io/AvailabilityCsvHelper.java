package HMS.src.io;

import java.io.File;
import java.util.List;

public class AvailabilityCsvHelper extends BaseCsvHelper
{
    // The specific CSV file name for unavailability records
    protected String FILE_NAME = "Availability_List.csv";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    /**
     * Reads all unavailability records from the CSV file.
     * @return List of String arrays containing unavailability data
     */
    public List<String[]> readCSV() {
        return readEntries();
    }

    /**
     * Gets the complete file path for the CSV file.
     * @return String representing the full file path
     */
    @Override
    public String getFilePath() {
        return new File(basePath, FILE_NAME).getAbsolutePath();
    }

    /**
     * Adds a new unavailability record to the CSV file.
     * @param unavailability The unavailability data to add
     */
    public void addUnavailability(String[] unavailability) {
        addEntry(unavailability);
    }

    /**
     * Removes an unavailability record by doctor ID and date.
     * @param doctorID The doctor ID to identify the record
     * @param date The date to identify the record
     */
    public void removeUnavailability(String doctorID, String date) {
        List<String[]> entries = readEntries();
        entries.removeIf(entry -> entry[0].equals(doctorID) && entry[1].equals(date)); // Assumes columns: doctorID, date, time
        writeEntries(entries);
    }

    /**
     * Updates an unavailability record for a specific doctor and date.
     * @param doctorID The doctor ID to identify the record
     * @param date The date to identify the record
     * @param newEntry The updated unavailability data
     */
    public void updateUnavailability(String doctorID, String date, String[] newEntry) {
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
     * Retrieves all unavailability records for a specific doctor.
     * @param doctorID The doctor ID to filter records
     * @return List of String arrays containing unavailability data for the doctor
     */
    public List<String[]> getUnavailabilityByDoctor(String doctorID) {
        return getEntriesByFilter(entry -> entry[0].equals(doctorID));
    }
}
