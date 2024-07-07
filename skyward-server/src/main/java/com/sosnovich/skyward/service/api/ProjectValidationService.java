package com.sosnovich.skyward.service.api;

/**
 * Interface for validating project-related operations.
 */
public interface ProjectValidationService {

    /**
     * Checks if a project with the specified ID is assigned to a user with the specified ID.
     *
     * @param projectId the ID of the project to check
     * @param id        the ID of the user to check
     * @return true if the project is assigned to the user, false otherwise
     */
    boolean isProjectAssignedToUser(Long id,String projectId );
}
