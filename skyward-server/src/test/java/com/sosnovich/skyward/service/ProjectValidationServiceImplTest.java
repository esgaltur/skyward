package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
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
     void testIsProjectAssignedToUser_ProjectAssignedToUser() {
        String projectId = "projectId";
        Long userId = 1L;
        when(userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId)).thenReturn(true);

        boolean result = projectValidationService.isProjectAssignedToUser(userId,projectId);

        assertTrue(result, "Project should be assigned to user");
    }

    @Test
     void testIsProjectAssignedToUser_ProjectNotAssignedToUser() {
        String projectId = "projectId";
        Long userId = 1L;
        when(userExternalProjectRepository.existsByUser_IdAndProjectId(userId, projectId)).thenReturn(false);

        boolean result = projectValidationService.isProjectAssignedToUser(userId,projectId );

        assertFalse(result, "Project should not be assigned to user");
    }

}
