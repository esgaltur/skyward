package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserRepository;
import com.sosnovich.skyward.exc.EmailAlreadyInUseException;
import com.sosnovich.skyward.exc.UserNotFoundException;
import com.sosnovich.skyward.service.api.UserValidationService;
import com.sosnovich.skyward.service.errors.ServiceError;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * Service implementation for validating user-related operations.
 */
@Service
@Validated
public class UserValidationServiceImpl implements UserValidationService {

    private final UserRepository userRepository;

    /**
     * Constructs a new UserValidationServiceImpl.
     *
     * @param userRepository the repository for user entities
     */
    @Autowired
    public UserValidationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Validates that a user with the specified ID exists.
     *
     * @param userId the ID of the user to check
     * @throws UserNotFoundException if the user does not exist
     */
    @Override
    public void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(ServiceError.USER_NOT_FOUND.format(userId));
        }
    }

    /**
     * Validates that the email is not already in use by another user.
     *
     * @param email the email to check
     * @throws EmailAlreadyInUseException if the email is already in use
     */
    @Override
    public void validateEmailNotInUse(@Valid @Email String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException(ServiceError.EMAIL_ALREADY_IN_USE.format(email));
        }
    }
}
