package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.service.api.ProjectValidationService;
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
     * Checks if a project with the specified ID is assigned to a user with the specified ID.
     *
     * @param projectId the ID of the project to check
     * @param userId    the ID of the user to check
     * @return true if the project is assigned to the user, false otherwise
     */
    @Override
    public boolean isProjectAssignedToUser(Long userId,String projectId ) {
        return userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId);
    }

}
