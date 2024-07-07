package com.sosnovich.skyward.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

/**
 * Data Transfer Object for creating a new External Project.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Validated
public class NewExternalProjectDTO {

    /**
     * The unique identifier of the new external project.
     */
    @NotBlank
    private String projectId;

    /**
     * The name of the new external project.
     */
    @NotBlank
    private String name;
}
