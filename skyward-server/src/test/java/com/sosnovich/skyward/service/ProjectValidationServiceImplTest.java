package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.exc.ProjectAlreadyExistsException;
import com.sosnovich.skyward.service.errors.ServiceError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ProjectValidationServiceImplTest {

    @Mock
    private UserExternalProjectRepository userExternalProjectRepository;

    @InjectMocks
    private ProjectValidationServiceImpl projectValidationService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsProjectExist_ProjectExists() {
        String projectId = "existingProjectId";
        when(userExternalProjectRepository.existsByProjectId(projectId)).thenReturn(true);

        boolean result = projectValidationService.isProjectExist(projectId);

        assertTrue(result, "Project should exist");
    }

    @Test
    void testIsProjectExist_ProjectDoesNotExist() {
        String projectId = "nonExistingProjectId";
        when(userExternalProjectRepository.existsByProjectId(projectId)).thenReturn(false);

        boolean result = projectValidationService.isProjectExist(projectId);

        assertFalse(result, "Project should not exist");
    }

    @Test
     void testIsProjectAssignedToUser_ProjectAssignedToUser() {
        String projectId = "projectId";
        Long userId = 1L;
        when(userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId)).thenReturn(true);

        boolean result = projectValidationService.isProjectAssignedToUser(projectId, userId);

        assertTrue(result, "Project should be assigned to user");
    }

    @Test
     void testIsProjectAssignedToUser_ProjectNotAssignedToUser() {
        String projectId = "projectId";
        Long userId = 1L;
        when(userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId)).thenReturn(false);

        boolean result = projectValidationService.isProjectAssignedToUser(projectId, userId);

        assertFalse(result, "Project should not be assigned to user");
    }

    @Test
     void testValidateProjectDoesNotExist_ProjectExists() {
        String projectId = "existingProjectId";
        when(userExternalProjectRepository.existsByProjectId(projectId)).thenReturn(true);

        ProjectAlreadyExistsException exception = assertThrows(ProjectAlreadyExistsException.class, () -> {
            projectValidationService.validateProjectDoesNotExist(projectId);
        });

        assertEquals(ServiceError.PROJECT_ALREADY_EXISTS.format(projectId), exception.getMessage());
    }

    @Test
     void testValidateProjectDoesNotExist_ProjectDoesNotExist() {
        String projectId = "nonExistingProjectId";
        when(userExternalProjectRepository.existsByProjectId(projectId)).thenReturn(false);

        assertDoesNotThrow(() -> projectValidationService.validateProjectDoesNotExist(projectId));
    }
}
