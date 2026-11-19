import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminFrame extends BaseStoreFrame {

    public AdminFrame(StoreFacade facade, Runnable onLogout) {
        super("Sklep z laptopami - Panel Administratora", "Administrator", facade, onLogout);
    }

    @Override
    protected void setupActionPanel(JPanel actionPanel) {
        // Układ 2 wiersze x 4 kolumny, żeby 7 przycisków zawsze wyglądało równo
        actionPanel.setLayout(new GridLayout(2, 4, 8, 8));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 10, 12));

        JButton addBranchBtn = new JButton("Dodaj oddział");
        JButton removeBranchBtn = new JButton("Usuń oddział");
        JButton addLaptopBtn = new JButton("Dodaj laptop");
        JButton removeLaptopBtn = new JButton("Usuń laptop");
        JButton editConditionBtn = new JButton("Edytuj stan");
        JButton editPriceBtn = new JButton("Edytuj cenę");
        JButton contactRequestsBtn = new JButton("Prośby o kontakt");

        addBranchBtn.addActionListener(e -> handleAddBranch());
        removeBranchBtn.addActionListener(e -> handleRemoveBranch());
        addLaptopBtn.addActionListener(e -> handleAddLaptop());
        removeLaptopBtn.addActionListener(e -> handleRemoveLaptop());
        editConditionBtn.addActionListener(e -> handleEditCondition());
        editPriceBtn.addActionListener(e -> handleEditPrice());
        contactRequestsBtn.addActionListener(e -> handleShowContactRequests());

        actionPanel.add(addBranchBtn);
        actionPanel.add(removeBranchBtn);
        actionPanel.add(addLaptopBtn);
        actionPanel.add(removeLaptopBtn);
        actionPanel.add(editConditionBtn);
        actionPanel.add(editPriceBtn);
        actionPanel.add(contactRequestsBtn);
    }

    private void handleAddBranch() {
        JTextField nameField = new JTextField();
        JTextField capacityField = new JTextField();

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Nazwa oddziału:"));
        form.add(nameField);
        form.add(new JLabel("Maks. pojemność:"));
        form.add(capacityField);

        int result = JOptionPane.showConfirmDialog(this, form, "Dodaj nowy oddział",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showError("Nazwa oddziału nie może być pusta.");
                return;
            }
            try {
                int capacity = Integer.parseInt(capacityField.getText().trim());
                if (capacity <= 0) {
                    showError("Pojemność musi być liczbą większą od 0.");
                    return;
                }
                facade.addBranch(name, capacity);
                reloadBranches();
            } catch (NumberFormatException ex) {
                showError("Pojemność musi być poprawną liczbą całkowitą.");
            }
        }
    }

    private void handleRemoveBranch() {
        Branch selected = branchTablePanel.getSelectedItem();
        if (selected == null) {
            showError("Zaznacz oddział, który chcesz usunąć.");
            return;
        }
        facade.removeBranch(selected.getBranchName());
        reloadBranches();
    }

    private void handleAddLaptop() {
        Branch selectedBranch = branchTablePanel.getSelectedItem();
        if (selectedBranch == null) {
            showError("Najpierw zaznacz oddział, do którego chcesz dodać laptopa.");
            return;
        }

        JTextField brandField = new JTextField();
        JTextField modelField = new JTextField();
        JComboBox<LaptopCondition> conditionBox = new JComboBox<>(LaptopCondition.values());
        JTextField yearField = new JTextField("2023");
        JTextField priceField = new JTextField("2500.0");
        JTextField batteryField = new JTextField("90.0");
        JTextField osField = new JTextField("Windows 11");
        JTextField storageField = new JTextField("512");

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.add(new JLabel("Marka:"));
        form.add(brandField);
        form.add(new JLabel("Model:"));
        form.add(modelField);
        form.add(new JLabel("Stan:"));
        form.add(conditionBox);
        form.add(new JLabel("Rok produkcji:"));
        form.add(yearField);
        form.add(new JLabel("Cena (PLN):"));
        form.add(priceField);
        form.add(new JLabel("Kondycja baterii (%):"));
        form.add(batteryField);
        form.add(new JLabel("System operacyjny:"));
        form.add(osField);
        form.add(new JLabel("Dysk (GB):"));
        form.add(storageField);

        int result = JOptionPane.showConfirmDialog(this, form,
                "Dodaj laptop do: " + selectedBranch.getBranchName(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String brand = brandField.getText().trim();
                String model = modelField.getText().trim();
                LaptopCondition condition = (LaptopCondition) conditionBox.getSelectedItem();
                int year = Integer.parseInt(yearField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                double battery = Double.parseDouble(batteryField.getText().trim());
                String os = osField.getText().trim();
                int storage = Integer.parseInt(storageField.getText().trim());

                if (brand.isEmpty() || model.isEmpty()) {
                    showError("Marka i model nie mogą być puste.");
                    return;
                }

                // Uwaga na kolejność argumentów Twojego konstruktora Laptop: najpierw model, potem brand
                Laptop newLaptop = new Laptop(model, brand, condition, year, price, battery, os, storage);
                facade.addLaptopToBranch(selectedBranch, newLaptop);

                branchTablePanel.refresh();
                refreshLaptopTable();
            } catch (NumberFormatException ex) {
                showError("Błędny format liczby (sprawdź rok, cenę, baterię lub dysk).");
            } catch (InvalidPriceException | BranchFullException | DuplicateLaptopException | LaptopNotFoundException ex) {
                // Przechwycenie wszystkich naszych własnych wyjątków checked i pokazanie ich w JOptionPane
                showError(ex.getMessage());
            }
        }
    }

    private void handleRemoveLaptop() {
        Branch selectedBranch = branchTablePanel.getSelectedItem();
        Laptop selectedLaptop = laptopTablePanel.getSelectedItem();

        try {
            facade.removeLaptopFromBranch(selectedBranch, selectedLaptop);
            branchTablePanel.refresh();
            refreshLaptopTable();
        } catch (LaptopNotFoundException ex) {
            showError(ex.getMessage());
        }
    }

    private void handleEditCondition() {
        Laptop selectedLaptop = laptopTablePanel.getSelectedItem();
        if (selectedLaptop == null) {
            showError("Zaznacz laptopa, którego stan chcesz zmienić.");
            return;
        }

        LaptopCondition newCondition = (LaptopCondition) JOptionPane.showInputDialog(
                this,
                "Wybierz nowy stan dla: " + selectedLaptop.getBrand() + " " + selectedLaptop.getModel(),
                "Edycja stanu",
                JOptionPane.PLAIN_MESSAGE,
                null,
                LaptopCondition.values(),
                selectedLaptop.getCondition()
        );

        if (newCondition != null) {
            try {
                facade.updateLaptopCondition(selectedLaptop, newCondition);
                refreshLaptopTable();
            } catch (LaptopNotFoundException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void handleEditPrice() {
        Laptop selectedLaptop = laptopTablePanel.getSelectedItem();
        if (selectedLaptop == null) {
            showError("Zaznacz laptopa, którego cenę chcesz zmienić.");
            return;
        }

        String input = JOptionPane.showInputDialog(
                this,
                "Podaj nową cenę (PLN) dla: " + selectedLaptop.getBrand() + " " + selectedLaptop.getModel(),
                selectedLaptop.getPrice()
        );

        if (input != null) {
            try {
                double newPrice = Double.parseDouble(input.trim());
                facade.updateLaptopPrice(selectedLaptop, newPrice);
                refreshLaptopTable();
            } catch (NumberFormatException ex) {
                showError("Wprowadź poprawną wartość liczbową.");
            } catch (LaptopNotFoundException | InvalidPriceException ex) {
                showError(ex.getMessage());
            }
        }
    }

    private void handleShowContactRequests() {
        List<String> requests = facade.getContactRequests();
        if (requests.isEmpty()) {
            showInfo("Brak nowych próśb o kontakt od klientów.");
            return;
        }

        StringBuilder sb = new StringBuilder("Zgłoszenia od klientów:\n\n");
        for (int i = 0; i < requests.size(); i++) {
            sb.append(i + 1).append(". ").append(requests.get(i)).append("\n");
        }
        showInfo(sb.toString());
    }
}