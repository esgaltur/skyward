package com.sosnovich.skyward.data.repository;

import com.sosnovich.skyward.data.model.UserExternalProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing and managing {@link UserExternalProjectEntity} entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations and custom queries.
 */
@Repository
public interface UserExternalProjectRepository extends JpaRepository<UserExternalProjectEntity, Long> {

    /**
     * Finds a list of {@link UserExternalProjectEntity} by the user ID.
     *
     * @param userId the ID of the user
     * @return a list of {@link UserExternalProjectEntity} associated with the specified user ID
     */
    List<UserExternalProjectEntity> findByUserId(Long userId);

    /**
     * Checks if a project with the specified ID is assigned to a user with the specified ID.
     *
     * @param id        the ID of the user
     * @param projectId the ID of the project
     * @return true if the project is assigned to the user, false otherwise
     */
    boolean existsByUser_IdAndProjectId(Long id, String projectId);


}
