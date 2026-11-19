import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StoreManagerTest {

    private StoreManager manager;
    private Laptop laptop1;
    private Laptop laptop2;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        manager = new StoreManager();
        manager.addBranch("Kraków", 2);
        manager.addBranch("Warszawa", 1);
        manager.addBranch("Gdańsk", 3);

        laptop1 = new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.SPRAWNY, 2022, 3500.0, 90.0, "Windows 11", 512);
        laptop2 = new Laptop("MacBook Air", "Apple", LaptopCondition.SPRAWNY, 2023, 5000.0, 98.0, "macOS", 256);
    }

    @Test
    void shouldAddGetAndRemoveBranches() {
        assertEquals(3, manager.getAllBranches().size());
        assertNotNull(manager.getBranch("Kraków"));

        manager.removeBranch("Gdańsk");
        assertNull(manager.getBranch("Gdańsk"));
        assertEquals(2, manager.getAllBranches().size());
    }

    @Test
    void shouldFindEmptyBranchesAndPrintSummary() throws Exception {
        manager.getBranch("Kraków").addLaptop(laptop1);

        List<Branch> empty = manager.findEmpty();
        assertEquals(2, empty.size());
        assertDoesNotThrow(() -> manager.summary());
    }

    @Test
    void shouldCalculateTotalValueAndCountByBrand() throws Exception {
        manager.getBranch("Kraków").addLaptop(laptop1);
        manager.getBranch("Warszawa").addLaptop(laptop2);

        assertEquals(3500.0, manager.totalValue("Kraków"), 0.01);
        assertEquals(0.0, manager.totalValue("Nieistniejący"), 0.01);
        assertEquals(1, manager.countByBrand("Lenovo"));
        assertEquals(1, manager.countByBrand("Apple"));
        assertEquals(0, manager.countByBrand("Dell"));
    }

    @Test
    void shouldTransferLaptopBetweenBranches() throws Exception {
        manager.getBranch("Kraków").addLaptop(laptop1);

        manager.transferLaptop(laptop1, "Kraków", "Warszawa");

        assertEquals(0, manager.getBranch("Kraków").getLaptopCount());
        assertEquals(1, manager.getBranch("Warszawa").getLaptopCount());
    }

    @Test
    void shouldRollbackTransferWhenTargetBranchIsFullOrMissing() throws Exception {
        manager.getBranch("Kraków").addLaptop(laptop1);
        manager.getBranch("Warszawa").addLaptop(laptop2); // Warszawa ma pojemność 1 -> pełna

        // Próba przeniesienia do pełnego oddziału -> wyjątek, ale laptop1 zostaje w Krakowie
        assertThrows(BranchFullException.class, () -> manager.transferLaptop(laptop1, "Kraków", "Warszawa"));
        assertEquals(1, manager.getBranch("Kraków").getLaptopCount());

        // Próba przeniesienia do nieistniejącego oddziału
        assertThrows(LaptopNotFoundException.class, () -> manager.transferLaptop(laptop1, "Kraków", "Poznań"));
        assertThrows(LaptopNotFoundException.class, () -> manager.transferLaptop(laptop1, "Poznań", "Warszawa"));
    }

    @Test
    void shouldVerifyDataGeneratorSingletonAndPopulate() {
        DataGenerator g1 = DataGenerator.getInstance();
        DataGenerator g2 = DataGenerator.getInstance();
        // Singleton musi zwracać dokładnie tę samą referencję w pamięci
        assertSame(g1, g2);

        StoreManager populated = new StoreManager();
        g1.populate(populated);
        assertEquals(4, populated.getAllBranches().size());
        assertTrue(populated.getBranch("Kraków Główny").getLaptopCount() > 0);
    }
}