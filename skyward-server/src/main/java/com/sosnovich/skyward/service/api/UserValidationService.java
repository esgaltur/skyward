package com.sosnovich.skyward.service.api;

import com.sosnovich.skyward.exc.EmailAlreadyInUseException;
import com.sosnovich.skyward.exc.UserNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

/**
 * Interface for validating user-related operations.
 */
public interface UserValidationService {

    /**
     * Validates that a user with the specified ID exists.
     *
     * @param userId the ID of the user to check
     * @throws UserNotFoundException if the user does not exist
     */
    void validateUserExists(Long userId);

    /**
     * Validates that the email is not already in use by another user.
     *
     * @param email the email to check
     * @throws EmailAlreadyInUseException if the email is already in use
     */
    void validateEmailNotInUse(@Valid @Email String email);
}
