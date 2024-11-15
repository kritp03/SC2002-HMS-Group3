package HMS.src.appointment;

import java.util.List;

import HMS.src.io.ApptCsvHelper;

public class AppointmentManager {
    // public Prescription prescription = new Prescription();
    /**
     * View the appointment outcome record.
     * @param filePath The path to the CSV file.
     */
    public void viewApptOutcomeRecord(String filePath) {
        ApptCsvHelper apptCsvHelper = new ApptCsvHelper();
        List<String[]> data = apptCsvHelper.readCSV();
        if (data.isEmpty()) {
            System.out.println("No data found in CSV file.");
            return;
        }
        for (int i = 1; i < data.size(); i++) {
            String[] row = data.get(i);
            if (row.length >= 10) {
                // String coloredStatus = PrescriptionStatus.valueOf(row[8]).showStatusByColor();
                System.out.println("Appointment ID: " + row[0]);
                System.out.println("--------------------");
                System.out.println("Patient ID: " + row[1]);
                System.out.println("Dr ID: " + row[2]);
                System.out.println("Date of Appointment: " + row[3]);
                System.out.println("Appointment Time: " + row[4]);
                System.out.println("Service: " + row[5]);
                System.out.println("Medicine Name: " + row[6]);
                System.out.println("Dosage: " + row[7]);
                System.out.println("Status: " + row[8]);
                System.out.println("Notes: " + row[9]);
                System.out.println();
            }
        }
    }
}
