public class SingletonPattern {
     public static void main(String[] args) {
        AppConfig.INSTANCE.set("env", "prod");
        System.out.println(AppConfig.INSTANCE.get("env")); // prod
    }
}
