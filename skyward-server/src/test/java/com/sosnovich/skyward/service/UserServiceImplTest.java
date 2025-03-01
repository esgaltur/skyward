package com.sosnovich.skyward.service;

import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import com.sosnovich.skyward.data.repository.UserExternalProjectRepository;
import com.sosnovich.skyward.data.repository.UserRepository;
import com.sosnovich.skyward.dto.*;
import com.sosnovich.skyward.exc.ConcurrencyException;
import com.sosnovich.skyward.mapping.ProjectMapper;
import com.sosnovich.skyward.mapping.UserMapper;
import com.sosnovich.skyward.mapping.UserMapperImpl;
import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;
import jakarta.persistence.OptimisticLockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Import(UserMapperImpl.class)
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

        CompletableFuture<User> result = userService.createUser(newUserDTO);

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

        CompletableFuture<User> result = userService.createUser(newUserDTO);

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

        CompletableFuture<ExternalProject> result = userService.addProjectToUser(userId, newProjectDTO);

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

   @Test
   void testDeleteUserThrowsConcurrencyException() {
      Long userId = 1L;

      doThrow(new OptimisticLockException()).when(userRepository).deleteById(userId);

      ConcurrencyException exception = assertThrows(ConcurrencyException.class, () -> {
         userService.deleteUser(userId);
      });

      assertEquals("The user record you are trying to delete has been modified by another transaction. Please try again.", exception.getMessage());
   }

   @Test
   void testAddProjectToUserThrowsConcurrencyException() {
      Long userId = 1L;
      NewExternalProjectDTO newProject = new NewExternalProjectDTO();
      newProject.setName("New Project");
      newProject.setProjectId("proj-123");

      UserEntity userEntity = new UserEntity();
      when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
      when(projectMapper.toEntity(newProject)).thenReturn(new UserExternalProjectEntity());
      doThrow(new OptimisticLockException()).when(userExternalProjectRepository).save(any(UserExternalProjectEntity.class));

      // First, get the CompletableFuture
      CompletableFuture<ExternalProject> future = userService.addProjectToUser(userId, newProject);

      // Then, test the join() method separately to ensure only one method is tested for exception
      CompletionException exception = assertThrows(CompletionException.class, future::join);

      Throwable cause = exception.getCause();
      assertEquals(ConcurrencyException.class, cause.getClass());
      assertEquals("The project record you are trying to add has been modified by another transaction. Please try again.", cause.getMessage());
   }
   @Test
   void testUpdateUserThrowsConcurrencyException() {
      Long userId = 1L;
      UpdateUserDTO updatedUser = new UpdateUserDTO("updated@example.com", "newpassword", "Updated User", "ROLE_USER", false, false, false, false);

      UserEntity userEntity = new UserEntity();
      when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
      doThrow(new OptimisticLockException()).when(userRepository).save(any(UserEntity.class));

      // First, get the CompletableFuture
      CompletableFuture<Boolean> future = userService.updateUser(userId, updatedUser);

      // Then, test the join() method separately to ensure only one method is tested for exception
      CompletionException exception = assertThrows(CompletionException.class, future::join);

      Throwable cause = exception.getCause();
      assertEquals(ConcurrencyException.class, cause.getClass());
      assertEquals("The user record you are trying to update has been modified by another transaction. Please try again.", cause.getMessage());
   }

}
