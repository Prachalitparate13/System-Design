public class Logger {
     private Logger() {}

    private static class Holder {
        private static final Logger INSTANCE = new Logger();
    }

    public static Logger getInstance() {
        return Holder.INSTANCE;
    }

    public void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}
