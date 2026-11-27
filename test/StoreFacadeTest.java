import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoreFacadeTest {

    private StoreFacade facade;

    @BeforeEach
    void setUp() {
        // Konstruktor domyślny automatycznie ładuje dane z DataGenerator
        facade = new StoreFacade();
    }

    @Test
    void shouldInitializeWithDataAndSupportCustomManagerConstructor() {
        assertEquals(4, facade.getAllBranches().size());

        StoreFacade emptyFacade = new StoreFacade(new StoreManager());
        assertTrue(emptyFacade.getAllBranches().isEmpty());
    }

    @Test
    void shouldAddRemoveAndSortBranchesByLoad() {
        facade.addBranch("Poznań", 5);
        assertEquals(5, facade.getAllBranches().size());

        facade.removeBranch("Poznań");
        assertEquals(4, facade.getAllBranches().size());

        List<Branch> branches = facade.getAllBranches();
        facade.sortBranchesByLoad(branches);

        // Po sortowaniu malejąco każdy kolejny oddział ma zapełnienie <= poprzedniego
        for (int i = 0; i < branches.size() - 1; i++) {
            assertTrue(branches.get(i).getCurrentLoad() >= branches.get(i + 1).getCurrentLoad());
        }
    }

    @Test
    void shouldAddRemoveAndUpdateLaptopsThroughFacade() throws Exception {
        Branch gdansk = facade.getAllBranches().stream()
                .filter(b -> b.getBranchName().equals("Gdańsk Wrzeszcz"))
                .findFirst().orElseThrow();

        Laptop newLaptop = new Laptop("Pavilion", "HP", LaptopCondition.SPRAWNY, 2022, 2800.0, 90.0, "Windows 11", 512);
        facade.addLaptopToBranch(gdansk, newLaptop);
        assertEquals(1, facade.getLaptopsInBranch(gdansk).size());

        facade.updateLaptopCondition(newLaptop, LaptopCondition.W_NAPRAWIE);
        assertEquals(LaptopCondition.W_NAPRAWIE, newLaptop.getCondition());

        facade.updateLaptopPrice(newLaptop, 2400.0);
        assertEquals(2400.0, newLaptop.getPrice());

        facade.reserveLaptop(newLaptop);
        assertEquals(LaptopCondition.ZAREZERWOWANY, newLaptop.getCondition());

        facade.removeLaptopFromBranch(gdansk, newLaptop);
        assertTrue(facade.getLaptopsInBranch(gdansk).isEmpty());
    }

    @Test
    void shouldThrowExceptionsOnNullInputsInFacade() throws InvalidPriceException {
        Laptop laptop = new Laptop("Test", "Dell", LaptopCondition.SPRAWNY, 2022, 1000.0, 90.0, "Linux", 256);

        assertTrue(facade.getLaptopsInBranch(null).isEmpty());
        assertThrows(LaptopNotFoundException.class, () -> facade.addLaptopToBranch(null, laptop));
        assertThrows(LaptopNotFoundException.class, () -> facade.removeLaptopFromBranch(null, laptop));
        assertThrows(LaptopNotFoundException.class, () -> facade.updateLaptopCondition(null, LaptopCondition.SPRAWNY));
        assertThrows(LaptopNotFoundException.class, () -> facade.updateLaptopPrice(null, 1500.0));
        assertThrows(LaptopNotFoundException.class, () -> facade.reserveLaptop(null));
    }

    @Test
    void shouldFilterLaptopsByQueryAndCondition() {
        Branch krakow = facade.getAllBranches().stream()
                .filter(b -> b.getBranchName().equals("Kraków Główny"))
                .findFirst().orElseThrow();

        // Brak oddziału -> pusta lista
        assertTrue(facade.filterLaptops(null, "Lenovo", LaptopCondition.SPRAWNY).isEmpty());

        // Pusty filtr i brak stanu -> wszystkie laptopy w Krakowie (5 szt.)
        assertEquals(5, facade.filterLaptops(krakow, "", null).size());

        // Tylko stan SPRAWNY -> ThinkPad T14 i MacBook Pro 14 (2 szt.)
        assertEquals(2, facade.filterLaptops(krakow, "   ", LaptopCondition.SPRAWNY).size());

        // Jednocześnie tekst "lenovo" + stan SPRAWNY -> tylko 1 szt.
        List<Laptop> combined = facade.filterLaptops(krakow, "lenovo", LaptopCondition.SPRAWNY);
        assertEquals(1, combined.size());
        assertEquals("ThinkPad T14", combined.get(0).getModel());

        // Oryginalna lista w oddziale NIE mogła zostać uszczuplona przez filtrowanie
        assertEquals(5, krakow.getLaptopCount());
    }

    @Test
    void shouldSortLaptopsByPriceAscending() {
        Branch krakow = facade.getAllBranches().stream()
                .filter(b -> b.getBranchName().equals("Kraków Główny"))
                .findFirst().orElseThrow();

        List<Laptop> laptops = facade.filterLaptops(krakow, "", null);
        facade.sortLaptopsByPrice(laptops);

        for (int i = 0; i < laptops.size() - 1; i++) {
            assertTrue(laptops.get(i).getPrice() <= laptops.get(i + 1).getPrice());
        }
    }


    @Test
    void shouldSaveAndReturnContactRequests() {
        Branch krakow = facade.getAllBranches().stream()
                .filter(b -> b.getBranchName().equals("Kraków Główny"))
                .findFirst().orElseThrow();
        Laptop laptop = facade.getLaptopsInBranch(krakow).get(0);

        assertTrue(facade.getContactRequests().isEmpty());
        facade.addContactRequest(krakow, laptop, "jan@kowalski.pl");

        List<String> requests = facade.getContactRequests();
        assertEquals(1, requests.size());
        assertTrue(requests.get(0).contains("jan@kowalski.pl"));
    }
}