package com.sosnovich.skyward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for User information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The email address of the user.
     */
    private String email;

    /**
     * The name of the user.
     */
    private String name;
}
