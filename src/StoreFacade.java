import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StoreFacade {
    private final StoreManager storeManager;
    private final List<String> contactRequests;

    // Konstruktor używany przez aplikację (ładuje dane startowe z Singletona)
    public StoreFacade() {
        this.storeManager = new StoreManager();
        this.contactRequests = new ArrayList<>();
        DataGenerator.getInstance().populate(this.storeManager);
    }

    // Konstruktor używany np. w testach JUnit (pozwala wstrzyknąć pusty StoreManager)
    public StoreFacade(StoreManager storeManager) {
        this.storeManager = storeManager;
        this.contactRequests = new ArrayList<>();
    }

    // --- OPERACJE NA ODDZIAŁACH ---

    public List<Branch> getAllBranches() {
        return storeManager.getAllBranches();
    }

    public void addBranch(String name, int capacity) {
        storeManager.addBranch(name, capacity);
    }

    public void removeBranch(String name) {
        storeManager.removeBranch(name);
    }

    // Sortuje przekazaną listę oddziałów w miejscu malejąco po aktualnym zapełnieniu (%)
    public void sortBranchesByLoad(List<Branch> branches) {
        branches.sort(Comparator.comparingDouble(Branch::getCurrentLoad).reversed());
    }

    // --- OPERACJE NA LAPTOPACH (ADMIN) ---

    public List<Laptop> getLaptopsInBranch(Branch branch) {
        if (branch == null) {
            return new ArrayList<>();
        }
        return branch.getLaptopList();
    }

    public void addLaptopToBranch(Branch branch, Laptop laptop)
            throws BranchFullException, DuplicateLaptopException, LaptopNotFoundException {
        if (branch == null) {
            throw new LaptopNotFoundException("Nie wybrano oddziału.");
        }
        branch.addLaptop(laptop);
    }

    public void removeLaptopFromBranch(Branch branch, Laptop laptop)
            throws LaptopNotFoundException {
        if (branch == null || laptop == null) {
            throw new LaptopNotFoundException("Nie wybrano oddziału lub laptopa.");
        }
        branch.removeLaptop(laptop);
    }

    public void updateLaptopCondition(Laptop laptop, LaptopCondition newCondition)
            throws LaptopNotFoundException {
        if (laptop == null) {
            throw new LaptopNotFoundException("Nie wybrano laptopa.");
        }
        laptop.setCondition(newCondition);
    }

    public void updateLaptopPrice(Laptop laptop, double newPrice)
            throws LaptopNotFoundException, InvalidPriceException {
        if (laptop == null) {
            throw new LaptopNotFoundException("Nie wybrano laptopa.");
        }
        laptop.setPrice(newPrice);
    }

    // --- FILTROWANIE I OPERACJE KLIENTA ---

    // Łączy działanie pola filter textbox (tekst) oraz condition combobox (stan)
    public List<Laptop> filterLaptops(Branch branch, String query, LaptopCondition condition) {
        if (branch == null) {
            return new ArrayList<>();
        }

        // Krok 1: filtr po tekście (model lub marka) z Twojej metody searchPartial
        List<Laptop> filtered;
        if (query == null || query.trim().isEmpty()) {
            filtered = new ArrayList<>(branch.getLaptopList());
        } else {
            filtered = branch.searchPartial(query.trim());
        }

        // Krok 2: jeśli w ComboBox wybrano konkretny stan (nie "Wszystkie"), zawężamy wynik
        if (condition != null) {
            filtered.removeIf(laptop -> laptop.getCondition() != condition);
        }

        return filtered;
    }

    public void reserveLaptop(Laptop laptop) throws LaptopNotFoundException {
        if (laptop == null) {
            throw new LaptopNotFoundException("Nie wybrano laptopa do rezerwacji.");
        }
        laptop.setCondition(LaptopCondition.ZAREZERWOWANY);
    }

    public void addContactRequest(Branch branch, Laptop laptop, String clientInfo) {
        String entry = String.format("[%s] %s %s (%.2f zł) - Kontakt: %s",
                branch.getBranchName(), laptop.getBrand(), laptop.getModel(), laptop.getPrice(), clientInfo);
        contactRequests.add(entry);
    }

    //sortowanie po cenie
    public void sortLaptopsByPrice(List<Laptop> laptops) {
        if (laptops != null) {
            laptops.sort(Comparator.comparingDouble(Laptop::getPrice));
        }
    }


    public List<String> getContactRequests() {
        return new ArrayList<>(contactRequests);
    }
}