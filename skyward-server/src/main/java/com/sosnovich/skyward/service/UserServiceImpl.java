// src/main/java/com/sosnovich/skyward/service/UserServiceImpl.java
package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.data.repository.UserRepository;
import com.sosnovich.skyward.dto.ExternalProjectDTO;
import com.sosnovich.skyward.dto.NewExternalProjectDTO;
import com.sosnovich.skyward.dto.NewUserDTO;
import com.sosnovich.skyward.dto.UserDTO;
import com.sosnovich.skyward.mapping.ProjectMapper;
import com.sosnovich.skyward.mapping.UserMapper;
import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;
import com.sosnovich.skyward.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserExternalProjectRepository userExternalProjectRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserExternalProjectRepository userExternalProjectRepository, UserMapper userMapper, ProjectMapper projectMapper,
                           PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.userExternalProjectRepository = userExternalProjectRepository;
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Async
    @Override
    public CompletableFuture<User> createUser(NewUser newUser) {
        return CompletableFuture.supplyAsync(() -> {
            NewUserDTO newUserDTO = userMapper.toNewUserDTO(newUser);
            newUserDTO.setPassword(bCryptPasswordEncoder.encode(newUserDTO.getPassword()));
            UserEntity userEntity = userMapper.toEntity(newUserDTO);
            UserEntity savedUserEntity = userRepository.save(userEntity);
            UserDTO userDTO = userMapper.toDTO(savedUserEntity);
            return userMapper.toApiModel(userDTO);
        });
    }

    @Async
    @Override
    public CompletableFuture<Optional<User>> getUserById(Long id) {
        return CompletableFuture.supplyAsync(() ->             // Fetch user info logic here
             userRepository.findById(id)
                    .map(userMapper::toDTO)
                    .map(userMapper::toApiModel)
        );
    }

    @Async
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Async
    @Override
    public CompletableFuture<ExternalProject> addProjectToUser(Long userId, NewExternalProject newProject) {
        return CompletableFuture.supplyAsync(() -> {
            NewExternalProjectDTO newProjectDTO = projectMapper.toNewExternalProjectDTO(newProject);
            UserEntity userEntity = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with ID: " + userId));
            UserExternalProjectEntity projectEntity = projectMapper.toEntity(newProjectDTO);
            projectEntity.setUser(userEntity);
            UserExternalProjectEntity savedProjectEntity = userExternalProjectRepository.save(projectEntity);
            ExternalProjectDTO externalProjectDTO = projectMapper.toDTO(savedProjectEntity);
            return projectMapper.toApiModel(externalProjectDTO);
        });
    }

    @Async
    @Override
    public CompletableFuture<List<ExternalProject>> getProjectsByUserId(Long userId) {
        return CompletableFuture.supplyAsync(() -> userExternalProjectRepository.findByUserId(userId).stream()
                .map(projectMapper::toDTO)
                .map(projectMapper::toApiModel)
                .toList());
    }
}
