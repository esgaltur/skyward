package com.sosnovich.skyward.mapping;

import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.dto.NewUserDTO;
import com.sosnovich.skyward.dto.UserDTO;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
     void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
     void testToNewUserDTO() {
        NewUser newUser = new NewUser();
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("newpassword");
        newUser.setName("New User");

        NewUserDTO newUserDTO = userMapper.toNewUserDTO(newUser);

        assertNotNull(newUserDTO);
        assertEquals("newuser@example.com", newUserDTO.getEmail());
        assertEquals("newpassword", newUserDTO.getPassword());
        assertEquals("New User", newUserDTO.getName());
    }

    @Test
     void testToEntityFromNewUserDTO() {
        NewUserDTO newUserDTO = new NewUserDTO();
        newUserDTO.setEmail("newuser@example.com");
        newUserDTO.setPassword("newpassword");
        newUserDTO.setName("New User");

        UserEntity userEntity = userMapper.toEntity(newUserDTO);

        assertNotNull(userEntity);
        assertEquals("newuser@example.com", userEntity.getEmail());
        assertEquals("newpassword", userEntity.getPassword());
        assertEquals("New User", userEntity.getName());
    }

    @Test
     void testToEntityFromUserDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");
        userDTO.setName("User Name");

        UserEntity userEntity = userMapper.toEntity(userDTO);

        assertNotNull(userEntity);
        assertEquals(1L, userEntity.getId());
        assertEquals("user@example.com", userEntity.getEmail());
        assertEquals("User Name", userEntity.getName());
    }

    @Test
     void testToDTO() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("user@example.com");
        userEntity.setName("User Name");

        UserDTO userDTO = userMapper.toDTO(userEntity);

        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals("user@example.com", userDTO.getEmail());
        assertEquals("User Name", userDTO.getName());
    }

    @Test
     void testToApiModel() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("user@example.com");
        userDTO.setName("User Name");

        User user = userMapper.toApiModel(userDTO);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("user@example.com", user.getEmail());
        assertEquals("User Name", user.getName());
    }
}
