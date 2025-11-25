public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            DataStore ds = DataStore.getInstance();
            ds.load();
            ds.seedDemoDataIfEmpty();
            new LoginFrame();
        });
    }
}
