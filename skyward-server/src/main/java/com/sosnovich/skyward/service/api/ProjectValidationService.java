package com.sosnovich.skyward.service.api;

import com.sosnovich.skyward.exc.ProjectAlreadyExistsException;

/**
 * Interface for validating project-related operations.
 */
public interface ProjectValidationService {

    /**
     * Checks if a project with the specified name exists.
     *
     * @param projectName the name of the project to check
     * @return true if the project exists, false otherwise
     */
    boolean isProjectExist(String projectName);

    /**
     * Checks if a project with the specified ID is assigned to a user with the specified ID.
     *
     * @param projectId the ID of the project to check
     * @param id        the ID of the user to check
     * @return true if the project is assigned to the user, false otherwise
     */
    boolean isProjectAssignedToUser(String projectId, Long id);

    /**
     * Validates that a project with the specified ID does not already exist.
     *
     * @param projectId the ID of the project to check
     * @throws ProjectAlreadyExistsException if the project already exists
     */
    void validateProjectDoesNotExist(String projectId);
}
