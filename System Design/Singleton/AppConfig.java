import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppConfig {
    public static final AppConfig INSTANCE = new AppConfig();
    private final Map<String, String> settings = new ConcurrentHashMap<>();

    public void set(String key, String value) { settings.put(key, value); }
    public String get(String key)             { return settings.get(key); }
}
