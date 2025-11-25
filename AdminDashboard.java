import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {
    private DefaultListModel<String> userListModel = new DefaultListModel<>();

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        build();
        setVisible(true);
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Admin"));
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        top.add(logout);

        // Center: user list and controls
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createTitledBorder("Users"));
        JList<String> userList = new JList<>(userListModel);
        center.add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"User","Admin"});
        JButton addBtn = new JButton("Add User");
        JButton delBtn = new JButton("Delete Selected");

        controls.add(new JLabel("Username:")); controls.add(usernameField);
        controls.add(new JLabel("Password:")); controls.add(passwordField);
        controls.add(new JLabel("Role:")); controls.add(roleBox);
        controls.add(addBtn); controls.add(delBtn);

        addBtn.addActionListener(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText().trim();
            String r = (String) roleBox.getSelectedItem();
            if (u.isEmpty() || p.isEmpty()) { JOptionPane.showMessageDialog(this, "Enter username and password"); return; }
            boolean ok = DataStore.getInstance().addUser(new User(u,p,r,u));
            if (!ok) JOptionPane.showMessageDialog(this, "User exists"); else { usernameField.setText(""); passwordField.setText(""); refreshUsers(); }
        });

        delBtn.addActionListener(e -> {
            String sel = userList.getSelectedValue();
            if (sel == null) { JOptionPane.showMessageDialog(this, "Select user to delete"); return; }
            DataStore.getInstance().removeUser(sel);
            refreshUsers();
        });

        root.add(top, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(controls, BorderLayout.EAST);

        add(root);
        refreshUsers();
    }

    private void refreshUsers() {
        userListModel.clear();
        for (User u : DataStore.getInstance().getAllUsers()) {
            userListModel.addElement(u.getUsername());
        }
    }
}
