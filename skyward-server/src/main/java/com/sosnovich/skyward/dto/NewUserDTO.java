package com.sosnovich.skyward.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object for creating a new User.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Validated
public class NewUserDTO {

    /**
     * The email address of the new user.
     */
    @Email
    private String email;

    /**
     * The password for the new user's account.
     */
    private String password;

    /**
     * The name of the new user.
     */
    private String name;

    /**
     * The role assigned to the new user. Default is "ROLE_USER".
     */
    @Builder.Default
    private String role = "ROLE_USER";

    /**
     * Indicates if the user's account has expired. Default is false.
     */
    @Builder.Default
    private boolean accountExpired = false;

    /**
     * Indicates if the user's account is locked. Default is false.
     */
    @Builder.Default
    private boolean accountLocked = false;

    /**
     * Indicates if the user's credentials have expired. Default is false.
     */
    @Builder.Default
    private boolean credentialsExpired = false;

    /**
     * Indicates if the user's account is disabled. Default is false.
     */
    @Builder.Default
    private boolean disabled = false;
}
