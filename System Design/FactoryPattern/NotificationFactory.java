public class NotificationFactory {

public Notification createNotification(NotificationType type) {
        if (type == NotificationType.SMS) {
            return new SMSNotification();
        } else if (type == NotificationType.EMAIL) {
            return new EmailNotification();
        }
        return null;
    }
}

enum NotificationType {
    SMS, EMAIL
}