import java.io.*;
import java.util.*;

public class DataStore implements Serializable {
    private static final long serialVersionUID = 1L;
    private static DataStore instance;
    private Map<String, User> users = new HashMap<>();
    private static final String FILE = "data.ser";

    private DataStore() {}

    public static synchronized DataStore getInstance() {
        if (instance == null) instance = new DataStore();
        return instance;
    }

    public synchronized void load() {
        File f = new File(FILE);
        if (!f.exists()) return;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            DataStore ds = (DataStore) ois.readObject();
            this.users = ds.users != null ? ds.users : new HashMap<>();
        } catch (Exception e) {
            System.err.println("Failed to load data.ser: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(this);
        } catch (Exception e) {
            System.err.println("Failed to save data.ser: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public synchronized User authenticate(String username, String password) {
        if (username == null) return null;
        User u = users.get(username.toLowerCase());
        if (u != null && u.getPassword().equals(password)) return u;
        return null;
    }

    public synchronized boolean addUser(User user) {
        if (users.containsKey(user.getUsername().toLowerCase())) return false;
        users.put(user.getUsername().toLowerCase(), user);
        save();
        return true;
    }

    public synchronized boolean removeUser(String username) {
        if (users.remove(username.toLowerCase()) != null) {
            save();
            return true;
        }
        return false;
    }

    public synchronized Collection<User> getAllUsers() { return users.values(); }

    public synchronized void addWorkout(String username, Workout w) {
        User u = users.get(username.toLowerCase());
        if (u != null) {
            u.getWorkouts().add(0, w);
            save();
        }
    }

    public void seedDemoDataIfEmpty() {
        if (!users.isEmpty()) return;
        addUser(new User("admin","admin123","Admin","Administrator"));
        addUser(new User("user1","pass123","User","Demo User"));
        addWorkout("user1", new Workout("Cardio", 30, 300, java.time.LocalDateTime.now(), "Morning run"));
    }
}
