public class Truck extends Vehicle implements Maintainable {

    public static final double SERVICE_FEE = 50;   // fixed fee per rental

    private double maxLoadTons;

    public Truck(String vehicleId, String brand, String model, int year,
                 double dailyPrice, double maxLoadTons) {
        super(vehicleId, brand, model, year, dailyPrice);
        if (maxLoadTons <= 0) {
            throw new IllegalArgumentException("Maximum load must be greater than 0.");
        }
        this.maxLoadTons = maxLoadTons;
    }

    public double getMaxLoadTons() { return maxLoadTons; }

    // The truck has no optional service, so the second parameter is not used.
    @Override
    public double calculateRentalCost(int days, boolean optionalService) {
        return calculateBaseCost(days) + SERVICE_FEE;
    }

    @Override
    public void displayInfo() {
        System.out.print("[Truck] ");
        displayCommonInfo();
        System.out.println(" | Max load: " + maxLoadTons + " tons");
    }

    @Override
    public String getOptionalServiceName() {
        return null;   // trucks have only the fixed service fee
    }

    // ----- Maintainable -----
    @Override
    public void performMaintenance() {
        System.out.println("Truck " + getVehicleId() + ": engine, brakes and cargo area inspected.");
        clearMaintenanceFlag();
    }

    @Override
    public boolean needsMaintenance() {
        return isMaintenanceRequired();
    }
}
