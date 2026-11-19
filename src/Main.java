import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Tworzymy jedną instancję Fasady (od razu ładuje dane z Singletona DataGenerator)
        StoreFacade facade = new StoreFacade();

        // Bezpieczne uruchomienie interfejsu w wątku zdarzeń Swinga (EDT)
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame(facade);
            loginFrame.setVisible(true);
        });
    }
}