package uz.itpu.ajp.isolated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImpl} using Mockito mocks.
 * Tests verify the service behavior in isolation from its dependencies.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserServiceImpl Tests")
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceValidator userServiceValidator;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private User testUser;

    private static final Long EXPECTED_USER_ID = 1L; // stub value for expected user ID after save
    private static final String TEST_NAME = "John"; // stub value for user name
    private static final String TEST_SURNAME = "Doe"; // stub value for user surname
    private static final String TEST_EMAIL = "john.doe@example.com"; // stub value for user email

    @BeforeEach
    void setUp() {
        testUser = new User(TEST_NAME, TEST_SURNAME, TEST_EMAIL); // initialize fake user with stub values
    }

    @Test
    @DisplayName("Should save user successfully when validation passes")
    void testSaveUser_positive_whenValidationPasses() {
        // Arrange
        doNothing().when(userServiceValidator).validate(testUser);
        when(userRepository.save(testUser)).thenReturn(EXPECTED_USER_ID);

        // Act
        Long actualUserId = userService.saveUser(testUser);

        // Assert
        assertNotNull(actualUserId, "User ID should not be null");
        assertEquals(EXPECTED_USER_ID, actualUserId, "Returned user ID should match expected value");
        verify(userServiceValidator, times(1)).validate(testUser);
        verify(userRepository, times(1)).save(testUser);

        var inOrder = inOrder(userServiceValidator, userRepository);
        inOrder.verify(userServiceValidator).validate(testUser);
        inOrder.verify(userRepository).save(testUser);
    }

    @Test
    @DisplayName("Should not call repository when validation fails")
    void testSaveUser_negative_notCallingRepositoryWhenValidationFails() {
        // Arrange
        String errorMessage = "Invalid user data";
        doThrow(new IllegalArgumentException(errorMessage))
            .when(userServiceValidator).validate(testUser);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.saveUser(testUser),
            "Should throw IllegalArgumentException when validation fails"
        );

        assertEquals(errorMessage, exception.getMessage());
        verify(userServiceValidator, times(1)).validate(testUser);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should propagate repository exception")
    void testSaveUser_negative_propagateRepositoryException() {
        // Arrange
        String errorMessage = "Database connection failed";
        doNothing().when(userServiceValidator).validate(testUser);
        when(userRepository.save(testUser))
            .thenThrow(new RuntimeException(errorMessage));

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.saveUser(testUser),
            "Should propagate repository exception"
        );

        assertEquals(errorMessage, exception.getMessage());
        verify(userServiceValidator, times(1)).validate(testUser);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Should handle null user validation")
    void testSaveUser_negative_nullUserPassed() {
        // Arrange
        User nullUser = null;
        doThrow(new IllegalArgumentException("User cannot be null"))
            .when(userServiceValidator).validate(nullUser);

        // Act & Assert
        assertThrows(
            IllegalArgumentException.class,
            () -> userService.saveUser(nullUser),
            "Should throw exception when user is null"
        );

        verify(userServiceValidator, times(1)).validate(nullUser);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should save multiple users with different IDs")
    void testSaveUser_positive_saveMultipleUsersWithDifferentIds() {
        // Arrange
        User user1 = new User("Alice", "Smith", "alice.smith@example.com");
        User user2 = new User("Bob", "Johnson", "bob.johnson@example.com");

        when(userRepository.save(user1)).thenReturn(1L);
        when(userRepository.save(user2)).thenReturn(2L);

        // Act
        Long userId1 = userService.saveUser(user1);
        Long userId2 = userService.saveUser(user2);

        // Assert
        assertEquals(1L, userId1);
        assertEquals(2L, userId2);
        verify(userServiceValidator, times(1)).validate(user1);
        verify(userServiceValidator, times(1)).validate(user2);
        verify(userRepository, times(1)).save(user1);
        verify(userRepository, times(1)).save(user2);
    }

    @Test
    @DisplayName("Should verify validator is called with correct user object")
    void testSaveUser_positive_verifyValidatorIsCalledWithCorrectUserObject() {
        // Arrange
        when(userRepository.save(testUser)).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(userServiceValidator).validate(argThat(user ->
            user != null &&
            TEST_NAME.equals(user.getName()) &&
            TEST_SURNAME.equals(user.getSurname()) &&
            TEST_EMAIL.equals(user.getEmail())
        ));
    }

    @Test
    @DisplayName("Should verify repository is called with correct user object")
    void testSaveUser_positive_verifyRepositoryIsCalledWithCorrectUserObject() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(userRepository).save(argThat(user ->
            user != null &&
            TEST_NAME.equals(user.getName()) &&
            TEST_SURNAME.equals(user.getSurname()) &&
            TEST_EMAIL.equals(user.getEmail())
        ));
    }

    // ==================== ArgumentCaptor Tests ====================

    @Test
    @DisplayName("Should capture user passed using ArgumentCaptor")
    void testSaveUser_positive_captureArgument() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert - Capture the argument passed
        verify(userServiceValidator).validate(userCaptor.capture());
        User capturedUser1 = userCaptor.getValue();

        verify(userRepository).save(userCaptor.capture());
        User capturedUser2 = userCaptor.getValue();

        // same object should be passed to both validator and repository
        assertEquals(capturedUser1, capturedUser2);

        // test that captured user has expected properties
        User capturedUser = userCaptor.getValue();
        assertNotNull(capturedUser, "Captured user should not be null");
        assertEquals(TEST_NAME, capturedUser.getName(), "Captured user name should match");
        assertEquals(TEST_SURNAME, capturedUser.getSurname(), "Captured user surname should match");
        assertEquals(TEST_EMAIL, capturedUser.getEmail(), "Captured user email should match");
    }

    @Test
    @DisplayName("Should capture all users when saving multiple times")
    void testSaveUser_positive_argumentCaptorCaptureMultipleArguments() {
        // Arrange
        User user1 = new User("Alice", "Smith", "alice@example.com");
        User user2 = new User("Bob", "Johnson", "bob@example.com");
        User user3 = new User("Charlie", "Brown", "charlie@example.com");

        when(userRepository.save(any(User.class))).thenReturn(1L, 2L, 3L);

        // Act
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        // Assert - Capture all arguments
        verify(userRepository, times(3)).save(userCaptor.capture());

        var allCapturedUsers = userCaptor.getAllValues();
        assertEquals(3, allCapturedUsers.size(), "Should capture 3 users");

        assertEquals("Alice", allCapturedUsers.get(0).getName());
        assertEquals("Bob", allCapturedUsers.get(1).getName());
        assertEquals("Charlie", allCapturedUsers.get(2).getName());
    }

    // ==================== Argument Matcher Tests ====================

    @Test
    @DisplayName("Should accept any User object using any() matcher")
    void testSaveUser_positive_matcher() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(EXPECTED_USER_ID);

        // Act
        Long userId = userService.saveUser(testUser);

        // Assert
        assertEquals(EXPECTED_USER_ID, userId);
        verify(userRepository).save(any(User.class));
        verify(userServiceValidator).validate(any(User.class));
    }

    @Test
    @DisplayName("Should verify using isNull() matcher")
    void testSaveUser_negative_argumentMatcherIsNull() {
        // Arrange
        doThrow(new IllegalArgumentException("User cannot be null"))
            .when(userServiceValidator).validate(isNull());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(null));
        verify(userServiceValidator).validate(isNull());
    }

    @Test
    @DisplayName("Should verify using notNull() matcher")
    void testSaveUser_positive_argumentMatcherNotNull() {
        // Arrange
        when(userRepository.save(notNull())).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(userRepository).save(notNull());
        verify(userServiceValidator).validate(notNull());
    }

    @Test
    @DisplayName("Should verify using eq() matcher for exact match")
    void testSaveUser_positive_exactMatch() {
        // Arrange
        when(userRepository.save(eq(testUser))).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(userRepository).save(eq(testUser));
        verify(userServiceValidator).validate(eq(testUser));
    }

    @Test
    @DisplayName("Should verify using same() matcher for object identity")
    void testSaveUser_posotive_sameObject() {
        // Arrange
        when(userRepository.save(same(testUser))).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert
        verify(userRepository).save(same(testUser));
        verify(userServiceValidator).validate(same(testUser));
    }

    @Test
    @DisplayName("Should verify using custom argThat() matcher with complex conditions")
    void testSaveUser_positive_customMatcher() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert - Custom matcher with multiple conditions
        verify(userServiceValidator).validate(argThat(user ->
            user != null &&
            user.getName() != null && user.getName().length() > 0 &&
            user.getSurname() != null && user.getSurname().length() > 0 &&
            user.getEmail() != null && user.getEmail().contains("@")
        ));
    }

    @Test
    @DisplayName("Should combine ArgumentCaptor with assertions for detailed verification")
    void testSaveUser_positive_detailedVerification() {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(EXPECTED_USER_ID);

        // Act
        userService.saveUser(testUser);

        // Assert - Capture and perform detailed checks
        verify(userRepository).save(userCaptor.capture());

        User captured = userCaptor.getValue();
        assertAll("Captured user validation",
            () -> assertNotNull(captured, "User should not be null"),
            () -> assertEquals(TEST_NAME, captured.getName(), "Name should match"),
            () -> assertEquals(TEST_SURNAME, captured.getSurname(), "Surname should match"),
            () -> assertEquals(TEST_EMAIL, captured.getEmail(), "Email should match"),
            () -> assertTrue(captured.getEmail().contains("@"), "Email should contain @")
        );
    }

    @Test
    @DisplayName("Should verify argument order and values with multiple calls")
    void testSaveUser_positive_argumentCaptorVerifyOrderAndValues() {
        // Arrange
        User firstUser = new User("First", "User", "first@example.com");
        User secondUser = new User("Second", "User", "second@example.com");

        when(userRepository.save(any(User.class))).thenReturn(1L, 2L);

        // Act
        userService.saveUser(firstUser);
        userService.saveUser(secondUser);

        // Assert - Capture all and verify order
        verify(userRepository, times(2)).save(userCaptor.capture());

        var capturedUsers = userCaptor.getAllValues();
        assertEquals(2, capturedUsers.size());
        assertEquals("First", capturedUsers.get(0).getName(), "First call should be 'First' user");
        assertEquals("Second", capturedUsers.get(1).getName(), "Second call should be 'Second' user");
    }
}


