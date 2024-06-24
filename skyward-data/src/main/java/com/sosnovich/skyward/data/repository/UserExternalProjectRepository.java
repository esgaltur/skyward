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

    boolean existsByUser_IdAndProjectId(Long id, String projectId);

    boolean existsByProjectId(String projectId);
}
