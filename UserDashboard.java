import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

public class UserDashboard extends JFrame {
    private String username;
    private DefaultListModel<String> listModel = new DefaultListModel<>();

    public UserDashboard(String username) {
        this.username = username;
        setTitle("User Dashboard - " + username);
        setSize(700,500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        build();
        setVisible(true);
    }

    private void build() {
        JPanel root = new JPanel(new BorderLayout(10,10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Logged in as: " + username));
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });
        top.add(logout);

        // Left: form to add workout
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createTitledBorder("Log Workout"));

        String[] types = {"Cardio","Strength","Yoga","Flexibility","Other"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        JTextField durField = new JTextField();
        JTextField calField = new JTextField();
        JTextArea notes = new JTextArea(4,20);
        JButton saveBtn = new JButton("Save Workout");

        left.add(new JLabel("Type:")); left.add(typeBox);
        left.add(new JLabel("Duration (min):")); left.add(durField);
        left.add(new JLabel("Calories:")); left.add(calField);
        left.add(new JLabel("Notes:")); left.add(new JScrollPane(notes));
        left.add(saveBtn);

        // Right: list of workouts + stats
        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Recent Workouts"));
        JList<String> list = new JList<>(listModel);
        right.add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel stats = new JPanel(new GridLayout(2,1));
        JLabel totalWorkouts = new JLabel();
        JLabel totalCalories = new JLabel();
        stats.add(totalWorkouts);
        stats.add(totalCalories);
        right.add(stats, BorderLayout.SOUTH);

        saveBtn.addActionListener(e -> {
            try {
                String type = (String) typeBox.getSelectedItem();
                int dur = Integer.parseInt(durField.getText().trim());
                int cal = Integer.parseInt(calField.getText().trim());
                String note = notes.getText().trim();
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                Workout w = new Workout(type, dur, cal, now, note);
                DataStore ds = DataStore.getInstance();
                ds.addWorkout(username, w);
                refreshList();
                durField.setText(""); calField.setText(""); notes.setText(""); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter numeric duration and calories", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        root.add(top, BorderLayout.NORTH);
        root.add(left, BorderLayout.WEST);
        root.add(right, BorderLayout.CENTER);

        add(root);
        refreshList();
    }

    private void refreshList() {
        listModel.clear();
        DataStore ds = DataStore.getInstance();
        User u = ds.authenticate(username, ds.getAllUsers().stream().filter(x -> x.getUsername().equalsIgnoreCase(username)).findFirst().get().getPassword()); // hacky access
        // Better: fetch user from datastore directly
        User user = ds.getAllUsers().stream().filter(x -> x.getUsername().equalsIgnoreCase(username)).findFirst().orElse(null);
        if (user == null) return;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM HH:mm");
        int total = 0;
        int calories = 0;
        for (Workout w : user.getWorkouts()) {
            listModel.addElement(String.format("%s — %d min — %d kcal — %s", w.getType(), w.getDuration(), w.getCalories(), w.getPerformedAt().format(dtf)));
            total++;
            calories += w.getCalories();
        }
    }
}
