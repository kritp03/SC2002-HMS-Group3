package HMS.src.pharmacy;

public class Medication {
    private String medicationID;
    private String name;
    private DosageForm dosageForm;
    private int concentrationMg;
    private int inventoryStock;
    private int lowThreshold;

    public Medication(String medicineName, int initialStock, int lowStockLevel)
    {
        this.name = medicineName;
        this.inventoryStock = initialStock;
        this.lowThreshold = lowStockLevel;
    }

    public Medication(String medicationID, String name, DosageForm dosageForm, int concentrationMg, int inventoryStock, int lowThreshold) {
        this.medicationID = medicationID;
        this.name = name;
        this.dosageForm = dosageForm;
        this.concentrationMg = concentrationMg;
        this.inventoryStock = inventoryStock;
        this.lowThreshold = lowThreshold;
    }

    public String getMedicationID() {
        return medicationID;
    }

    public void setMedicationID(String medicationID) {
        this.medicationID = medicationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DosageForm getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(DosageForm dosageForm) {
        this.dosageForm = dosageForm;
    }

    public int getConcentrationMg() {
        return concentrationMg;
    }

    public void setConcentrationMg(int concentrationMg) {
        this.concentrationMg = concentrationMg;
    }

    public int getInventoryStock() {
        return inventoryStock;
    }

    public void setInventoryStock(int inventoryStock) {
        this.inventoryStock = inventoryStock;
    }

    public int getLowThreshold() {
        return lowThreshold;
    }

    public void setLowThreshold(int lowThreshold) {
        this.lowThreshold = lowThreshold;
    }
}

enum DosageForm {
    TABLET, 
    CAPSULE, 
    LIQUID, 
    INJECTION,
    CREAM,
    INHALER, 
    SUPPOSITORY;
}