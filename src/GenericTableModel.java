import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenericTableModel<T> extends AbstractTableModel {
    private List<T> data;
    private final String[] columnNames;
    private final List<Function<T, Object>> columnExtractors;

    public GenericTableModel(String[] columnNames, List<Function<T, Object>> columnExtractors) {
        this.data = new ArrayList<>();
        this.columnNames = columnNames;
        this.columnExtractors = columnExtractors;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // 1. Wyciągamy obiekt z danego wiersza (np. konkretny Laptop albo Branch)
        T item = data.get(rowIndex);
        // 2. Uruchamiamy funkcję przypisaną do tej kolumny (np. Laptop::getPrice)
        return columnExtractors.get(columnIndex).apply(item);
    }

    // Podmienia dane w tabeli i powiadamia JTable, że ma się przerysować
    public void setData(List<T> newData) {
        this.data = (newData != null) ? newData : new ArrayList<>();
        fireTableDataChanged();
    }

    // Zwraca referencję do aktualnej listy w tabeli (kluczowe do sortowania w miejscu!)
    public List<T> getData() {
        return data;
    }

    // Zwraca cały obiekt T z wybranego wiersza (np. po kliknięciu w tabeli)
    public T getItemAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= data.size()) {
            return null;
        }
        return data.get(rowIndex);
    }

    // Ręczne odświeżenie widoku po zmianie np. kolejności na liście lub stanu obiektu
    public void refresh() {
        fireTableDataChanged();
    }
}