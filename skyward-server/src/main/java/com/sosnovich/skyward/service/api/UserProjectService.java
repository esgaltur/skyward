package com.sosnovich.skyward.service.api;

import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Interface for managing users and their associated projects.
 */
public interface UserProjectService {

    /**
     * Creates a new user.
     *
     * @param newUser the new user to be created
     * @return a CompletableFuture containing the created user
     */
    CompletableFuture<User> createUser(NewUser newUser);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a CompletableFuture containing an Optional with the retrieved user, if found
     */
    CompletableFuture<Optional<User>> getUserById(Long id);

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    void deleteUser(Long userId);

    /**
     * Adds a new external project to a user.
     *
     * @param userId     the ID of the user to add the project to
     * @param newProject the new external project to add
     * @return a CompletableFuture containing the added external project
     */
    CompletableFuture<ExternalProject> addProjectToUser(Long userId, NewExternalProject newProject);

    /**
     * Retrieves all external projects associated with a user.
     *
     * @param userId the ID of the user to retrieve projects for
     * @return a CompletableFuture containing a list of external projects associated with the user
     */
    CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId);
}
