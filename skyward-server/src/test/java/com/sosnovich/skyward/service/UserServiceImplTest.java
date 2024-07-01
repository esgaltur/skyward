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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

 class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserExternalProjectRepository userExternalProjectRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
     void testCreateUser_Success() {
        NewUser newUser = new NewUser();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setName("Test User");

        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("test@example.com");
        newUserDTO.setPassword("encodedPassword");
        newUserDTO.setName("Test User");

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setName("Test User");

        UserEntity savedUserEntity = new UserEntity();
        savedUserEntity.setId(1L);
        savedUserEntity.setEmail("test@example.com");
        savedUserEntity.setPassword("encodedPassword");
        savedUserEntity.setName("Test User");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userMapper.toNewUserDTO(newUser)).thenReturn(newUserDTO);
        when(bCryptPasswordEncoder.encode(newUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toEntity(newUserDTO)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUserEntity);
        when(userMapper.toDTO(savedUserEntity)).thenReturn(userDTO);
        when(userMapper.toApiModel(userDTO)).thenReturn(user);

        CompletableFuture<User> result = userService.createUser(newUser);

        assertEquals(user, result.join());
    }

    @Test
     void testCreateUser_Failure() {
        NewUser newUser = new NewUser();
        newUser.setEmail("test@example.com");
        newUser.setPassword("password");
        newUser.setName("Test User");

        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("test@example.com");
        newUserDTO.setPassword("encodedPassword");
        newUserDTO.setName("Test User");

        when(userMapper.toNewUserDTO(newUser)).thenReturn(newUserDTO);
        when(bCryptPasswordEncoder.encode(newUserDTO.getPassword())).thenReturn("encodedPassword");
        when(userMapper.toEntity(newUserDTO)).thenReturn(new UserEntity());
        when(userRepository.save(any(UserEntity.class))).thenThrow(RuntimeException.class);

        CompletableFuture<User> result = userService.createUser(newUser);

        assertThrows(RuntimeException.class, result::join);
    }

    @Test
     void testGetUserById_Success() {
        Long userId = 1L;
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setEmail("test@example.com");
        userEntity.setName("Test User");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setEmail("test@example.com");
        userDTO.setName("Test User");

        User user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(userDTO);
        when(userMapper.toApiModel(userDTO)).thenReturn(user);

        CompletableFuture<Optional<User>> result = userService.getUserById(userId);

        assertEquals(Optional.of(user), result.join());
    }

    @Test
     void testGetUserById_NotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        CompletableFuture<Optional<User>> result = userService.getUserById(userId);

        assertEquals(Optional.empty(), result.join());
    }

    @Test
     void testDeleteUser_Success() {
        Long userId = 1L;

        doNothing().when(userRepository).deleteById(userId);

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
     void testAddProjectToUser_Success() {
        Long userId = 1L;
        NewExternalProject newProject = new NewExternalProject();
        newProject.setId("projectId");
        newProject.setName("Test Project");

        NewExternalProjectDTO newProjectDTO = new NewExternalProjectDTO();
        newProjectDTO.setProjectId("projectId");
        newProjectDTO.setName("Test Project");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);

        UserExternalProjectEntity projectEntity = new UserExternalProjectEntity();
        projectEntity.setUser(userEntity);
        projectEntity.setProjectId("projectId");
        projectEntity.setName("Test Project");

        UserExternalProjectEntity savedProjectEntity = new UserExternalProjectEntity();
        savedProjectEntity.setUser(userEntity);
        savedProjectEntity.setProjectId("projectId");
        savedProjectEntity.setName("Test Project");

        ExternalProjectDTO externalProjectDTO = new ExternalProjectDTO();
        externalProjectDTO.setProjectId("projectId");
        externalProjectDTO.setUserId(userId);
        externalProjectDTO.setName("Test Project");

        ExternalProject externalProject = new ExternalProject();
        externalProject.setId("projectId");
        externalProject.setUserId(userId);
        externalProject.setName("Test Project");

        when(projectMapper.toNewExternalProjectDTO(newProject)).thenReturn(newProjectDTO);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(projectMapper.toEntity(newProjectDTO)).thenReturn(projectEntity);
        when(userExternalProjectRepository.save(projectEntity)).thenReturn(savedProjectEntity);
        when(projectMapper.toDTO(savedProjectEntity)).thenReturn(externalProjectDTO);
        when(projectMapper.toApiModel(externalProjectDTO)).thenReturn(externalProject);

        CompletableFuture<ExternalProject> result = userService.addProjectToUser(userId, newProject);

        assertEquals(externalProject, result.join());
    }

    @Test
     void testGetProjectsByUserId_Success() {
        Long userId = 1L;
        UserExternalProjectEntity projectEntity = new UserExternalProjectEntity();
        projectEntity.setProjectId("projectId");
        var u = new UserEntity();
        u.setId(1L);
        projectEntity.setUser(u);
        projectEntity.setName("Test Project");

        ExternalProjectDTO externalProjectDTO = new ExternalProjectDTO();
        externalProjectDTO.setProjectId("projectId");
        externalProjectDTO.setUserId(userId);
        externalProjectDTO.setName("Test Project");

        ExternalProject externalProject = new ExternalProject();
        externalProject.setId("projectId");
        externalProject.setUserId(userId);
        externalProject.setName("Test Project");

        when(userExternalProjectRepository.findByUserId(userId)).thenReturn(List.of(projectEntity));
        when(projectMapper.toDTO(projectEntity)).thenReturn(externalProjectDTO);
        when(projectMapper.toApiModel(externalProjectDTO)).thenReturn(externalProject);

        CompletableFuture<List<ExternalProject>> result = userService.getProjectsByUserId(userId);

        List<ExternalProject> projects = result.join();
        assertEquals(1, projects.size());
        assertEquals("projectId", projects.get(0).getId());
    }

    @Test
     void testGetProjectsByUserId_NoProjects() {
        Long userId = 1L;

        when(userExternalProjectRepository.findByUserId(userId)).thenReturn(List.of());

        CompletableFuture<List<ExternalProject>> result = userService.getProjectsByUserId(userId);

        List<ExternalProject> projects = result.join();
        assertTrue(projects.isEmpty());
    }
}
