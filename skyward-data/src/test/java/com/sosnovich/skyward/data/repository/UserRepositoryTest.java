package com.sosnovich.skyward.data.repository;

import com.sosnovich.skyward.data.config.JpaConfig;
import com.sosnovich.skyward.data.model.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = JpaConfig.class)
@ActiveProfiles("test")
 class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private UserEntity user1;
    private UserEntity user2;

    @BeforeEach
     void setUp() {
        user1 = new UserEntity();
        user1.setEmail("user28@example.com");
        user1.setPassword("$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6");
        user1.setName("User One");
        user1.setRole("ROLE_USER");
        user1.setAccountExpired(false);
        user1.setAccountLocked(false);
        user1.setCredentialsExpired(false);
        user1.setDisabled(false);

        user2 = new UserEntity();
        user2.setEmail("user55@example.com");
        user2.setPassword("$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6");
        user2.setName("User Two");
        user2.setRole("ROLE_USER");
        user2.setAccountExpired(false);
        user2.setAccountLocked(false);
        user2.setCredentialsExpired(false);
        user2.setDisabled(false);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
     void testFindByEmail_HappyPath() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("user28@example.com");
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
     void testFindByEmail_UnhappyPath() {
        Optional<UserEntity> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertThat(foundUser).isNotPresent();
    }

    @Test
     void testExistsByEmail_HappyPath() {
        boolean exists = userRepository.existsByEmail("user1@example.com");
        assertThat(exists).isTrue();
    }

    @Test
     void testExistsByEmail_UnhappyPath() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
        assertThat(exists).isFalse();
    }

    @Test
     void testSaveUser() {
        UserEntity newUser = new UserEntity();
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6");
        newUser.setName("New User");
        newUser.setRole("ROLE_USER");
        newUser.setAccountExpired(false);
        newUser.setAccountLocked(false);
        newUser.setCredentialsExpired(false);
        newUser.setDisabled(false);

        UserEntity savedUser = userRepository.save(newUser);
        Optional<UserEntity> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
     void testDeleteUser() {
        userRepository.delete(user1);
        Optional<UserEntity> foundUser = userRepository.findById(user1.getId());
        assertThat(foundUser).isNotPresent();
    }
}
