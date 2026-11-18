public class DataGenerator {
    // Jedyna instancja tej klasy w całym programie
    private static DataGenerator instance;

    // Prywatny konstruktor blokuje tworzenie obiektów przez "new DataGenerator()"
    private DataGenerator() {
    }

    // Globalny punkt dostępu do tej jednej instancji
    public static DataGenerator getInstance() {
        if (instance == null) {
            instance = new DataGenerator();
        }
        return instance;
    }

    public void populate(StoreManager manager) {
        manager.addBranch("Kraków Główny", 6);
        manager.addBranch("Warszawa Centrum", 10);
        manager.addBranch("Wrocław Rynek", 5);
        manager.addBranch("Gdańsk Wrzeszcz", 4); // Celowo zostawimy tu mało lub 0 laptopów do testu sortowania

        try {
            Branch krakow = manager.getBranch("Kraków Główny");
            krakow.addLaptop(new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.SPRAWNY, 2022, 3500.0, 92.5, "Windows 11", 512));
            krakow.addLaptop(new Laptop("MacBook Pro 14", "Apple", LaptopCondition.SPRAWNY, 2023, 8500.0, 99.0, "macOS", 512));
            krakow.addLaptop(new Laptop("XPS 15", "Dell", LaptopCondition.W_NAPRAWIE, 2021, 5200.0, 84.0, "Windows 11", 1024));
            krakow.addLaptop(new Laptop("EliteBook 840", "HP", LaptopCondition.W_TRAKCIE_TESTOW, 2020, 2100.0, 78.0, "Windows 10", 256));
            krakow.addLaptop(new Laptop("ZenBook 14", "Asus", LaptopCondition.USZKODZONY, 2019, 1100.0, 65.0, "Windows 10", 512));

            Branch warszawa = manager.getBranch("Warszawa Centrum");
            warszawa.addLaptop(new Laptop("MacBook Air M2", "Apple", LaptopCondition.SPRAWNY, 2023, 4900.0, 97.0, "macOS", 256));
            warszawa.addLaptop(new Laptop("Latitude 7490", "Dell", LaptopCondition.SPRAWNY, 2019, 1450.0, 81.0, "Windows 10", 256));
            warszawa.addLaptop(new Laptop("IdeaPad 3", "Lenovo", LaptopCondition.USZKODZONY, 2020, 950.0, 70.0, "Windows 10", 256));
            warszawa.addLaptop(new Laptop("ProBook 450", "HP", LaptopCondition.W_TRAKCIE_TESTOW, 2021, 2300.0, 88.0, "Windows 11", 512));

            Branch wroclaw = manager.getBranch("Wrocław Rynek");
            wroclaw.addLaptop(new Laptop("ROG Zephyrus", "Asus", LaptopCondition.SPRAWNY, 2022, 6200.0, 91.0, "Windows 11", 1024));
            wroclaw.addLaptop(new Laptop("ThinkPad X1 Carbon", "Lenovo", LaptopCondition.ZAREZERWOWANY, 2022, 5800.0, 94.0, "Linux", 512));
            wroclaw.addLaptop(new Laptop("Swift 3", "Acer", LaptopCondition.W_NAPRAWIE, 2020, 1300.0, 76.0, "Windows 11", 512));

        } catch (InvalidPriceException | BranchFullException | DuplicateLaptopException e) {
            System.err.println("Błąd podczas generowania danych testowych: " + e.getMessage());
        }
    }
}