# Laptop Store Manager 💻

> **Note:** This is an ongoing university project, currently in active development.

A professional desktop application written in Java for comprehensive management of a computer store network. The project is designed with a strong focus on solid object-oriented architecture, best programming practices, and **reliability proven by comprehensive unit tests**. 

The application uses a graphical user interface based on the **Swing** library and features role-based access control (Administrator, Client), allowing for inventory management across multiple branches and sales simulation.

## 🚀 Key Features

- **Branch Management:**
  - Create and remove store branches.
  - Monitor branch capacity in real-time.
  - Generate occupancy reports and statistics.
- **Inventory Management (Laptops):**
  - Add, remove, and sell laptops.
  - Track device condition (Working, Damaged, In repair).
  - Exact and partial search by model and brand.
  - Sort inventory by name and price.
  - Calculate total inventory value.
- **Complex Business Operations:**
  - Safe hardware transfer between branches with transaction rollback in case of errors.
  - Advanced algorithms, e.g., transferring the oldest of the two most expensive laptops between selected branches.
- **Graphical User Interface (GUI):**
  - Dedicated panels for different user roles (Login, AdminFrame, ClientFrame).
  - Utilization of custom table models (`GenericTableModel`).

## 🧪 Unit Testing

A critical focus of this project is ensuring fault tolerance and reliability. The business logic is covered by an **extensive suite of unit tests** using the **JUnit 5** framework.

Tests guarantee correct application behavior, including:
- **Business Exception Handling:** 
  - `BranchFullException` – Attempting to add equipment to an already full branch.
  - `DuplicateLaptopException` – Preventing duplicate laptops in the database.
  - `LaptopNotFoundException` – Safe handling of missing devices.
  - `InvalidPriceException` – Equipment price validation.
- **Testing Algorithms and Logic:**
  - Verification of correct branch load/occupancy calculations.
  - Testing inventory filters (technical condition, partial string matching).
  - Verification of sorted lists.
  - Transaction safety (testing state rollback after an exception is thrown during goods transfer).

## 🛠️ Technologies

- **Language:** Java 17+
- **GUI:** Java Swing (AWT)
- **Testing:** JUnit 5 (Jupiter)
- **Paradigms:** Object-Oriented Programming (OOP), Facade Design Pattern

## 💻 How to Run

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
3. Ensure the appropriate Java SDK version is configured in your project settings.
4. Run the main class `Main.java` located in the `src` directory.
5. *To run tests:* Execute the `test/` package using your IDE's built-in test runner.

---
*This project is being developed as a university assignment to expand competencies in Java programming, software engineering, and rigorous code testing practices.*