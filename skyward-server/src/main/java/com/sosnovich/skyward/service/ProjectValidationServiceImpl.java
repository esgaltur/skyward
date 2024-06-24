package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.exc.ProjectAlreadyExistsException;
import com.sosnovich.skyward.service.api.ProjectValidationService;
import com.sosnovich.skyward.service.errors.ServiceError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectValidationServiceImpl implements ProjectValidationService {

    private final UserExternalProjectRepository userExternalProjectRepository;

    @Autowired
    public ProjectValidationServiceImpl(UserExternalProjectRepository userExternalProjectRepository) {
        this.userExternalProjectRepository = userExternalProjectRepository;
    }

    @Override
    public boolean isProjectExist(String projectId) {
        return userExternalProjectRepository.existsByProjectId(projectId);
    }

    @Override
    public boolean isProjectAssignedToUser(String projectId, Long userId) {
        return userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId);
    }

    public void validateProjectDoesNotExist(String projectId) {
        if (userExternalProjectRepository.existsByProjectId(projectId)) {
            throw new ProjectAlreadyExistsException(ServiceError.PROJECT_ALREADY_EXISTS.format(projectId));
        }
    }
}
