package com.sosnovich.skyward.service.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

public interface UserValidationService {
    void validateUserExists(Long userId);

    void validateEmailNotInUse(@Valid @Email String email);
}
