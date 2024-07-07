package com.sosnovich.skyward.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@Data
@Validated
public class UpdateUserDTO {

    /**
     * The email of the user.
     * This field is mandatory and has a maximum length of 200 characters.
     */
    @Valid
    @NotBlank
    @Email
    private String email;

    /**
     * The password of the user.
     * This field is mandatory and has a maximum length of 129 characters.
     */

    private String password;

    /**
     * The name of the user.
     * This field is optional and has a maximum length of 120 characters.
     */
    private String name;

    /**
     * The role of the user.
     * This field is mandatory and has a maximum length of 50 characters.
     */

    private String role;

    /**
     * Indicates whether the user's account is expired.
     */

    private boolean accountExpired;

    /**
     * Indicates whether the user's account is locked.
     */

    private boolean accountLocked;

    /**
     * Indicates whether the user's credentials are expired.
     */

    private boolean credentialsExpired;

    /**
     * Indicates whether the user's account is disabled.
     */

    private boolean disabled;
}
