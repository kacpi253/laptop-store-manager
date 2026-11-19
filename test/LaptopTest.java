import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LaptopTest {

    private Laptop laptop;

    // Test Fixture: przed każdym testem tworzymy świeży, czysty obiekt Laptop
    @BeforeEach
    void setUp() throws InvalidPriceException {
        laptop = new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.SPRAWNY,
                2022, 3500.0, 92.5, "Windows 11", 512);
    }

    @Test
    void shouldCreateLaptopWithValidFields() {
        assertEquals("ThinkPad T14", laptop.getModel());
        assertEquals("Lenovo", laptop.getBrand());
        assertEquals(LaptopCondition.SPRAWNY, laptop.getCondition());
        assertEquals(2022, laptop.getYear());
        assertEquals(3500.0, laptop.getPrice());
        assertEquals(92.5, laptop.getBatteryHealth());
        assertEquals("Windows 11", laptop.getOperatingSystem());
        assertEquals(512, laptop.getStorageGB());
    }

    @Test
    void shouldThrowInvalidPriceExceptionWhenCreatingWithNegativePrice() {
        // Sprawdzamy, czy konstruktor rzuci nasz własny wyjątek dla ceny < 0
        assertThrows(InvalidPriceException.class, () ->
                new Laptop("MacBook", "Apple", LaptopCondition.SPRAWNY,
                        2023, -100.0, 95.0, "macOS", 512)
        );
    }

    @Test
    void shouldUpdateFieldsUsingSetters() throws InvalidPriceException {
        laptop.setModel("XPS 15");
        laptop.setBrand("Dell");
        laptop.setCondition(LaptopCondition.W_NAPRAWIE);
        laptop.setYear(2023);
        laptop.setPrice(5000.0);
        laptop.setBatteryHealth(88.0);
        laptop.setOperatingSystem("Linux");
        laptop.setStorageGB(1024);

        assertEquals("XPS 15", laptop.getModel());
        assertEquals("Dell", laptop.getBrand());
        assertEquals(LaptopCondition.W_NAPRAWIE, laptop.getCondition());
        assertEquals(2023, laptop.getYear());
        assertEquals(5000.0, laptop.getPrice());
        assertEquals(88.0, laptop.getBatteryHealth());
        assertEquals("Linux", laptop.getOperatingSystem());
        assertEquals(1024, laptop.getStorageGB());
    }

    @Test
    void shouldThrowInvalidPriceExceptionWhenSettingNegativePrice() {
        assertThrows(InvalidPriceException.class, () -> laptop.setPrice(-1.0));
    }

    @Test
    void shouldCompareLaptopsByModelThenBrandThenYear() throws InvalidPriceException {
        Laptop sameLaptop = new Laptop("thinkpad t14", "lenovo", LaptopCondition.USZKODZONY,
                2022, 1000.0, 50.0, "Linux", 256);
        Laptop differentModel = new Laptop("ZenBook", "Asus", LaptopCondition.SPRAWNY,
                2022, 3500.0, 90.0, "Windows 11", 512);
        Laptop sameModelDifferentBrand = new Laptop("ThinkPad T14", "Acer", LaptopCondition.SPRAWNY,
                2022, 3500.0, 90.0, "Windows 11", 512);
        Laptop newerSameModelAndBrand = new Laptop("ThinkPad T14", "Lenovo", LaptopCondition.SPRAWNY,
                2024, 4000.0, 99.0, "Windows 11", 512);

        // Identyczny model, marka (ignorując wielkość liter) i rok -> 0
        assertEquals(0, laptop.compareTo(sameLaptop));
        // "ThinkPad T14" < "ZenBook" -> wynik ujemny
        assertTrue(laptop.compareTo(differentModel) < 0);
        // Ten sam model, ale "Lenovo" > "Acer" -> wynik dodatni
        assertTrue(laptop.compareTo(sameModelDifferentBrand) > 0);
        // Ten sam model i marka, ale 2022 < 2024 -> wynik ujemny
        assertTrue(laptop.compareTo(newerSameModelAndBrand) < 0);
    }

    @Test
    void shouldFormatToStringAndPrintWithoutErrors() {
        String text = laptop.toString();
        assertTrue(text.contains("ThinkPad T14"));
        assertTrue(text.contains("Lenovo"));
        assertTrue(text.contains("Sprawny"));
        assertDoesNotThrow(() -> laptop.print());
    }

    @Test
    void shouldReturnCorrectConditionStrings() {
        assertEquals("Sprawny", LaptopCondition.SPRAWNY.toString());
        assertEquals("Uszkodzony", LaptopCondition.USZKODZONY.toString());
        assertEquals("W naprawie", LaptopCondition.W_NAPRAWIE.toString());
        assertEquals("W trakcie testów", LaptopCondition.W_TRAKCIE_TESTOW.toString());
        assertEquals("Zarezerwowany", LaptopCondition.ZAREZERWOWANY.toString());
    }
}