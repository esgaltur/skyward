package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserRepository;
import com.sosnovich.skyward.exc.EmailAlreadyInUseException;
import com.sosnovich.skyward.exc.UserNotFoundException;
import com.sosnovich.skyward.service.errors.ServiceError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserValidationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidationServiceImpl userValidationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateUserExists_UserExists() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> userValidationService.validateUserExists(userId));
    }

    @Test
    public void testValidateUserExists_UserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.existsById(userId)).thenReturn(false);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userValidationService.validateUserExists(userId);
        });

        assertEquals(ServiceError.USER_NOT_FOUND.format(userId), exception.getMessage());
    }

    @Test
    public void testValidateEmailNotInUse_EmailNotInUse() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> userValidationService.validateEmailNotInUse(email));
    }

    @Test
    public void testValidateEmailNotInUse_EmailInUse() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        EmailAlreadyInUseException exception = assertThrows(EmailAlreadyInUseException.class, () -> {
            userValidationService.validateEmailNotInUse(email);
        });

        assertEquals(ServiceError.EMAIL_ALREADY_IN_USE.format(email), exception.getMessage());
    }
}
