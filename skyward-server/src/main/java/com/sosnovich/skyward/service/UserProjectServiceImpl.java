package com.sosnovich.skyward.service;

import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;
import com.sosnovich.skyward.service.api.UserService;
import com.sosnovich.skyward.service.api.UserValidationService;
import com.sosnovich.skyward.service.api.ProjectValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserProjectServiceImpl implements com.sosnovich.skyward.service.api.UserProjectService {

    private final UserService userService;
    private final UserValidationService userValidationService;
    private final ProjectValidationService projectValidationService;

    @Autowired
    public UserProjectServiceImpl(UserService userService, UserValidationService userValidationService, ProjectValidationService projectValidationService) {
        this.userService = userService;
        this.userValidationService = userValidationService;
        this.projectValidationService = projectValidationService;
    }

    @Override
    public CompletableFuture<User> createUser(NewUser newUser) {
        userValidationService.validateEmailNotInUse(newUser.getEmail());
        return userService.createUser(newUser);
    }

    @Override
    public CompletableFuture<Optional<User>> getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public void deleteUser(Long userId) {
        userValidationService.validateUserExists(userId);
        userService.deleteUser(userId);
    }

    @Override
    public CompletableFuture<ExternalProject> addProjectToUser(Long userId, NewExternalProject newProject) {
        userValidationService.validateUserExists(userId);
        projectValidationService.isProjectExist(newProject.getId());

        return userService.addProjectToUser(userId, newProject);
    }

    @Override
    public CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId) {
        userValidationService.validateUserExists(userId);
        return userService.getProjectsByUserId(userId);
    }
}
