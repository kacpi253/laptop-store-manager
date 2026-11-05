import java.util.List;

public class Main {

    public static void main(String[] args){
        Laptop laptop1 = new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.SPRAWNY, 2022, 3500.0, 92.5, "Windows 11", 512);
        Laptop laptop2 = new Laptop("MacBook Pro 14", "Apple", LaptopCondition.SPRAWNY, 2023, 8500.0, 100.0, "macOS", 512);
        Laptop laptop3 = new Laptop("IdeaPad 3", "Lenovo", LaptopCondition.USZKODZONY, 2020, 1200.0, 75.0, "Windows 10", 256);

        StoreManager manager = new StoreManager();

        manager.addBranch("Kraków Główny", 5);
        manager.addBranch("Warszawa Centrum", 10);
        manager.addBranch("Gdańsk Wrzeszcz", 4);

        Branch krakow = manager.getBranch("Kraków Główny");
        krakow.addLaptop(laptop1);
        krakow.addLaptop(laptop2);

        Branch warszawa = manager.getBranch("Warszawa Centrum");
        warszawa.addLaptop(laptop3);
        System.out.println("--- TEST PODSUMOWANIA (SUMMARY) ---");
        manager.summary();

        System.out.println("\n--- TEST PUSTYCH ODDZIAŁÓW (FIND EMPTY) ---");
        List<Branch> emptyBranches = manager.findEmpty();
        for (Branch b : emptyBranches) {
            System.out.println("Pusty oddział: " + b.getBranchName());
        }

        System.out.println("\n--- TEST USUNIĘCIA ODDZIAŁU ---");
        manager.removeBranch("Gdańsk Wrzeszcz");
        manager.summary();

        System.out.println("\n--- TEST ŁĄCZNEJ WARTOŚCI (TOTAL VALUE) ---");
        System.out.println("Wartość w Kraków Główny: " + manager.totalValue("Kraków Główny") + " PLN");
        System.out.println("Wartość w Warszawa Centrum: " + manager.totalValue("Warszawa Centrum") + " PLN");
        System.out.println("Wartość w nieistniejącym oddziale: " + manager.totalValue("Poznań") + " PLN");

        System.out.println("\n--- TEST ZLICZANIA PO MARCE (COUNT BY BRAND) ---");
        System.out.println("Liczba laptopów Lenovo w całej sieci: " + manager.countByBrand("Lenovo"));
        System.out.println("Liczba laptopów Apple w całej sieci: " + manager.countByBrand("Apple"));
        System.out.println("Liczba laptopów Dell w całej sieci: " + manager.countByBrand("Dell"));

        System.out.println("\n--- TEST PRZENOSZENIA LAPTOPA (TRANSFER) ---");
        System.out.println("Przed transferem -> Kraków: " + krakow.getLaptopCount() + " szt., Warszawa: " + warszawa.getLaptopCount() + " szt.");

        manager.transferLaptop(laptop1, "Kraków Główny", "Warszawa Centrum");

        System.out.println("Po transferze -> Kraków: " + krakow.getLaptopCount() + " szt., Warszawa: " + warszawa.getLaptopCount() + " szt.");
        System.out.println("Nowa wartość w Warszawie: " + manager.totalValue("Warszawa Centrum") + " PLN");

        System.out.println("\nPróba transferu do nieistniejącego oddziału:");
        manager.transferLaptop(laptop2, "Kraków Główny", "Wrocław");
    }
}