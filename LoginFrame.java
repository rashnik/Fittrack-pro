import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Fitness Tracker - Login");
        setSize(420,220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30,30,80,25);
        panel.add(lblUser);

        usernameField = new JTextField();
        usernameField.setBounds(120,30,200,25);
        panel.add(usernameField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(30,70,80,25);
        panel.add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setBounds(120,70,200,25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150,110,100,30);
        panel.add(loginButton);

        add(panel);
        setVisible(true);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter credentials", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DataStore ds = DataStore.getInstance();
            User u = ds.authenticate(username, password);
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            dispose();
            if ("Admin".equalsIgnoreCase(u.getRole())) {
                new AdminDashboard();
            } else {
                new UserDashboard(u.getUsername());
            }
        });
    }
}
