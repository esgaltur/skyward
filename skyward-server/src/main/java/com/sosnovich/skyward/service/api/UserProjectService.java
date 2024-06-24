package com.sosnovich.skyward.service.api;

import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserProjectService {
    CompletableFuture<User> createUser(NewUser newUser);

    CompletableFuture<Optional<User>> getUserById(Long id);

    void deleteUser(Long userId);

    CompletableFuture<ExternalProject> addProjectToUser(Long userId, NewExternalProject newProject);

    CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId);
}
