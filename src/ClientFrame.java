import javax.swing.*;

public class ClientFrame extends BaseStoreFrame {

    public ClientFrame(StoreFacade facade, Runnable onLogout) {
        super("Sklep z laptopami - Panel Klienta", "Klient", facade, onLogout);
    }

    @Override
    protected void setupActionPanel(JPanel actionPanel) {
        JButton reserveButton = new JButton("Zarezerwuj laptop");
        JButton contactButton = new JButton("Poproś o kontakt");

        reserveButton.addActionListener(e -> handleReserveLaptop());
        contactButton.addActionListener(e -> handleContactRequest());

        actionPanel.add(reserveButton);
        actionPanel.add(contactButton);
    }

    private void handleReserveLaptop() {
        Laptop selectedLaptop = laptopTablePanel.getSelectedItem();
        if (selectedLaptop != null && selectedLaptop.getCondition() == LaptopCondition.ZAREZERWOWANY) {
            showInfo("Ten laptop jest już zarezerwowany.");
            return;
        }

        try {
            facade.reserveLaptop(selectedLaptop);
            refreshLaptopTable();
            showInfo("Laptop " + selectedLaptop.getBrand() + " " + selectedLaptop.getModel() + " został zarezerwowany!");
        } catch (LaptopNotFoundException ex) {
            // Obsługa naszego własnego wyjątku za pomocą JOptionPane
            showError(ex.getMessage());
        }
    }

    private void handleContactRequest() {
        Branch selectedBranch = branchTablePanel.getSelectedItem();
        Laptop selectedLaptop = laptopTablePanel.getSelectedItem();

        if (selectedBranch == null || selectedLaptop == null) {
            showError("Najpierw wybierz oddział oraz konkretny laptop z tabeli.");
            return;
        }

        String clientContact = JOptionPane.showInputDialog(
                this,
                "Podaj swój telefon lub e-mail w sprawie: " + selectedLaptop.getBrand() + " " + selectedLaptop.getModel(),
                "Prośba o kontakt",
                JOptionPane.QUESTION_MESSAGE
        );

        // Jeśli użytkownik kliknął Anuluj (null) albo zostawił puste pole, nic nie zapisujemy
        if (clientContact != null && !clientContact.trim().isEmpty()) {
            facade.addContactRequest(selectedBranch, selectedLaptop, clientContact.trim());
            showInfo("Twoja prośba o kontakt została wysłana do administratora!");
        }
    }
}