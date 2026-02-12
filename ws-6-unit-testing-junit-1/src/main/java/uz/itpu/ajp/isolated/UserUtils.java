package uz.itpu.ajp.isolated;

/**
 * Utility class with static methods for user operations.
 * Used to demonstrate static method mocking.
 */
public class UserUtils {

    /**
     * Validates email format.
     *
     * @param email email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    /**
     * Generates a unique user ID based on timestamp.
     *
     * @return generated user ID
     */
    public static Long generateUserId() {
        return System.currentTimeMillis();
    }

    /**
     * Formats user full name.
     *
     * @param name    first name
     * @param surname last name
     * @return formatted full name
     */
    public static String formatFullName(String name, String surname) {
        return name + " " + surname;
    }

    /**
     * Checks if user is adult based on age.
     *
     * @param age user's age
     * @return true if age >= 18
     */
    public static boolean isAdult(int age) {
        return age >= 18;
    }
}

