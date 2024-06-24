package com.sosnovich.skyward.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for External Projects.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExternalProjectDTO {

    /**
     * The unique identifier of the external project.
     */
    private String projectId;

    /**
     * The ID of the user to whom the external project belongs.
     */
    private Long userId;

    /**
     * The name of the external project.
     */
    private String name;
}
