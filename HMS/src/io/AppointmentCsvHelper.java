package HMS.src.io;

import java.io.File;
import java.util.List;

public class AppointmentCsvHelper extends BaseCsvHelper {
    protected String FILE_NAME = "Appt_List.csv";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    /**
     * Reads all appointments from the CSV file.
     * @return List of String arrays containing appointment data
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
     * Updates an appointment in the CSV file.
     * @param appointment The appointment data to update
     */
    public void updateAppointment(String[] appointment) {
        addEntry(appointment);
    }
}
