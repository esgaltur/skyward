package com.sosnovich.skyward.service;

import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;
import com.sosnovich.skyward.service.api.ProjectValidationService;
import com.sosnovich.skyward.service.api.UserService;
import com.sosnovich.skyward.service.api.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UserProjectServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserValidationService userValidationService;

    @Mock
    private ProjectValidationService projectValidationService;

    @InjectMocks
    private UserProjectServiceImpl userProjectService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateUser() {
        NewUser newUser = new NewUser();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setName("Test User");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userService.createUser(newUser)).thenReturn(CompletableFuture.completedFuture(user));

        CompletableFuture<User> result = userProjectService.createUser(newUser);

        verify(userValidationService).validateEmailNotInUse("test@example.com");
        assertEquals(user, result.join());
    }

    @Test
     void testGetUserById() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(Optional.of(user)));

        CompletableFuture<Optional<User>> result = userProjectService.getUserById(userId);

        assertEquals(Optional.of(user), result.join());
    }

    @Test
     void testDeleteUser() {
        Long userId = 1L;

        doNothing().when(userService).deleteUser(userId);

        userProjectService.deleteUser(userId);

        verify(userValidationService).validateUserExists(userId);
        verify(userService).deleteUser(userId);
    }

    @Test
     void testAddProjectToUser() {
        Long userId = 1L;
        NewExternalProject newProject = new NewExternalProject();
        newProject.setId("projectId");
        newProject.setName("Test Project");

        ExternalProject externalProject = new ExternalProject();
        externalProject.setId("projectId");
        externalProject.setUserId(userId);
        externalProject.setName("Test Project");

        when(userService.addProjectToUser(userId, newProject)).thenReturn(CompletableFuture.completedFuture(externalProject));

        CompletableFuture<ExternalProject> result = userProjectService.addProjectToUser(userId, newProject);

        verify(userValidationService).validateUserExists(userId);
        verify(projectValidationService).validateProjectDoesNotExist("projectId");
        assertEquals(externalProject, result.join());
    }

    @Test
     void testGetProjectsByUserId() {
        Long userId = 1L;
        ExternalProject externalProject = new ExternalProject();
        externalProject.setId("projectId");
        externalProject.setUserId(userId);
        externalProject.setName("Test Project");

        when(userService.getProjectsByUserId(userId)).thenReturn(CompletableFuture.completedFuture(List.of(externalProject)));

        CompletableFuture<List<ExternalProject>> result = userProjectService.getProjectsByUserId(userId);

        assertEquals(1, result.join().size());
        assertEquals("projectId", result.join().get(0).getId());
    }
}
