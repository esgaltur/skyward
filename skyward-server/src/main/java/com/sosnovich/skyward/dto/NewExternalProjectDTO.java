package com.sosnovich.skyward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating a new External Project.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewExternalProjectDTO {

    /**
     * The unique identifier of the new external project.
     */
    private String projectId;

    /**
     * The name of the new external project.
     */
    private String name;
}
