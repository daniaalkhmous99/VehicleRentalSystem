# Vehicle Rental Management System

A console-based Java application for a vehicle rental company that rents **cars, motorcycles and trucks**.
It manages vehicles, customers, rentals, returns, maintenance and revenue.

This project was made for the **Object-Oriented Programming** course assignment.

- **Author:** Dania Alkhmous
- **Course:** Object-Oriented Programming

---

## Requirements

- Java JDK 8 or newer (the project was tested with Java 21)
- A terminal (Command Prompt, PowerShell, or Terminal)

To check that Java is installed, run:

```
javac -version
java -version
```

---

## How to compile and run

1. Put all the `.java` files in the **same folder**.
2. Open the terminal inside that folder.
3. Compile all files:

```
javac *.java
```

4. Run the program:

```
java Main
```

The menu will appear and the program keeps running until you choose **0 (Exit)**.

---

## Project structure

| File | Responsibility |
|---|---|
| `Main.java` | Shows the menu, reads user input, calls `RentalSystem` (no business logic) |
| `RentalSystem.java` | All business logic: vehicles, customers, rentals, returns, maintenance, revenue |
| `Vehicle.java` | Abstract parent class for all vehicles |
| `Car.java` | Car (seats, transmission, optional insurance $15/day) |
| `Motorcycle.java` | Motorcycle (engine CC, optional helmet $5/day) |
| `Truck.java` | Truck (max load, fixed $50 service fee per rental) |
| `Maintainable.java` | Interface with `performMaintenance()` and `needsMaintenance()` |
| `Customer.java` | Customer data with validation |
| `Rental.java` | Rental transaction (links one customer and one vehicle) |
| `RentalException.java` | Our own exception for broken business rules |
| `UML_Class_Diagram.png` | UML class diagram of the project |
| `OOP_Report.pdf` | Short report (abstraction, abstract class vs interface, polymorphism, overloading, overriding) |
| `screenshots/` | Screenshots of vehicle creation, customer registration, renting, cost calculation and returning |

---

## Menu

```
1.  Add New Vehicle
2.  Display All Vehicles
3.  Search for Vehicle
4.  Display Available Vehicles
5.  Register New Customer
6.  Display All Customers
7.  Rent a Vehicle
8.  Return a Vehicle
9.  Display All Rentals
10. Display Customer Rental History
11. Display Total Revenue
12. Flag Vehicle for Maintenance
13. Display Vehicles Requiring Maintenance
14. Perform Vehicle Maintenance
0.  Exit
```

Some options ask a short follow-up question:

- **3 (Search for Vehicle):** search by ID or by brand and model. After finding a vehicle by ID, you can update its daily price.
- **4:** show available vehicles or rented vehicles.
- **9 (Display All Rentals):** show all rentals or active rentals only.
- **6 (Display All Customers):** after the list, you can update a customer's phone or email.
- **10 (Customer Rental History):** show the vehicles the customer is renting now, or the full rental history.

---

## Rental cost rules

Base cost = daily price x rental days.

| Vehicle | Extra cost | Example | Total |
|---|---|---|---|
| Car | Optional insurance: $15 per day | $60/day, 4 days, with insurance | $300 |
| Motorcycle | Optional helmet: $5 per day | $30/day, 2 days, with helmet | $70 |
| Truck | Fixed $50 service fee per rental | $100/day, 3 days | $350 |

The cost is calculated with **polymorphism**: each subclass overrides `calculateRentalCost`, so there is no big `if/else` on the vehicle type.

---

## Business rules (summary)

- Vehicle IDs and customer IDs must be unique. Rental IDs are generated automatically.
- Rental days must be positive.
- A vehicle cannot have more than one active rental. A customer can have many active rentals.
- Unknown customers or vehicles are rejected.
- Only an active rental can be returned, and the same rental cannot be returned twice.
- A vehicle flagged for maintenance cannot be rented, even if it is not rented now.
- Maintenance cannot be performed on a vehicle with an active rental.
- Performing maintenance clears the flag, and the vehicle can be rented again.
- Total revenue counts **completed** rentals only.

---

## OOP concepts used

| Concept | Where |
|---|---|
| Encapsulation | Private fields with getters and validated setters in all classes |
| Constructors and overloading | `Vehicle`, `Car`, `Motorcycle` have more than one constructor |
| Inheritance | `Car`, `Motorcycle`, `Truck` extend `Vehicle` |
| Abstract class | `Vehicle` (abstract methods: `calculateRentalCost`, `displayInfo`, `getOptionalServiceName`) |
| Interface | `Maintainable`, implemented by `Car`, `Motorcycle`, `Truck` |
| Polymorphism (inheritance) | `ArrayList<Vehicle>` and calls like `vehicle.calculateRentalCost(...)` |
| Polymorphism (interface) | `Maintainable` references in `RentalSystem` |
| Method overloading | `searchVehicle(id)` and `searchVehicle(brand, model)` |
| Method overriding | `calculateRentalCost`, `displayInfo` and the `Maintainable` methods in each subclass |
| `this` / `super` | `this(...)` and `this.field` in constructors, `super(...)` in subclasses |
| `static` / `final` | `totalVehiclesCreated`, `nextRentalId`, and constants like `INSURANCE_PER_DAY` |
| Association | `Rental` has a `Customer` and a `Vehicle` |
| Collections and exceptions | `ArrayList`, `IllegalArgumentException`, `RentalException`, `try/catch` |

---

## Screenshots

The `screenshots` folder shows: vehicle creation, customer registration, renting (with cost calculation), and returning a vehicle.
