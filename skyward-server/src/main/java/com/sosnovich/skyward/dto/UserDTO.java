package com.sosnovich.skyward.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object for User information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class UserDTO {

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The email address of the user.
     */
    @Email
    private String email;

    /**
     * The name of the user.
     */
    private String name;
}
