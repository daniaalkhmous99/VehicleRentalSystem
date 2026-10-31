public class Car extends Vehicle implements Maintainable {

    // final + static = a constant that never changes
    public static final double INSURANCE_PER_DAY = 15;

    private int seats;
    private String transmission;

    public Car(String vehicleId, String brand, String model, int year,
               double dailyPrice, int seats, String transmission) {
        super(vehicleId, brand, model, year, dailyPrice);   // super calls the Vehicle constructor
        if (seats <= 0) {
            throw new IllegalArgumentException("Seats must be greater than 0.");
        }
        if (transmission == null || transmission.trim().isEmpty()) {
            throw new IllegalArgumentException("Transmission cannot be empty.");
        }
        this.seats = seats;
        this.transmission = transmission.trim();
    }

    // Overloaded constructor: default 5 seats and Automatic
    public Car(String vehicleId, String brand, String model, int year, double dailyPrice) {
        this(vehicleId, brand, model, year, dailyPrice, 5, "Automatic");
    }

    public int getSeats() { return seats; }
    public String getTransmission() { return transmission; }

    @Override
    public double calculateRentalCost(int days, boolean withInsurance) {
        double total = calculateBaseCost(days);
        if (withInsurance) {
            total = total + INSURANCE_PER_DAY * days;
        }
        return total;
    }

    @Override
    public void displayInfo() {
        System.out.print("[Car] ");
        displayCommonInfo();
        System.out.println(" | Seats: " + seats + " | Transmission: " + transmission);
    }

    @Override
    public String getOptionalServiceName() {
        return "insurance ($" + INSURANCE_PER_DAY + "/day)";
    }

    // ----- Maintainable -----
    @Override
    public void performMaintenance() {
        System.out.println("Car " + getVehicleId() + ": oil changed, brakes and tires checked.");
        clearMaintenanceFlag();
    }

    @Override
    public boolean needsMaintenance() {
        return isMaintenanceRequired();
    }
}
