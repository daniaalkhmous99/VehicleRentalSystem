public class Motorcycle extends Vehicle implements Maintainable {

    public static final double HELMET_PER_DAY = 5;

    private int engineCC;
    private boolean helmetAvailable;   // does this motorcycle come with a helmet we can add?

    public Motorcycle(String vehicleId, String brand, String model, int year,
                      double dailyPrice, int engineCC, boolean helmetAvailable) {
        super(vehicleId, brand, model, year, dailyPrice);
        if (engineCC <= 0) {
            throw new IllegalArgumentException("Engine capacity must be greater than 0.");
        }
        this.engineCC = engineCC;
        this.helmetAvailable = helmetAvailable;
    }

    // Overloaded constructor: helmet available by default
    public Motorcycle(String vehicleId, String brand, String model, int year,
                      double dailyPrice, int engineCC) {
        this(vehicleId, brand, model, year, dailyPrice, engineCC, true);
    }

    public int getEngineCC() { return engineCC; }
    public boolean isHelmetAvailable() { return helmetAvailable; }

    @Override
    public double calculateRentalCost(int days, boolean withHelmet) {
        double total = calculateBaseCost(days);
        if (withHelmet && helmetAvailable) {
            total = total + HELMET_PER_DAY * days;
        }
        return total;
    }

    @Override
    public void displayInfo() {
        System.out.print("[Motorcycle] ");
        displayCommonInfo();

        String helmetText;
        if (helmetAvailable) {
            helmetText = "Yes";
        } else {
            helmetText = "No";
        }
        System.out.println(" | Engine: " + engineCC + "cc | Helmet available: " + helmetText);
    }

    @Override
    public String getOptionalServiceName() {
        if (helmetAvailable) {
            return "helmet ($" + HELMET_PER_DAY + "/day)";
        }
        return null;   // no helmet, so no optional service
    }

    // ----- Maintainable -----
    @Override
    public void performMaintenance() {
        System.out.println("Motorcycle " + getVehicleId() + ": chain lubricated, engine and tires checked.");
        clearMaintenanceFlag();
    }

    @Override
    public boolean needsMaintenance() {
        return isMaintenanceRequired();
    }
}
