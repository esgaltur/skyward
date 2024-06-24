package com.sosnovich.skyward.mapping;

import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import com.sosnovich.skyward.dto.ExternalProjectDTO;
import com.sosnovich.skyward.dto.NewExternalProjectDTO;
import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between different representations of projects.
 */
@Mapper(uses = {UserMapper.class}, config = MappersConfiguration.class)
public interface ProjectMapper {

    /**
     * Converts a NewExternalProject to a NewExternalProjectDTO.
     *
     * @param newExternalProject the new external project
     * @return the corresponding NewExternalProjectDTO
     */
    @Mapping(target = "projectId",source = "id")
    NewExternalProjectDTO toNewExternalProjectDTO(NewExternalProject newExternalProject);

    /**
     * Converts a NewExternalProjectDTO to a UserExternalProjectEntity.
     *
     * @param newProjectDTO the new external project DTO
     * @return the corresponding UserExternalProjectEntity
     */
    UserExternalProjectEntity toEntity(NewExternalProjectDTO newProjectDTO);

    /**
     * Converts a UserExternalProjectEntity to an ExternalProjectDTO.
     *
     * @param projectEntity the user external project entity
     * @return the corresponding ExternalProjectDTO
     */

    ExternalProjectDTO toDTO(UserExternalProjectEntity projectEntity);

    /**
     * Converts an ExternalProjectDTO to an ExternalProject.
     *
     * @param externalProjectDTO the external project DTO
     * @return the corresponding ExternalProject
     */
    @Mapping(target = "id", source = "projectId")
    ExternalProject toApiModel(ExternalProjectDTO externalProjectDTO);
}
