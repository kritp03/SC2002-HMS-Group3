package HMS.src.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */
public class ReplReqCsvHelper extends BaseCsvHelper{

    protected String FILE_NAME = "Replenishment_Request.csv";

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

    /**
     * Updates a replenishment request in the CSV file.
     * @param requestID The ID of the request to update
     * @param status The new status (APPROVED/REJECTED)
     * @param approverName The name of the approver
     * @param approvalDate The date of approval/rejection
     */
    public void updateRequest(String requestID, String status, String approverName, String approvalDate) {
        List<String[]> requests = readCSV();
        for (String[] request : requests) {
            if (request[0].equals(requestID)) {
                request[4] = status;
                request[5] = approverName;
                request[6] = approvalDate;
                updateEntry(requestID, request);
                break;
            }
        }
    }

    /**
     * Adds a new replenishment request to the CSV file.
     * @param replReq A string array representing the replenishment request entry.
     */
    public void addReplReq(String[] replReq) {
        addEntry(replReq);
    }
}
