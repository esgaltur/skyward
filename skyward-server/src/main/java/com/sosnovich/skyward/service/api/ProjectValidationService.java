package com.sosnovich.skyward.service.api;

public interface ProjectValidationService {

    boolean isProjectExist(String projectName);

    boolean isProjectAssignedToUser(String projectId, Long id);

    void validateProjectDoesNotExist(String projectId);
}
