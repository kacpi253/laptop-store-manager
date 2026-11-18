import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;

public class GenericTablePanel<T> extends JPanel {
    private final GenericTableModel<T> tableModel;
    private final JTable table;

    public GenericTablePanel(String title, String[] columnNames, List<Function<T, Object>> extractors) {
        // BorderLayout rozciąga główny komponent (CENTER) na całą dostępną przestrzeń
        super(new BorderLayout());

        // Estetyczna ramka z tytułem tabeli
        setBorder(BorderFactory.createTitledBorder(title));

        this.tableModel = new GenericTableModel<>(columnNames, extractors);
        this.table = new JTable(tableModel);

        // Pozwalamy zaznaczać jeden wiersz naraz (wygodniejsze przy usuwaniu i podglądzie oddziału)
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setFillsViewportHeight(true);

        // JScrollPane - bez niego nie wyświetlają się nagłówki kolumn i paski przewijania
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public T getSelectedItem() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        int modelRow = table.convertRowIndexToModel(selectedRow);
        return tableModel.getItemAt(modelRow);
    }

    public void setData(List<T> data) {
        tableModel.setData(data);
    }

    public List<T> getData() {
        return tableModel.getData();
    }

    public void refresh() {
        tableModel.refresh();
    }

    public JTable getTable() {
        return table;
    }
}