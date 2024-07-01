package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.exc.ProjectAlreadyExistsException;
import com.sosnovich.skyward.service.api.ProjectValidationService;
import com.sosnovich.skyward.service.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service implementation for validating project-related operations.
 */
@Service
public class ProjectValidationServiceImpl implements ProjectValidationService {

    private final UserExternalProjectRepository userExternalProjectRepository;

    /**
     * Constructs a new ProjectValidationServiceImpl.
     *
     * @param userExternalProjectRepository the repository for user external projects
     */
    @Autowired
    public ProjectValidationServiceImpl(UserExternalProjectRepository userExternalProjectRepository) {
        this.userExternalProjectRepository = userExternalProjectRepository;
    }

    /**
     * Checks if a project with the specified ID exists.
     *
     * @param projectId the ID of the project to check
     * @return true if the project exists, false otherwise
     */
    @Override
    public boolean isProjectExist(String projectId) {
        return userExternalProjectRepository.existsByProjectId(projectId);
    }

    /**
     * Checks if a project with the specified ID is assigned to a user with the specified ID.
     *
     * @param projectId the ID of the project to check
     * @param userId    the ID of the user to check
     * @return true if the project is assigned to the user, false otherwise
     */
    @Override
    public boolean isProjectAssignedToUser(String projectId, Long userId) {
        return userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId);
    }

    /**
     * Validates that a project with the specified ID does not already exist.
     *
     * @param projectId the ID of the project to check
     * @throws ProjectAlreadyExistsException if the project already exists
     */
    public void validateProjectDoesNotExist(String projectId) {
        if (userExternalProjectRepository.existsByProjectId(projectId)) {
            throw new ProjectAlreadyExistsException(ServiceError.PROJECT_ALREADY_EXISTS.format(projectId));
        }
    }
}
