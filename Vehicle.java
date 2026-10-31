// Abstract parent class for Car, Motorcycle and Truck.
// We cannot create an object from it directly (new Vehicle(...) is not allowed).
public abstract class Vehicle {

    // Private fields (encapsulation)
    private String vehicleId;
    private String brand;
    private String model;
    private int year;
    private double dailyPrice;
    private boolean available;            // false while the vehicle is rented
    private boolean maintenanceRequired;  // independent from "available"

    // static: shared by ALL vehicles, it counts how many vehicle objects were created
    private static int totalVehiclesCreated = 0;

    // Main constructor
    public Vehicle(String vehicleId, String brand, String model, int year,
                   double dailyPrice, boolean available) {
        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("Vehicle ID cannot be empty.");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be empty.");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be empty.");
        }
        if (year < 1950 || year > 2030) {
            throw new IllegalArgumentException("Year must be between 1950 and 2030.");
        }
        this.vehicleId = vehicleId.trim();
        this.brand = brand.trim();
        this.model = model.trim();
        this.year = year;
        setDailyPrice(dailyPrice);   // we reuse the setter because it checks the price
        this.available = available;
        this.maintenanceRequired = false;
        totalVehiclesCreated++;
    }

    // Overloaded constructor: same as above but the vehicle is available by default
    public Vehicle(String vehicleId, String brand, String model, int year, double dailyPrice) {
        this(vehicleId, brand, model, year, dailyPrice, true);
    }

    // ---------- Getters ----------
    public String getVehicleId() { return vehicleId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getDailyPrice() { return dailyPrice; }
    public boolean isAvailable() { return available; }
    public boolean isMaintenanceRequired() { return maintenanceRequired; }

    public static int getTotalVehiclesCreated() { return totalVehiclesCreated; }

    // ---------- Setters (with validation) ----------
    public void setDailyPrice(double dailyPrice) {
        if (dailyPrice <= 0) {
            throw new IllegalArgumentException("Daily price must be greater than 0.");
        }
        this.dailyPrice = dailyPrice;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    // Staff flags the vehicle as needing maintenance
    public void flagForMaintenance() {
        this.maintenanceRequired = true;
    }

    // Used by the subclasses after they finish the maintenance
    protected void clearMaintenanceFlag() {
        this.maintenanceRequired = false;
    }

    // Base cost = daily price x days (shared by all types)
    protected double calculateBaseCost(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Rental days must be positive.");
        }
        return dailyPrice * days;
    }

    // Prints the information that is the same for all vehicles
    protected void displayCommonInfo() {
        String status;
        if (available) {
            status = "Available";
        } else {
            status = "Rented";
        }

        String maintenance;
        if (maintenanceRequired) {
            maintenance = "Needs maintenance";
        } else {
            maintenance = "OK";
        }
        System.out.print("ID: " + vehicleId + " | " + brand + " " + model + " (" + year + ")"
                + " | $" + dailyPrice + "/day | " + status + " | Maintenance: " + maintenance);
    }

    // ---------- Abstract methods: every subclass MUST write its own version ----------

    // optionalService = true means the customer wants the extra service
    // (insurance for a car, helmet for a motorcycle, ignored for a truck)
    public abstract double calculateRentalCost(int days, boolean optionalService);

    public abstract void displayInfo();

    // Name of the optional service, or null if the vehicle has none
    public abstract String getOptionalServiceName();
}
