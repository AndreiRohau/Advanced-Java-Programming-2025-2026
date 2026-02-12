package uz.itpu.ajp.isolated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NotificationService} demonstrating advanced Mockito features.
 * Tests cover mocking of final methods, static methods, and final classes.
 *
 * Note: Requires mockito-inline dependency to mock final classes and static methods.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Advanced Mocking Tests")
class NotificationServiceTest {

    private NotificationService notificationService;
    private User testUser;

    private static final String TEST_USER_NAME = "Alice Smith";
    private static final String TEST_USER_SURNAME = "Smith";
    private static final String TEST_USER_EMAIL = "alice.smith@example.com";
    private static final String TEST_MESSAGE = "Welcome to our platform!";
    private static final String EMPTY_MESSAGE = "";
    private static final String NULL_MESSAGE = null;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
        testUser = new User(TEST_USER_NAME, TEST_USER_SURNAME, TEST_USER_EMAIL);
    }

    @Test
    @DisplayName("Should mock final method sendNotification in final class NotificationService")
    void shouldMockFinalMethodInFinalClass() {
        // Arrange - Mock the final NotificationService class and its final sendNotification method
        NotificationService mockNotificationService = mock(NotificationService.class);
        when(mockNotificationService.sendNotification(any(User.class), anyString()))
            .thenReturn(false); // Override default behavior to return false

        // Act
        boolean result = mockNotificationService.sendNotification(testUser, TEST_MESSAGE);

        // Assert
        assertFalse(result, "Mocked final method should return false as configured");
        verify(mockNotificationService, times(1))
            .sendNotification(testUser, TEST_MESSAGE);

        // Demonstrate that we can verify interactions with mocked final method
        verify(mockNotificationService).sendNotification(
            argThat(user -> user.getName().equals(TEST_USER_NAME)),
            eq(TEST_MESSAGE)
        );
    }

    @Test
    @DisplayName("Should mock static method isValidMessage using MockedStatic")
    void shouldMockStaticMethodIsValidMessage() {
        // Arrange - Mock the static method isValidMessage
        try (MockedStatic<NotificationService> mockedStatic = mockStatic(NotificationService.class)) {
            // Configure the static method mock to return false for valid messages
            mockedStatic.when(() -> NotificationService.isValidMessage(TEST_MESSAGE))
                .thenReturn(false);

            // Keep default behavior for other inputs
            mockedStatic.when(() -> NotificationService.isValidMessage(EMPTY_MESSAGE))
                .thenCallRealMethod();

            // Act
            boolean validMessageResult = NotificationService.isValidMessage(TEST_MESSAGE);
            boolean emptyMessageResult = NotificationService.isValidMessage(EMPTY_MESSAGE);

            // Assert
            assertFalse(validMessageResult,
                "Mocked static method should return false for valid message");
            assertFalse(emptyMessageResult,
                "Real static method should return false for empty message");

            // Verify static method was called
            mockedStatic.verify(() -> NotificationService.isValidMessage(TEST_MESSAGE), times(1));
            mockedStatic.verify(() -> NotificationService.isValidMessage(EMPTY_MESSAGE), times(1));
        }

        // After try-with-resources block, static mock is automatically closed
        // and original behavior is restored
        assertTrue(NotificationService.isValidMessage(TEST_MESSAGE),
            "Static method should return to original behavior after mock is closed");
    }

    @Test
    @DisplayName("Should verify final class mock with spy to allow partial mocking")
    void shouldVerifyFinalClassMockWithSpy() {
        // Arrange - Create a spy of the final NotificationService class
        // Spy allows us to mock some methods while keeping real implementation for others
        NotificationService spyNotificationService = spy(NotificationService.class);

        // Mock only the sendNotification method, keep logNotification real
        // Use doReturn().when() syntax to avoid calling the real method during stubbing
        doReturn(false).when(spyNotificationService).sendNotification(any(User.class), anyString());

        // Don't mock logNotification - it will use real implementation

        // Act
        boolean sendResult = spyNotificationService.sendNotification(testUser, TEST_MESSAGE);
        spyNotificationService.logNotification(TEST_MESSAGE); // This calls real method

        // Assert
        assertFalse(sendResult, "Spied method sendNotification should return mocked value");

        // Verify interactions
        verify(spyNotificationService, times(1))
            .sendNotification(testUser, TEST_MESSAGE);
        verify(spyNotificationService, times(1))
            .logNotification(TEST_MESSAGE);

        // Verify no more interactions
        verifyNoMoreInteractions(spyNotificationService);
    }

    @Test
    @DisplayName("Should demonstrate static method mocking with different return values")
    void shouldDemonstrateStaticMethodMockingWithDifferentValues() {
        // Arrange
        try (MockedStatic<NotificationService> mockedStatic = mockStatic(NotificationService.class)) {
            // Configure static method to return different values for different inputs
            mockedStatic.when(() -> NotificationService.isValidMessage(anyString()))
                .thenAnswer(invocation -> {
                    String message = invocation.getArgument(0);
                    // Custom logic: return true only for messages containing "urgent"
                    return message != null && message.toLowerCase().contains("urgent");
                });

            // Act
            boolean urgentResult = NotificationService.isValidMessage("Urgent: System alert");
            boolean normalResult = NotificationService.isValidMessage("Normal message");
            boolean nullResult = NotificationService.isValidMessage(NULL_MESSAGE);

            // Assert
            assertTrue(urgentResult, "Should return true for message containing 'urgent'");
            assertFalse(normalResult, "Should return false for normal message");
            assertFalse(nullResult, "Should return false for null message");

            // Verify all calls
            mockedStatic.verify(() -> NotificationService.isValidMessage("Urgent: System alert"));
            mockedStatic.verify(() -> NotificationService.isValidMessage("Normal message"));
            mockedStatic.verify(() -> NotificationService.isValidMessage(NULL_MESSAGE));
        }
    }

    @Test
    @DisplayName("Should test real NotificationService behavior without mocking")
    void shouldTestRealNotificationServiceBehavior() {
        // Arrange - Use real implementation to verify actual behavior
        String validMessage = "This is a valid message";
        String invalidEmptyMessage = "   ";

        // Act
        boolean sendResult = notificationService.sendNotification(testUser, validMessage);
        boolean validCheck = NotificationService.isValidMessage(validMessage);
        boolean invalidCheck = NotificationService.isValidMessage(invalidEmptyMessage);

        // Assert
        assertTrue(sendResult, "Real sendNotification should return true");
        assertTrue(validCheck, "Real isValidMessage should return true for valid message");
        assertFalse(invalidCheck, "Real isValidMessage should return false for empty/whitespace message");
    }
}

