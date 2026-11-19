import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenericTableModelTest {

    @Test
    void shouldExposeColumnsRowsAndValuesForGenericType() throws InvalidPriceException {
        GenericTableModel<Laptop> model = new GenericTableModel<>(
                new String[]{"Marka", "Model", "Cena"},
                List.of(Laptop::getBrand, Laptop::getModel, Laptop::getPrice)
        );

        assertEquals(3, model.getColumnCount());
        assertEquals("Marka", model.getColumnName(0));
        assertEquals(0, model.getRowCount());
        assertNull(model.getItemAt(0));

        Laptop l1 = new Laptop("ThinkPad", "Lenovo", LaptopCondition.SPRAWNY, 2022, 3000.0, 90.0, "Linux", 512);
        model.setData(List.of(l1));

        assertEquals(1, model.getRowCount());
        assertEquals("Lenovo", model.getValueAt(0, 0));
        assertEquals("ThinkPad", model.getValueAt(0, 1));
        assertEquals(3000.0, model.getValueAt(0, 2));
        assertEquals(l1, model.getItemAt(0));
        assertNull(model.getItemAt(-1));
        assertNull(model.getItemAt(5));
        assertEquals(1, model.getData().size());

        // Obsługa null w setData i refresh
        model.setData(null);
        assertEquals(0, model.getRowCount());
        assertDoesNotThrow(model::refresh);
    }
}