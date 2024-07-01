package com.sosnovich.skyward.controller;

import com.sosnovich.skyward.openapi.model.*;
import com.sosnovich.skyward.service.api.UserProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UserControllerTest {

    @Mock
    private UserProjectService userProjectService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testAddExternalProject_Success() {
        Long userId = 1L;
        NewExternalProject newProject = new NewExternalProject();
        newProject.setId("project1");
        newProject.setName("Project One");

        ExternalProject createdProject = new ExternalProject();
        createdProject.setId("project1");
        createdProject.setUserId(userId);
        createdProject.setName("Project One");

        when(userProjectService.addProjectToUser(userId, newProject)).thenReturn(CompletableFuture.completedFuture(createdProject));

        Response response = userController.addExternalProject(userId, newProject);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(createdProject, response.getEntity());
    }

    @Test
     void testCreateUser_Success() {
        NewUser newUser = new NewUser();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setName("Test User");

        User createdUser = new User();
        createdUser.setId(1L);
        createdUser.setEmail("test@example.com");
        createdUser.setName("Test User");

        when(userProjectService.createUser(newUser)).thenReturn(CompletableFuture.completedFuture(createdUser));

        Response response = userController.createUser(newUser);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertEquals(createdUser, response.getEntity());
    }

     @Test
     void testDeleteUser_Success() {
        Long userId = 1L;

        doNothing().when(userProjectService).deleteUser(userId);

        Response response = userController.deleteUser(userId);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
     void testDeleteUser_NotFound() {
        Long userId = 1L;

        doThrow(new RuntimeException()).when(userProjectService).deleteUser(userId);

        Response response = userController.deleteUser(userId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
     void testGetExternalProjects_Success() {
        Long userId = 1L;
        ExternalProject externalProject = new ExternalProject();
        externalProject.setId("project1");
        externalProject.setUserId(userId);
        externalProject.setName("Project One");

        when(userProjectService.getProjectsByUserId(userId)).thenReturn(CompletableFuture.completedFuture(List.of(externalProject)));

        Response response = userController.getExternalProjects(userId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(List.of(externalProject), response.getEntity());
    }

    @Test
     void testGetUserById_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userProjectService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(Optional.of(user)));

        Response response = userController.getUserById(userId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(user, response.getEntity());
    }

    @Test
     void testGetUserById_NotFound() {
        Long userId = 1L;

        when(userProjectService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        Response response = userController.getUserById(userId);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity() instanceof GetUserById404Response);
        assertEquals("User not found with ID: " + userId, ((GetUserById404Response) response.getEntity()).getError());
    }
}
