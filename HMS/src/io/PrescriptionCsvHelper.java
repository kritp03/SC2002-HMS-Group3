package HMS.src.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class PrescriptionCsvHelper extends BaseCsvHelper{

    protected String FILE_NAME = "Prescription_List.csv";

    @Override
    protected String getFileName() {
        return FILE_NAME; 
    }

    // /**
    //  * Reads a CSV file and parses each row into an array of strings.
    //  * @param fileName The path to the CSV file.
    //  * @return A List containing string arrays, each representing a row from the CSV file.
    //  */
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

     /**
     * Utilizes the base class method to get all entries from the CSV file.
     * @return A List of String arrays where each array represents a prescription entry.
     */
    public List<String[]> getAllPrescriptions() {
        return readEntries(); 
    }
    
    /**
     * Utilizes the base class method to write an entries to the CSV file.
     * @param prescriptions A List of String arrays where each array represents a prescription entry.
     */
    public void addPrescriptions(List<String[]> prescriptions) {
        writeEntries(prescriptions);
    }
    
    /**
     * Utilizes the base class method to write an entries to the CSV file.
     * @param prescriptions A List of String arrays where each array represents a prescription entry.
     */ 
    public void updatePrescriptions(List<String[]> prescriptions) {
        writeEntries(prescriptions);
    }

    /**
     * Removes an entry from the CSV file by prescription ID.
     * @param prescriptionID The ID of the prescription to be removed.
     */
    public void removePrescriptionById(String prescriptionID) {
        removeEntryById(prescriptionID);
    }

    /**
     * Retrieves a prescription entry from the CSV file by prescription ID.
     * @param prescriptionID The ID of the prescription to be retrieved.
     * @return A String array representing the prescription entry.
     */
    public String[] getPrescriptionById(String prescriptionID) {
        return getEntryById(prescriptionID);
    }

    /**
     * Updates a prescription entry in the CSV file by prescription ID.
     * @param prescriptionID The ID of the prescription to be updated.
     * @param updatedPrescription A String array representing the updated prescription entry.
     */
    public void updatePrescriptionById(String prescriptionID, String[] updatedPrescription) {
        updateEntry(prescriptionID, updatedPrescription);
    }


}
