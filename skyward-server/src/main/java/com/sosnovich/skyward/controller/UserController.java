package com.sosnovich.skyward.controller;

import com.sosnovich.skyward.exc.EmailAlreadyInUseException;
import com.sosnovich.skyward.exc.MultithreadingException;
import com.sosnovich.skyward.openapi.api.UsersApi;
import com.sosnovich.skyward.openapi.model.*;
import com.sosnovich.skyward.service.api.UserProjectService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * UserController handles requests related to user management and external projects.
 */
@Component
public class UserController implements UsersApi {

    private final UserProjectService userProjectService;

    /**
     * Constructs a UserController with the specified UserService.
     *
     * @param userProjectService the service for managing users and their projects
     */
    @Autowired
    public UserController(com.sosnovich.skyward.service.api.UserProjectService userProjectService) {

        this.userProjectService = userProjectService;
    }

    /**
     * Adds an external project to a user.
     *
     * @param id                 the user ID
     * @param newExternalProject the new external project to add
     * @return a Response with the created project or an error status
     */
    @PreAuthorize("hasRole('USER')")
    @Override
    public Response addExternalProject(Long id, NewExternalProject newExternalProject) {
        try {
            CompletableFuture<ExternalProject> createdProject = userProjectService.addProjectToUser(id, newExternalProject);
            var completedProject = createdProject.get();
            return Response.status(Response.Status.CREATED).entity(completedProject).build();
        } catch (ExecutionException | InterruptedException e) {
            // Handle exceptions and re-interrupt the current thread
            Thread.currentThread().interrupt();
            throw new MultithreadingException("Exception occurred while adding external project", e);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Creates a new user.
     *
     * @param newUser the new user to create
     * @return a Response with the created user or an error status
     */
    @PreAuthorize("hasRole('USER')")
    @Override
    public Response createUser(NewUser newUser) {
        try {
            CompletableFuture<User> createdUser = userProjectService.createUser(newUser);
            var completedUser = createdUser.get();
            return Response.status(Response.Status.CREATED).entity(completedUser).build();
        } catch (InterruptedException e) {
            // Re-interrupt the current thread and throw MultithreadingException
            Thread.currentThread().interrupt();
            throw new MultithreadingException("Exception occurred while creating the new user", e);
        } catch (ExecutionException e) {
            // Unwrap the cause and throw the original exception
            Throwable cause = e.getCause();
            if (cause instanceof EmailAlreadyInUseException emailAlreadyInUseException) {
                throw emailAlreadyInUseException;
            } else {
                throw new MultithreadingException("Exception occurred while creating the new user", cause);
            }
        }
    }

    /**
     * Deletes a user by ID.
     *
     * @param id the user ID
     * @return a Response indicating success or failure
     */
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public Response deleteUser(Long id) {
        try {
            userProjectService.deleteUser(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Retrieves external projects for a user.
     *
     * @param id the user ID
     * @return a Response with the list of external projects or an error status
     */
    @PreAuthorize("hasRole('USER')")
    @Override
    public Response getExternalProjects(Long id) {
        try {
            CompletableFuture<List<ExternalProject>> projects = userProjectService.getProjectsByUserId(id);
            var completedExternalProjects = projects.get();
            return Response.status(Response.Status.OK).entity(completedExternalProjects).build();
        } catch (InterruptedException e) {
            // Re-interrupt the current thread and throw MultithreadingException
            Thread.currentThread().interrupt();
            throw new MultithreadingException("Exception occurred while getting the project by id", e);
        } catch (ExecutionException e) {
            // Handle exceptions and re-interrupt the current thread
            // Unwrap the cause and throw the original exception
            Throwable cause = e.getCause();
            if (cause instanceof UsernameNotFoundException usernameNotFoundException) {
                throw usernameNotFoundException;
            } else {
                throw new MultithreadingException("Exception occurred while retrieving external projects", e);
            }

        }
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id the user ID
     * @return a Response with the user or an error status
     */
    @PreAuthorize("hasRole('USER')")
    @Override
    public Response getUserById(Long id) {
        try {
            CompletableFuture<Optional<User>> user = userProjectService.getUserById(id);
            var completedUser = user.get();
            return completedUser.map(value -> Response.status(Response.Status.OK)
                            .entity(value).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND)
                            .entity(new GetUserById404Response("User not found with ID: " + id))
                            .build());
        } catch (ExecutionException | InterruptedException e) {
            // Handle exceptions and re-interrupt the current thread
            Thread.currentThread().interrupt();
            throw new MultithreadingException("Exception occurred while retrieving user by ID", e);
        }
    }
}
