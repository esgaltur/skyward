package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.data.repository.UserRepository;
import com.sosnovich.skyward.dto.*;
import com.sosnovich.skyward.exc.ConcurrencyException;
import com.sosnovich.skyward.mapping.ProjectMapper;
import com.sosnovich.skyward.mapping.UserMapper;
import com.sosnovich.skyward.openapi.model.*;
import com.sosnovich.skyward.service.api.UserService;

import io.github.resilience4j.retry.annotation.Retry;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service implementation for handling user and project-related operations.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserExternalProjectRepository userExternalProjectRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    /**
     * Constructs a new UserServiceImpl.
     *
     * @param userRepository                the repository for user entities
     * @param userExternalProjectRepository the repository for user external project entities
     * @param userMapper                    the mapper for user entities and DTOs
     * @param projectMapper                 the mapper for project entities and DTOs
     * @param bCryptPasswordEncoder         the password encoder
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserExternalProjectRepository userExternalProjectRepository,
                           UserMapper userMapper, ProjectMapper projectMapper, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userExternalProjectRepository = userExternalProjectRepository;
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * Creates a new user.
     *
     * @param newUser the new user to be created
     * @return a CompletableFuture containing the created user
     */
    @Async
    @Override
    public CompletableFuture<User> createUser(NewUserDTO newUser) {
        return CompletableFuture.supplyAsync(() -> {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            UserEntity userEntity = userMapper.toEntity(newUser);
            UserEntity savedUserEntity = userRepository.save(userEntity);
            UserDTO userDTO = userMapper.toDTO(savedUserEntity);
            return userMapper.toApiModel(userDTO);
        });
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return a CompletableFuture containing an Optional with the retrieved user, if found
     */
    @Async
    @Override
    public CompletableFuture<Optional<User>> getUserById(Long id) {
        return CompletableFuture.supplyAsync(() ->
                userRepository.findById(id)
                        .map(userMapper::toDTO)
                        .map(userMapper::toApiModel)
        );
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     */
    @Async
    @Transactional
    @Retry(name = "deleteUser", fallbackMethod = "deleteUserFallback")
    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (OptimisticLockException e) {
            throw new ConcurrencyException("The user record you are trying to delete has been modified by another transaction. Please try again.");
        }
    }

    /**
     * Fallback method for deleteUser in case of retry failure.
     *
     * @param userId the ID of the user to delete
     * @param t      the throwable that caused the fallback
     */
    public void deleteUserFallback(Long userId, Throwable t) {
        throw new ConcurrencyException("The user record you are trying to delete could not be processed due to concurrent modification. Please try again later.");
    }

    /**
     * Adds a new external project to a user.
     *
     * @param userId     the ID of the user to add the project to
     * @param newProject the new external project to add
     * @return a CompletableFuture containing the added external project
     */
    @Async
    @Retry(name = "addProjectToUser", fallbackMethod = "addProjectToUserFallback")
    @Override
    public CompletableFuture<ExternalProject> addProjectToUser(Long userId, NewExternalProjectDTO newProject) {
        return CompletableFuture.supplyAsync(() -> {
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
            UserExternalProjectEntity projectEntity = projectMapper.toEntity(newProject);
            projectEntity.setUser(userEntity);
            try {
                UserExternalProjectEntity savedProjectEntity = userExternalProjectRepository.save(projectEntity);
                ExternalProjectDTO externalProjectDTO = projectMapper.toDTO(savedProjectEntity);
                return projectMapper.toApiModel(externalProjectDTO);
            } catch (OptimisticLockException e) {
                throw new ConcurrencyException("The project record you are trying to add has been modified by another transaction. Please try again.");
            }
        });
    }

    /**
     * Fallback method for addProjectToUser in case of retry failure.
     *
     * @param userId     the ID of the user to add the project to
     * @param newProject the new external project to add
     * @param t          the throwable that caused the fallback
     * @return a CompletableFuture containing the fallback result
     */
    public CompletableFuture<ExternalProject> addProjectToUserFallback(Long userId, NewExternalProjectDTO newProject, Throwable t) {
        throw new ConcurrencyException("The project record you are trying to add could not be processed due to concurrent modification. Please try again later.");
    }

    /**
     * Retrieves all external projects associated with a user.
     *
     * @param userId the ID of the user to retrieve projects for
     * @return a CompletableFuture containing a list of external projects associated with the user
     */
    @Async
    @Override
    public CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId) {
        return CompletableFuture.supplyAsync(() -> userExternalProjectRepository.findByUserId(userId).stream()
                .map(projectMapper::toDTO)
                .map(projectMapper::toApiModel)
                .toList());
    }

    /**
     * Updates an existing user.
     *
     * @param userId      the ID of the user to update
     * @param updatedUser the updated user information
     * @return a CompletableFuture containing the updated user
     */
    @Async
    @Transactional
    @Retry(name = "updateUser", fallbackMethod = "updateUserFallback")
    @Override
    public CompletableFuture<Boolean> updateUser(Long userId, UpdateUserDTO updatedUser) {
        return CompletableFuture.supplyAsync(() -> {
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
            if (updatedUser.getPassword() != null) {
                updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
            }
            userMapper.updateUserFromDto(updatedUser, userEntity);
            try {
                userRepository.save(userEntity);
                return true;
            } catch (OptimisticLockException e) {
                throw new ConcurrencyException("The user record you are trying to update has been modified by another transaction. Please try again.");
            }
        });
    }

    /**
     * Fallback method for updateUser in case of retry failure.
     *
     * @param userId      the ID of the user toContinuing from where the code snippet ended, here's the complete `UserServiceImpl` class with detailed Javadoc comments, including the final part of the updateUserFallback method:

    ```java
    /**
     * Fallback method for updateUser in case of retry failure.
     *
     * @param userId      the ID of the user to update
     * @param updatedUser the updated user information
     * @param t           the throwable that caused the fallback
     * @return a CompletableFuture containing the fallback result
     */
    public CompletableFuture<Boolean> updateUserFallback(Long userId, UpdateUserDTO updatedUser, Throwable t) {
        throw new ConcurrencyException("The user record you are trying to update could not be processed due to concurrent modification. Please try again later.");
    }
}
