import java.util.ArrayList;

// This class contains ALL the business logic (Main only talks to the user).
public class RentalSystem {

    private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    private ArrayList<Customer> customers = new ArrayList<Customer>();
    private ArrayList<Rental> rentals = new ArrayList<Rental>();

    // ================= Vehicles =================

    public void addVehicle(Vehicle vehicle) throws RentalException {
        if (findVehicleById(vehicle.getVehicleId()) != null) {
            throw new RentalException("A vehicle with ID " + vehicle.getVehicleId() + " already exists.");
        }
        vehicles.add(vehicle);
    }

    // Returns the vehicle or null if not found
    public Vehicle findVehicleById(String id) {
        for (Vehicle v : vehicles) {
            if (v.getVehicleId().equalsIgnoreCase(id.trim())) {
                return v;
            }
        }
        return null;
    }

    // Overloading 1: search by ID
    public void searchVehicle(String vehicleId) {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            System.out.println("No vehicle found with ID " + vehicleId);
        } else {
            v.displayInfo();
        }
    }

    // Overloading 2: search by brand and model
    public void searchVehicle(String brand, String model) {
        int found = 0;
        for (Vehicle v : vehicles) {
            if (v.getBrand().equalsIgnoreCase(brand.trim())
                    && v.getModel().equalsIgnoreCase(model.trim())) {
                v.displayInfo();
                found++;
            }
        }
        if (found == 0) {
            System.out.println("No vehicle found for " + brand + " " + model);
        }
    }

    public void displayAllVehicles() {
        if (vehicles.isEmpty()) {
            System.out.println("There are no vehicles yet.");
            return;
        }
        for (Vehicle v : vehicles) {
            v.displayInfo();   // polymorphism: each type prints its own details
        }
    }

    // Available = not rented AND not waiting for maintenance
    public void displayAvailableVehicles() {
        int count = 0;
        for (Vehicle v : vehicles) {
            if (v.isAvailable() && !v.isMaintenanceRequired()) {
                v.displayInfo();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No vehicles are available right now.");
        }
    }

    public void displayRentedVehicles() {
        int count = 0;
        for (Vehicle v : vehicles) {
            if (!v.isAvailable()) {
                v.displayInfo();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No vehicles are currently rented.");
        }
    }

    public void updateVehiclePrice(String vehicleId, double newPrice) throws RentalException {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            throw new RentalException("Vehicle not found.");
        }
        v.setDailyPrice(newPrice);   // the setter rejects bad prices
    }

    // ================= Customers =================

    public void addCustomer(Customer customer) throws RentalException {
        if (findCustomerById(customer.getCustomerId()) != null) {
            throw new RentalException("A customer with ID " + customer.getCustomerId() + " already exists.");
        }
        customers.add(customer);
    }

    public Customer findCustomerById(String id) {
        for (Customer c : customers) {
            if (c.getCustomerId().equalsIgnoreCase(id.trim())) {
                return c;
            }
        }
        return null;
    }

    public void displayAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("There are no customers yet.");
            return;
        }
        for (Customer c : customers) {
            c.displayInfo();
        }
    }

    public void updateCustomerPhone(String customerId, String newPhone) throws RentalException {
        Customer c = findCustomerById(customerId);
        if (c == null) {
            throw new RentalException("Customer not found.");
        }
        c.setPhone(newPhone);
    }

    public void updateCustomerEmail(String customerId, String newEmail) throws RentalException {
        Customer c = findCustomerById(customerId);
        if (c == null) {
            throw new RentalException("Customer not found.");
        }
        c.setEmail(newEmail);
    }

    // ================= Rentals =================

    private boolean hasActiveRental(Vehicle vehicle) {
        for (Rental r : rentals) {
            if (r.getVehicle() == vehicle && r.isActive()) {
                return true;
            }
        }
        return false;
    }

    public Rental rentVehicle(String customerId, String vehicleId, int days,
                              boolean optionalService) throws RentalException {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new RentalException("Customer not found. Please register the customer first.");
        }
        Vehicle vehicle = findVehicleById(vehicleId);
        if (vehicle == null) {
            throw new RentalException("Vehicle not found.");
        }
        if (days <= 0) {
            throw new RentalException("Rental days must be positive.");
        }
        if (vehicle.isMaintenanceRequired()) {
            throw new RentalException("This vehicle needs maintenance and cannot be rented.");
        }
        if (!vehicle.isAvailable() || hasActiveRental(vehicle)) {
            throw new RentalException("This vehicle is not available (it is already rented).");
        }

        // Polymorphism: the right calculateRentalCost (Car / Motorcycle / Truck) runs
        double total = vehicle.calculateRentalCost(days, optionalService);

        Rental rental = new Rental(customer, vehicle, days, total);
        rentals.add(rental);
        vehicle.setAvailable(false);   // the vehicle becomes unavailable immediately
        return rental;
    }

    public void returnVehicle(int rentalId) throws RentalException {
        Rental rental = null;
        for (Rental r : rentals) {
            if (r.getRentalId() == rentalId) {
                rental = r;
            }
        }
        if (rental == null) {
            throw new RentalException("Rental #" + rentalId + " does not exist.");
        }
        rental.complete();                       // throws an exception if it is not active
        rental.getVehicle().setAvailable(true);  // the vehicle is available again
    }

    public void displayAllRentals() {
        if (rentals.isEmpty()) {
            System.out.println("There are no rentals yet.");
            return;
        }
        for (Rental r : rentals) {
            r.displayInfo();
        }
    }

    public void displayActiveRentals() {
        int count = 0;
        for (Rental r : rentals) {
            if (r.isActive()) {
                r.displayInfo();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("There are no active rentals.");
        }
    }

    // Shows ONLY the vehicles this customer is renting now (active rentals)
    public void displayCustomerRentedVehicles(String customerId) throws RentalException {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new RentalException("Customer not found.");
        }

        System.out.println("--- Vehicles currently rented by " + customer.getFullName() + " ---");
        int active = 0;
        for (Rental r : rentals) {
            if (r.getCustomer() == customer && r.isActive()) {
                r.getVehicle().displayInfo();
                active++;
            }
        }
        if (active == 0) {
            System.out.println("None.");
        }
    }

    // Shows the FULL rental history of the customer (active and completed)
    public void displayCustomerHistory(String customerId) throws RentalException {
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            throw new RentalException("Customer not found.");
        }

        System.out.println("--- Rental history of " + customer.getFullName() + " ---");
        int count = 0;
        for (Rental r : rentals) {
            if (r.getCustomer() == customer) {
                r.displayInfo();
                count++;
            }
        }
        if (count == 0) {
            System.out.println("This customer has no rentals yet.");
        }
    }

    // Only COMPLETED rentals are counted
    public double getTotalRevenue() {
        double total = 0;
        for (Rental r : rentals) {
            if (!r.isActive()) {
                total = total + r.getTotalCost();
            }
        }
        return total;
    }

    // ================= Maintenance =================

    public void flagVehicleForMaintenance(String vehicleId) throws RentalException {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            throw new RentalException("Vehicle not found.");
        }
        if (v.isMaintenanceRequired()) {
            throw new RentalException("This vehicle is already flagged for maintenance.");
        }
        v.flagForMaintenance();
    }

    // Interface polymorphism: we look at the vehicles through Maintainable references
    public void displayVehiclesNeedingMaintenance() {
        int count = 0;
        for (Vehicle v : vehicles) {
            if (v instanceof Maintainable) {
                Maintainable m = (Maintainable) v;
                if (m.needsMaintenance()) {
                    v.displayInfo();
                    count++;
                }
            }
        }
        if (count == 0) {
            System.out.println("No vehicles need maintenance.");
        }
    }

    public void performMaintenance(String vehicleId) throws RentalException {
        Vehicle v = findVehicleById(vehicleId);
        if (v == null) {
            throw new RentalException("Vehicle not found.");
        }
        if (hasActiveRental(v)) {
            throw new RentalException("Cannot perform maintenance while the vehicle has an active rental. "
                    + "Return the vehicle first.");
        }
        if (!(v instanceof Maintainable)) {
            throw new RentalException("This vehicle cannot be maintained.");
        }
        Maintainable m = (Maintainable) v;
        if (!m.needsMaintenance()) {
            throw new RentalException("This vehicle is not flagged for maintenance.");
        }
        m.performMaintenance();   // Car / Motorcycle / Truck version runs
    }
}
