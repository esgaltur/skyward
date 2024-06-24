package com.sosnovich.skyward.mapping;

import com.sosnovich.skyward.data.model.UserEntity;
import com.sosnovich.skyward.dto.NewUserDTO;
import com.sosnovich.skyward.dto.UserDTO;
import com.sosnovich.skyward.openapi.model.NewUser;
import com.sosnovich.skyward.openapi.model.User;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

/**
 * Mapper interface for converting between different representations of users.
 */
@Mapper(builder = @Builder(disableBuilder = true), config = MappersConfiguration.class)
public interface UserMapper {

    /**
     * Converts a NewUser to a NewUserDTO.
     *
     * @param newUser the new user
     * @return the corresponding NewUserDTO
     */
  NewUserDTO toNewUserDTO(NewUser newUser);

    /**
     * Converts a NewUserDTO to a UserEntity.
     *
     * @param newUserDTO the new user DTO
     * @return the corresponding UserEntity
     */
    UserEntity toEntity(NewUserDTO newUserDTO);

    /**
     * Converts a UserDTO to a UserEntity.
     *
     * @param userDTO the user DTO
     * @return the corresponding UserEntity
     */
    UserEntity toEntity(UserDTO userDTO);

    /**
     * Converts a UserEntity to a UserDTO.
     *
     * @param userEntity the user entity
     * @return the corresponding UserDTO
     */

    UserDTO toDTO(UserEntity userEntity);

    /**
     * Converts a UserDTO to a User.
     *
     * @param userDTO the user DTO
     * @return the corresponding User
     */
    User toApiModel(UserDTO userDTO);



}
