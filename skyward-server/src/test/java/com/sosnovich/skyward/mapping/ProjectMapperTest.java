package com.sosnovich.skyward.mapping;

import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import com.sosnovich.skyward.dto.ExternalProjectDTO;
import com.sosnovich.skyward.dto.NewExternalProjectDTO;
import com.sosnovich.skyward.openapi.model.ExternalProject;
import com.sosnovich.skyward.openapi.model.NewExternalProject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class ProjectMapperTest {

    private ProjectMapper projectMapper;

    @BeforeEach
    public void setUp() {
        projectMapper = Mappers.getMapper(ProjectMapper.class);
    }

    @Test
     void testToNewExternalProjectDTO() {
        NewExternalProject newExternalProject = new NewExternalProject();
        newExternalProject.setId("123");
        newExternalProject.setName("Project Name");

        NewExternalProjectDTO newExternalProjectDTO = projectMapper.toNewExternalProjectDTO(newExternalProject);

        assertNotNull(newExternalProjectDTO);
        assertEquals("123", newExternalProjectDTO.getProjectId());
        assertEquals("Project Name", newExternalProjectDTO.getName());
    }

    @Test
     void testToEntity() {
        NewExternalProjectDTO newExternalProjectDTO = new NewExternalProjectDTO();
        newExternalProjectDTO.setProjectId("123");
        newExternalProjectDTO.setName("Project Name");

        UserExternalProjectEntity entity = projectMapper.toEntity(newExternalProjectDTO);

        assertNotNull(entity);
        assertEquals("123", entity.getProjectId());
        assertEquals("Project Name", entity.getName());
    }

    @Test
     void testToDTO() {
        UserExternalProjectEntity entity = new UserExternalProjectEntity();
        entity.setProjectId("123");
        entity.setName("Project Name");

        ExternalProjectDTO dto = projectMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals("123", dto.getProjectId());
        assertEquals("Project Name", dto.getName());
    }

    @Test
     void testToApiModel() {
        ExternalProjectDTO externalProjectDTO = new ExternalProjectDTO();
        externalProjectDTO.setProjectId("123");
        externalProjectDTO.setName("Project Name");

        ExternalProject apiModel = projectMapper.toApiModel(externalProjectDTO);

        assertNotNull(apiModel);
        assertEquals("123", apiModel.getId());
        assertEquals("Project Name", apiModel.getName());
    }
}
