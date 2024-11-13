package HMS.src.prescription;

import java.util.List;

import HMS.src.io_new.PrescriptionCsvHelper;

public class PrescriptionManager {
    private PrescriptionCsvHelper prescriptionCsvHelper = new PrescriptionCsvHelper();
    /**
     * Displays all prescriptions in the CSV file.
     * @param filePath The path to the CSV file.
     */
    public void showAllPrescriptions(String filePath) {
        List<String[]> data = prescriptionCsvHelper.readCSV();
        if (data.isEmpty()) {
            System.out.println("No medications found in the file.");
        } else {
            System.out.println("+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
            System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s |\n", 
                              "Prescription ID", "Medicine Name", "Dosage", "Status", "Patient Name", "Requested By", "Date of Request", "Date of Approval");
            System.out.println("+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");

            for (int i = 1; i < data.size(); i++) {
                String[] row = data.get(i);
                if (row.length >= 8) {  
                    String coloredStatus = PrescriptionStatus.valueOf(row[3].toUpperCase()).showStatusByColor();
                    System.out.format("| %-14s | %-14s | %-7s | %-8s | %-14s | %-16s | %-12s | %-14s |\n",
                                      row[0], row[1], row[2], coloredStatus, row[4], row[5], row[6], row[7]);
                }
            }
            System.out.println("+----------------+----------------+---------+----------+----------------+------------------+--------------+----------------+");
        }
    }

    
    // public static void main(String[] args) {
    //     String filePath = "/Users/weipingtee/Library/CloudStorage/OneDrive-NanyangTechnologicalUniversity/Year 2/Sem 1/SC2002 Object Oriented Programming/Assignment/SC2002-HMS-Group3/HMS/data/Prescription_List.csv";
    //     showAllPrescriptions();
    // }
}
