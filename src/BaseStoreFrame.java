import javax.swing.*;
import java.awt.*;
import java.util.List;

public abstract class BaseStoreFrame extends JFrame {
    protected final StoreFacade facade;
    protected final GenericTablePanel<Branch> branchTablePanel;
    protected final GenericTablePanel<Laptop> laptopTablePanel;

    private final JTextField filterTextField;
    private final JComboBox<Object> conditionComboBox;
    private final Runnable onLogout;

    public BaseStoreFrame(String title, String roleLabel, StoreFacade facade, Runnable onLogout) {
        super(title);
        this.facade = facade;
        this.onLogout = onLogout;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null); // Wyśrodkowanie okna na ekranie
        setLayout(new BorderLayout(10, 10));

        // 1. GÓRNY PASEK (rola + wylogowanie)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 0, 12));
        JLabel userLabel = new JLabel("Zalogowano jako: " + roleLabel);
        userLabel.setFont(userLabel.getFont().deriveFont(Font.BOLD, 14f));

        JButton logoutButton = new JButton("Wyloguj");
        logoutButton.addActionListener(e -> {
            dispose(); // Zamyka bieżące okno
            if (this.onLogout != null) {
                this.onLogout.run(); // Wraca do okna logowania z tą samą Fasadą
            }
        });
        headerPanel.add(userLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // 2. TABELA ODDZIAŁÓW (nasz generyczny komponent dla typu Branch)
        branchTablePanel = new GenericTablePanel<>(
                "Oddziały sklepu",
                new String[]{"Nazwa oddziału", "Liczba laptopów", "Pojemność", "Zapełnienie (%)"},
                List.of(
                        Branch::getBranchName,
                        Branch::getLaptopCount,
                        Branch::getMaxCapacity,
                        b -> String.format("%.1f%%", b.getCurrentLoad())
                )
        );

        // Przycisk sortowania oddziałów po aktualnym zapełnieniu (przekazanie referencji listy)
        JButton sortBranchesButton = new JButton("Sortuj oddziały wg zapełnienia");
        sortBranchesButton.addActionListener(e -> {
            facade.sortBranchesByLoad(branchTablePanel.getData());
            branchTablePanel.refresh();
        });

        JPanel branchTopSection = new JPanel(new BorderLayout(5, 5));
        branchTopSection.add(branchTablePanel, BorderLayout.CENTER);
        JPanel branchSortBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        branchSortBar.add(sortBranchesButton);
        branchTopSection.add(branchSortBar, BorderLayout.SOUTH);

        // 3. PASEK FILTRÓW + TABELA LAPTOPÓW (ten sam generyczny komponent dla typu Laptop)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.add(new JLabel("Filtr (model/marka + Enter):"));

        filterTextField = new JTextField(18);
        // ActionListener w JTextField uruchamia się po naciśnięciu klawisza Enter
        filterTextField.addActionListener(e -> refreshLaptopTable());
        filterPanel.add(filterTextField);

        filterPanel.add(new JLabel("Stan:"));
        conditionComboBox = new JComboBox<>();
        conditionComboBox.addItem("Wszystkie"); // Pierwsza opcja = brak filtra stanu (null)
        for (LaptopCondition condition : LaptopCondition.values()) {
            conditionComboBox.addItem(condition); // JComboBox sam wywoła Twoje toString()
        }
        conditionComboBox.addActionListener(e -> refreshLaptopTable());
        filterPanel.add(conditionComboBox);


        laptopTablePanel = new GenericTablePanel<>(
                "Laptopy w wybranym oddziale",
                new String[]{"Marka", "Model", "Stan", "Rok", "Cena (PLN)", "Bateria (%)", "System", "Dysk (GB)"},
                List.of(
                        Laptop::getBrand,
                        Laptop::getModel,
                        Laptop::getCondition,
                        Laptop::getYear,
                        Laptop::getPrice,
                        Laptop::getBatteryHealth,
                        Laptop::getOperatingSystem,
                        Laptop::getStorageGB
                )
        );

        JButton sortByPriceButton = new JButton("Sortuj wg ceny");
        sortByPriceButton.addActionListener(e -> {
            facade.sortLaptopsByPrice(laptopTablePanel.getData());
            laptopTablePanel.refresh();
        });
        filterPanel.add(sortByPriceButton);


        JPanel laptopBottomSection = new JPanel(new BorderLayout(5, 5));
        laptopBottomSection.add(filterPanel, BorderLayout.NORTH);
        laptopBottomSection.add(laptopTablePanel, BorderLayout.CENTER);

        // Połączenie obu sekcji dzielnikiem (JSplitPane)
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, branchTopSection, laptopBottomSection);
        splitPane.setDividerLocation(210);
        splitPane.setResizeWeight(0.35);
        add(splitPane, BorderLayout.CENTER);

        // 4. REAKCJA NA KLIKNIĘCIE ODDZIAŁU W GÓRNEJ TABELI
        branchTablePanel.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                refreshLaptopTable();
            }
        });

        // 5. DOLNY PANEL AKCJI (każda rola wypełni go po swojemu)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        setupActionPanel(actionPanel);
        add(actionPanel, BorderLayout.SOUTH);

        // Załadowanie początkowych danych oddziałów z Fasady
        reloadBranches();
    }

    // Metoda abstrakcyjna – klasy potomne (AdminFrame i ClientFrame) dodają tu swoje przyciski
    protected abstract void setupActionPanel(JPanel actionPanel);

    protected void reloadBranches() {
        branchTablePanel.setData(facade.getAllBranches());
        refreshLaptopTable();
    }



    protected void refreshLaptopTable() {
        Branch selectedBranch = branchTablePanel.getSelectedItem();
        String query = filterTextField.getText();

        Object selectedOption = conditionComboBox.getSelectedItem();
        // Jeśli wybrano "Wszystkie" (String), przekazujemy null; jeśli enum, rzutujemy na LaptopCondition
        LaptopCondition condition = (selectedOption instanceof LaptopCondition)
                ? (LaptopCondition) selectedOption
                : null;

        List<Laptop> filtered = facade.filterLaptops(selectedBranch, query, condition);
        laptopTablePanel.setData(filtered);
    }

    // Pomocnicze okienka dialogowe do obsługi wyjątków i komunikatów
    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Błąd", JOptionPane.ERROR_MESSAGE);
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Informacja", JOptionPane.INFORMATION_MESSAGE);
    }
}