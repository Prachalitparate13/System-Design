public class FactoryPattern {
  public static void main(String[] args) {
        NotificationFactory notificationFactory = new NotificationFactory();

        // Create an EmailNotification and call its notifyUser method
        Notification notification1 = notificationFactory.createNotification(NotificationType.EMAIL);
        notification1.notifyUser();

        // Create an SMSNotification and call its notifyUser method
        Notification notification2 = notificationFactory.createNotification(NotificationType.SMS);
        notification2.notifyUser();
    }
}
