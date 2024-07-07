package com.sosnovich.skyward.service.api;

import com.sosnovich.skyward.dto.NewExternalProjectDTO;
import com.sosnovich.skyward.dto.NewUserDTO;
import com.sosnovich.skyward.dto.UpdateUserDTO;
import com.sosnovich.skyward.openapi.model.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for managing users and their associated projects.
 */
public interface UserService {

    /**
     * Creates a new user asynchronously.
     *
     * @param newUser the information of the new user to be created
     * @return a CompletableFuture containing the created User
     */
    CompletableFuture<User> createUser(NewUserDTO newUser);

    /**
     * Retrieves a user by their ID asynchronously.
     *
     * @param id the ID of the user to be retrieved
     * @return a CompletableFuture containing an Optional with the User if found, or empty if not found
     */
    CompletableFuture<Optional<User>> getUserById(Long id);

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    void deleteUser(Long id);

    /**
     * Adds a new project to a user asynchronously.
     *
     * @param userId the ID of the user to whom the project will be added
     * @param newProject the information of the new project to be added
     * @return a CompletableFuture containing the added ExternalProject
     */
    CompletableFuture<ExternalProject> addProjectToUser(Long userId, NewExternalProjectDTO newProject);

    /**
     * Retrieves all projects associated with a user by their ID asynchronously.
     *
     * @param userId the ID of the user whose projects are to be retrieved
     * @return a CompletableFuture containing a list of ExternalProject associated with the user
     */
    CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId);


    CompletableFuture<Boolean> updateUser(Long userId, UpdateUserDTO updatedUser);
}
