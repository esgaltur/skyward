package com.sosnovich.skyward.data.repository;

import com.sosnovich.skyward.data.config.JpaConfig;
import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
class UserExternalProjectRepositoryTest {

    @Autowired
    private UserExternalProjectRepository userExternalProjectRepository;

    @Autowired
    private UserRepository userRepository;

    private UserEntity user;
    private UserExternalProjectEntity project1;
    private UserExternalProjectEntity project2;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setName("Test User");
        user.setRole("USER"); // Ensure the role is set

        user = userRepository.save(user);

        project1 = new UserExternalProjectEntity();
        project1.setProjectId("project1");
        project1.setName("Project One");
        project1.setUser(user);

        project2 = new UserExternalProjectEntity();
        project2.setProjectId("project2");
        project2.setName("Project Two");
        project2.setUser(user);

        userExternalProjectRepository.save(project1);
        userExternalProjectRepository.save(project2);
    }

    @Test
    void testFindByUserId() {
        List<UserExternalProjectEntity> projects = userExternalProjectRepository.findByUserId(user.getId());
        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertTrue(projects.stream().anyMatch(project -> project.getProjectId().equals("project1")));
        assertTrue(projects.stream().anyMatch(project -> project.getProjectId().equals("project2")));
    }

    @Test
    void testExistsByUser_IdAndProjectId_ProjectExists() {
        boolean exists = userExternalProjectRepository.existsByUser_IdAndProjectId(user.getId(), "project1");
        assertTrue(exists);
    }

    @Test
    void testExistsByUser_IdAndProjectId_ProjectDoesNotExist() {
        boolean exists = userExternalProjectRepository.existsByUser_IdAndProjectId(user.getId(), "project3");
        assertFalse(exists);
    }

}
