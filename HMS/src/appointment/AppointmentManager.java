package HMS.src.appointment;

import java.util.List;

import HMS.src.io.ApptCsvHelper;

public class AppointmentManager {
    ApptCsvHelper apptCsvHelper = new ApptCsvHelper();

    /**
     * View appointment outcome record
     */
    public void viewApptOutcomeRecord() {
        List<String[]> apptOutcome = apptCsvHelper.readCSV();

        if (apptOutcome.isEmpty()) {
            System.out.println("No appointment outcomes found.");
            return;
        }

        for (int i = 1; i < apptOutcome.size(); i++) {
            String[] row = apptOutcome.get(i);
            // String coloredStatus =
            // PrescriptionStatus.valueOf(row[8]).showStatusByColor();
            System.out.println("Appointment ID: " + row[0]);
            System.out.println("--------------------");
            System.out.println("Patient ID: " + row[1]);
            System.out.println("Dr ID: " + row[2]);
            System.out.println("Date of Appointment: " + row[3]);
            System.out.println("Appointment Time: " + row[4]);
            System.out.println("Service: " + row[5]);
            System.out.println("Medicine Name: " + row[6]);
            System.out.println("Dosage: " + row[7]);
            System.out.println("Notes: " + row[8]);
            System.out.println();
        }
    }
}
