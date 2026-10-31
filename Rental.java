public class Rental {

    public static final String ACTIVE = "Active";
    public static final String COMPLETED = "Completed";

    // static counter: gives every rental a new unique ID automatically
    private static int nextRentalId = 1;

    private int rentalId;
    private Customer customer;   // association: a rental knows its customer
    private Vehicle vehicle;     // association: a rental knows its vehicle
    private int days;
    private double totalCost;
    private String status;

    public Rental(Customer customer, Vehicle vehicle, int days, double totalCost) {
        this.rentalId = nextRentalId;
        nextRentalId++;
        this.customer = customer;
        this.vehicle = vehicle;
        this.days = days;
        this.totalCost = totalCost;
        this.status = ACTIVE;
    }

    public int getRentalId() { return rentalId; }
    public Customer getCustomer() { return customer; }
    public Vehicle getVehicle() { return vehicle; }
    public int getDays() { return days; }
    public double getTotalCost() { return totalCost; }
    public String getStatus() { return status; }

    public boolean isActive() {
        return status.equals(ACTIVE);
    }

    // Completes the rental. Only an active rental can be completed.
    public void complete() throws RentalException {
        if (!isActive()) {
            throw new RentalException("Rental #" + rentalId + " is already completed.");
        }
        status = COMPLETED;
    }

    public void displayInfo() {
        System.out.println("Rental #" + rentalId + " | Customer: " + customer.getFullName()
                + " | Vehicle: " + vehicle.getBrand() + " " + vehicle.getModel()
                + " (" + vehicle.getVehicleId() + ")"
                + " | Days: " + days + " | Total: $" + totalCost + " | Status: " + status);
    }
}
