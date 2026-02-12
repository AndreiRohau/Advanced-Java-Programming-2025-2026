package uz.itpu.ajp.isolated;

public final class NotificationService {

    public final boolean sendNotification(User user, String message) {
        System.out.println("Sending notification to: " + user.getName());
        System.out.println("Message: " + message);
        return true;
    }

    public void logNotification(String message) {
        System.out.println("Log: " + message);
    }

    public static boolean isValidMessage(String message) {
        return message != null && !message.trim().isEmpty();
    }
}

