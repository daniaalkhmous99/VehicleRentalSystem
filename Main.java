import java.util.Scanner;

// Main only shows the menu, reads input and calls RentalSystem.
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static RentalSystem system = new RentalSystem();

    public static void main(String[] args) {
        int choice = -1;
        while (choice != 0) {
            printMenu();
            choice = readInt("Enter your choice: ");
            try {
                handleChoice(choice);
            } catch (RentalException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid data: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== Vehicle Rental Management System =====");
        System.out.println("1.  Add New Vehicle");
        System.out.println("2.  Display All Vehicles");
        System.out.println("3.  Search for Vehicle");
        System.out.println("4.  Display Available Vehicles");
        System.out.println("5.  Register New Customer");
        System.out.println("6.  Display All Customers");
        System.out.println("7.  Rent a Vehicle");
        System.out.println("8.  Return a Vehicle");
        System.out.println("9.  Display All Rentals");
        System.out.println("10. Display Customer Rental History");
        System.out.println("11. Display Total Revenue");
        System.out.println("12. Flag Vehicle for Maintenance");
        System.out.println("13. Display Vehicles Requiring Maintenance");
        System.out.println("14. Perform Vehicle Maintenance");
        System.out.println("0.  Exit");
    }

    private static void handleChoice(int choice) throws RentalException {
        switch (choice) {
            case 1:
                addVehicle();
                break;
            case 2:
                system.displayAllVehicles();
                break;
            case 3:
                searchVehicle();
                break;
            case 4:
                showVehiclesByStatus();
                break;
            case 5:
                registerCustomer();
                break;
            case 6:
                showCustomers();
                break;
            case 7:
                rentVehicle();
                break;
            case 8:
                int rentalId = readInt("Enter rental ID: ");
                system.returnVehicle(rentalId);
                System.out.println("Vehicle returned successfully. Rental completed.");
                break;
            case 9:
                showRentals();
                break;
            case 10:
                showCustomerRentals();
                break;
            case 11:
                System.out.println("Total revenue (completed rentals only): $" + system.getTotalRevenue());
                break;
            case 12:
                system.flagVehicleForMaintenance(readText("Enter vehicle ID: "));
                System.out.println("Vehicle flagged for maintenance. It cannot be rented now.");
                break;
            case 13:
                system.displayVehiclesNeedingMaintenance();
                break;
            case 14:
                system.performMaintenance(readText("Enter vehicle ID: "));
                System.out.println("Maintenance completed. The vehicle can be rented again.");
                break;
            case 0:
                System.out.println("Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please choose a number from the menu.");
        }
    }

    // ---------- Menu actions that need several inputs ----------

    private static void addVehicle() throws RentalException {
        System.out.println("Vehicle type: 1) Car  2) Motorcycle  3) Truck");
        int type = readInt("Choose type: ");
        if (type < 1 || type > 3) {
            throw new RentalException("Invalid vehicle type.");
        }

        String id = readText("Vehicle ID: ");
        if (system.findVehicleById(id) != null) {
            throw new RentalException("A vehicle with ID " + id + " already exists.");
        }
        String brand = readText("Brand: ");
        String model = readText("Model: ");
        int year = readInt("Manufacturing year: ");
        double price = readDouble("Daily rental price: ");

        Vehicle vehicle;
        if (type == 1) {
            int seats = readInt("Number of seats: ");
            String transmission = readText("Transmission (Manual/Automatic): ");
            vehicle = new Car(id, brand, model, year, price, seats, transmission);
        } else if (type == 2) {
            int cc = readInt("Engine capacity (CC): ");
            boolean helmet = readYesNo("Helmet available? (y/n): ");
            vehicle = new Motorcycle(id, brand, model, year, price, cc, helmet);
        } else {
            double load = readDouble("Maximum load capacity (tons): ");
            vehicle = new Truck(id, brand, model, year, price, load);
        }

        system.addVehicle(vehicle);
        System.out.println("Vehicle added successfully.");
    }

    private static void searchVehicle() throws RentalException {
        System.out.println("Search by: 1) Vehicle ID  2) Brand and model");
        int option = readInt("Choose: ");
        if (option == 1) {
            String id = readText("Vehicle ID: ");
            system.searchVehicle(id);
            // If the vehicle exists, the staff can update its daily price
            if (system.findVehicleById(id) != null) {
                if (readYesNo("Do you want to update the daily price of this vehicle? (y/n): ")) {
                    double newPrice = readDouble("Enter new daily price: ");
                    system.updateVehiclePrice(id, newPrice);
                    System.out.println("Price updated successfully.");
                }
            }
        } else if (option == 2) {
            String brand = readText("Brand: ");
            String model = readText("Model: ");
            system.searchVehicle(brand, model);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void showVehiclesByStatus() {
        System.out.println("Show: 1) Available vehicles  2) Rented vehicles");
        int option = readInt("Choose: ");
        if (option == 1) {
            system.displayAvailableVehicles();
        } else if (option == 2) {
            system.displayRentedVehicles();
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void showRentals() {
        System.out.println("Show: 1) All rentals  2) Active rentals only");
        int option = readInt("Choose: ");
        if (option == 1) {
            system.displayAllRentals();
        } else if (option == 2) {
            system.displayActiveRentals();
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void showCustomers() throws RentalException {
        system.displayAllCustomers();
        if (readYesNo("Do you want to update a customer's phone or email? (y/n): ")) {
            updateCustomer();
        }
    }

    private static void updateCustomer() throws RentalException {
        String id = readText("Customer ID: ");
        System.out.println("Update: 1) Phone  2) Email");
        int option = readInt("Choose: ");
        if (option == 1) {
            system.updateCustomerPhone(id, readText("New phone: "));
            System.out.println("Phone updated.");
        } else if (option == 2) {
            system.updateCustomerEmail(id, readText("New email: "));
            System.out.println("Email updated.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void showCustomerRentals() throws RentalException {
        String id = readText("Enter customer ID: ");
        System.out.println("Show: 1) Vehicles currently rented  2) Full rental history");
        int option = readInt("Choose: ");
        if (option == 1) {
            system.displayCustomerRentedVehicles(id);
        } else if (option == 2) {
            system.displayCustomerHistory(id);
        } else {
            System.out.println("Invalid option.");
        }
    }

    private static void registerCustomer() throws RentalException {
        String id = readText("Customer ID: ");
        String name = readText("Full name: ");
        String phone = readText("Phone: ");
        String email = readText("Email: ");
        String license = readText("Driving license number: ");
        system.addCustomer(new Customer(id, name, phone, email, license));
        System.out.println("Customer registered successfully.");
    }

    private static void rentVehicle() throws RentalException {
        String customerId = readText("Customer ID: ");
        String vehicleId = readText("Vehicle ID: ");

        Vehicle vehicle = system.findVehicleById(vehicleId);
        if (vehicle == null) {
            throw new RentalException("Vehicle not found.");
        }

        int days = readInt("Rental days: ");

        // Each vehicle type tells us its own optional service (or null if it has none)
        boolean optional = false;
        String serviceName = vehicle.getOptionalServiceName();
        if (serviceName != null) {
            optional = readYesNo("Add " + serviceName + "? (y/n): ");
        }

        Rental rental = system.rentVehicle(customerId, vehicleId, days, optional);
        System.out.println("Rental created successfully!");
        rental.displayInfo();
    }

    // ---------- Input helpers (they keep asking until the input is valid) ----------

    private static String readText(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static boolean readYesNo(String message) {
        while (true) {
            String answer = readText(message).toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                return true;
            }
            if (answer.equals("n") || answer.equals("no")) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }
}
