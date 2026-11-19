import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final StoreFacade facade;
    private final JTextField loginField;
    private final JPasswordField passwordField;

    public LoginFrame(StoreFacade facade) {
        super("Logowanie - Sieć Sklepów z Laptopami");
        this.facade = facade;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 260);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nagłówek zajmujący 2 kolumny
        JLabel titleLabel = new JLabel("System zarządzania sklepem", SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 16f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Wiersz 1: Login
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Login:"), gbc);

        loginField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(loginField, gbc);

        // Wiersz 2: Hasło
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("Hasło:"), gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Wiersz 3: Przycisk logowania (Enter w polu hasła też go uruchomi!)
        JButton loginButton = new JButton("Zaloguj");
        loginButton.addActionListener(e -> handleLogin());
        passwordField.addActionListener(e -> handleLogin());

        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        // Wiersz 4: Podpowiedź kont do szybkiego testowania na zaliczeniu
        JLabel hintLabel = new JLabel("Konta: admin / admin  |  klient / klient", SwingConstants.CENTER);
        hintLabel.setForeground(Color.GRAY);
        gbc.gridy = 4;
        panel.add(hintLabel, gbc);

        add(panel);
    }

    private void handleLogin() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        // Akcja powrotu po kliknięciu "Wyloguj" w panelu admina lub klienta
        Runnable returnToLogin = () -> new LoginFrame(facade).setVisible(true);

        if (login.equals("admin") && password.equals("admin")) {
            dispose();
            new AdminFrame(facade, returnToLogin).setVisible(true);
        } else if (login.equals("klient") && password.equals("klient")) {
            dispose();
            new ClientFrame(facade, returnToLogin).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Nieprawidłowy login lub hasło!\nUżyj: admin/admin lub klient/klient",
                    "Błąd logowania",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}