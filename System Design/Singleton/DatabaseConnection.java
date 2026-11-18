public class DatabaseConnection {
    private static volatile DatabaseConnection instance;

    private DatabaseConnection() {
        // expensive setup
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {                      // first check, no lock
            synchronized (DatabaseConnection.class) {
                if (instance == null) {              // second check, with lock
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }
}
