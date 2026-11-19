import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BranchTest {

    private Branch branch;
    private Laptop lenovo;
    private Laptop apple;
    private Laptop dell;

    @BeforeEach
    void setUp() throws InvalidPriceException {
        branch = new Branch("Kraków Główny", 3);
        lenovo = new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.SPRAWNY, 2022, 3500.0, 92.0, "Windows 11", 512);
        apple = new Laptop("MacBook Pro", "Apple", LaptopCondition.SPRAWNY, 2023, 8500.0, 99.0, "macOS", 512);
        dell = new Laptop("XPS 15", "Dell", LaptopCondition.USZKODZONY, 2021, 5000.0, 80.0, "Windows 11", 1024);
    }

    @Test
    void shouldAddLaptopsAndCalculateLoadCorrectly() throws Exception {
        assertTrue(branch.isEmpty());
        assertEquals(0.0, branch.getCurrentLoad());

        branch.addLaptop(lenovo);
        branch.addLaptop(apple);

        assertFalse(branch.isEmpty());
        assertEquals(2, branch.getLaptopCount());
        assertEquals(3, branch.getMaxCapacity());
        assertEquals("Kraków Główny", branch.getBranchName());
        assertEquals(2.0 / 3.0 * 100.0, branch.getCurrentLoad(), 0.01);
    }

    @Test
    void shouldReturnZeroLoadWhenMaxCapacityIsZero() {
        Branch zeroCap = new Branch("Pusty", 0);
        assertEquals(0.0, zeroCap.getCurrentLoad());
    }

    @Test
    void shouldThrowBranchFullExceptionWhenCapacityExceeded() throws Exception {
        branch.addLaptop(lenovo);
        branch.addLaptop(apple);
        branch.addLaptop(dell);

        Laptop extra = new Laptop("ZenBook", "Asus", LaptopCondition.SPRAWNY, 2022, 4000.0, 90.0, "Windows 11", 512);
        assertThrows(BranchFullException.class, () -> branch.addLaptop(extra));
    }

    @Test
    void shouldThrowDuplicateLaptopExceptionWhenAddingSameLaptop() throws Exception {
        branch.addLaptop(lenovo);
        Laptop duplicate = new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.W_NAPRAWIE, 2022, 2000.0, 70.0, "Linux", 256);

        assertThrows(DuplicateLaptopException.class, () -> branch.addLaptop(duplicate));
    }

    @Test
    void shouldRemoveAndSellLaptopOrThrowNotFound() throws Exception {
        branch.addLaptop(lenovo);
        branch.addLaptop(apple);

        branch.removeLaptop(lenovo);
        assertEquals(1, branch.getLaptopCount());

        branch.sellLaptop(apple);
        assertTrue(branch.isEmpty());

        assertThrows(LaptopNotFoundException.class, () -> branch.removeLaptop(dell));
        assertThrows(LaptopNotFoundException.class, () -> branch.sellLaptop(dell));
    }

    @Test
    void shouldChangeConditionAndYear() {
        branch.changeCondition(lenovo, LaptopCondition.W_NAPRAWIE);
        branch.changeYear(lenovo, 2020);

        assertEquals(LaptopCondition.W_NAPRAWIE, lenovo.getCondition());
        assertEquals(2020, lenovo.getYear());
    }

    @Test
    void shouldCountByConditionAndBrandAndCalculateTotalValue() throws Exception {
        branch.addLaptop(lenovo);
        branch.addLaptop(apple);
        branch.addLaptop(dell);

        assertEquals(2, branch.countByCondition(LaptopCondition.SPRAWNY));
        assertEquals(1, branch.countByCondition(LaptopCondition.USZKODZONY));
        assertEquals(1, branch.countByBrand("lenovo"));
        assertEquals(17000.0, branch.totalValue(), 0.01);
    }

    @Test
    void shouldSortByNameAndSortByPrice() throws Exception {
        branch.addLaptop(dell);   // XPS 15, 5000
        branch.addLaptop(lenovo); // ThinkPad T14, 3500
        branch.addLaptop(apple);  // MacBook Pro, 8500

        List<Laptop> byName = branch.sortByName();
        assertEquals("MacBook Pro", byName.get(0).getModel());
        assertEquals("ThinkPad T14", byName.get(1).getModel());
        assertEquals("XPS 15", byName.get(2).getModel());

        List<Laptop> byPrice = branch.sortByPrice();
        assertEquals(3500.0, byPrice.get(0).getPrice());
        assertEquals(5000.0, byPrice.get(1).getPrice());
        assertEquals(8500.0, byPrice.get(2).getPrice());
    }

    @Test
    void shouldSearchExactAndSearchPartialByModelOrBrand() throws Exception {
        branch.addLaptop(lenovo);
        branch.addLaptop(apple);
        branch.addLaptop(dell);

        assertEquals(1, branch.search("thinkpad t14").size());
        assertEquals(1, branch.searchPartial("book").size());   // po modelu
        assertEquals(1, branch.searchPartial("dell").size());   // po marce
        assertTrue(branch.searchPartial("nieistnieje").isEmpty());
    }

    @Test
    void shouldReturnMaxPricedLaptopOrNullWhenEmpty() throws Exception {
        assertNull(branch.max());

        branch.addLaptop(lenovo);
        branch.addLaptop(apple);
        branch.addLaptop(dell);

        assertEquals(apple, branch.max());
        assertDoesNotThrow(() -> branch.summary());
    }

    @Test
    void shouldTransferOldestOfTwoExpensiveAndHandleErrors() throws Exception {
        Branch target = new Branch("Warszawa", 2);

        // Za mało laptopów (< 2)
        branch.addLaptop(apple);
        assertThrows(LaptopNotFoundException.class, () -> branch.transferOldestOfTwoExpensive(target));
        assertThrows(LaptopNotFoundException.class, () -> branch.transferOldestOfTwoExpensive(null));

        // 2 najdroższe: apple (8500, 2023) i dell (5000, 2021) -> starszy to dell
        branch.addLaptop(lenovo);
        branch.addLaptop(dell);
        branch.transferOldestOfTwoExpensive(target);

        assertEquals(2, branch.getLaptopCount());
        assertEquals(1, target.getLaptopCount());
        assertTrue(target.getLaptopList().contains(dell));

        // Pełny oddział docelowy
        Branch fullTarget = new Branch("Pełny", 0);
        assertThrows(BranchFullException.class, () -> branch.transferOldestOfTwoExpensive(fullTarget));
    }
}