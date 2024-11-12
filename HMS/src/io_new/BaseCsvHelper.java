package HMS.src.io_new;

import java.io.File;

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

    
}