package com.sosnovich.skyward.service;

import com.sosnovich.skyward.dto.NewExternalProjectDTO;
import com.sosnovich.skyward.dto.NewUserDTO;
import com.sosnovich.skyward.dto.UpdateUserDTO;
import com.sosnovich.skyward.openapi.model.*;
import com.sosnovich.skyward.service.api.UserProjectService;
import com.sosnovich.skyward.service.api.UserService;
import com.sosnovich.skyward.service.api.UserValidationService;
import com.sosnovich.skyward.service.api.ProjectValidationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

/**
 * Service implementation for handling user and project-related operations.
 */
@Service
@Validated
public class UserProjectServiceImpl implements UserProjectService {

    private final UserService userService;
    private final UserValidationService userValidationService;
    private final ProjectValidationService projectValidationService;
    private final WeakHashMap<Long, User> userCache = new WeakHashMap<>();
    /**
     * Constructs a new UserProjectServiceImpl.
     *
     * @param userService              the user service
     * @param userValidationService    the user validation service
     * @param projectValidationService the project validation service
     */
    @Autowired
    public UserProjectServiceImpl(UserService userService, UserValidationService userValidationService, ProjectValidationService projectValidationService) {
        this.userService = userService;
        this.userValidationService = userValidationService;
        this.projectValidationService = projectValidationService;
    }

    /**
     * Creates a new user.
     *
     * @param newUser the new user to be created
     * @return a CompletableFuture containing the created user
     */
    @Override
    public CompletableFuture<User> createUser(@Valid NewUserDTO newUser) {
        userValidationService.validateEmailNotInUse(newUser.getEmail());
        return userService.createUser(newUser);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a CompletableFuture containing an Optional with the retrieved user, if found
     */
    @Override
    public CompletableFuture<Optional<User>> getUserById(Long id) {
        User cachedUser = userCache.get(id);
        if (cachedUser != null) {
            return CompletableFuture.completedFuture(Optional.of(cachedUser));
        }

        return userService.getUserById(id).thenApply(userOpt -> {
            userOpt.ifPresent(user -> userCache.put(id, user));
            return userOpt;
        });
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    @Override
    public void deleteUser(Long userId) {
        userValidationService.validateUserExists(userId);
        userService.deleteUser(userId);
    }

    /**
     * Adds a new external project to a user.
     *
     * @param userId     the ID of the user to add the project to
     * @param newProject the new external project to add
     * @return a CompletableFuture containing the added external project
     */
    @Override
    public CompletableFuture<ExternalProject> assignProjectToUser(Long userId, @Valid NewExternalProjectDTO newProject) {
        userValidationService.validateUserExists(userId);
        projectValidationService.isProjectAssignedToUser(userId,newProject.getProjectId());
        return userService.addProjectToUser(userId, newProject);
    }

    /**
     * Retrieves all external projects associated with a user.
     *
     * @param userId the ID of the user to retrieve projects for
     * @return a CompletableFuture containing a list of external projects associated with the user
     */
    @Override
    public CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId) {
        userValidationService.validateUserExists(userId);
        return userService.getProjectsByUserId(userId);
    }

    /**
     * Updates an existing user.
     *
     * @param userId the ID of the user to update
     * @param updatedUser the updated user information
     * @return a CompletableFuture containing the updated user
     */
    @Override
    public CompletableFuture<Boolean> updateUser(Long userId,@Valid UpdateUserDTO updatedUser) {
        userValidationService.validateUserExists(userId);
        return userService.updateUser(userId, updatedUser);
    }
}
