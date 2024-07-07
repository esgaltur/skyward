package com.sosnovich.skyward.data.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.IdentityGenerator;

/**
 * Entity class representing an external project associated with a user in the application.
 */
@Entity
@Table(name = "tb_user_external_project")
@EqualsAndHashCode
public class UserExternalProjectEntity {

    /**
     * The unique identifier for the external project.
     * Generated automatically using the specified strategy and generator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tb_user_external_project_seq")
    @GenericGenerator(name = "tb_user_external_project_seq", type = IdentityGenerator.class)
    private Long id;

    /**
     * The project identifier.
     * This field is mandatory and has a maximum length of 200 characters.
     */
    @Column(name = "project_id", nullable = false, length = 200)
    private String projectId;

    /**
     * The user associated with the external project.
     * This relationship is managed by the {@link UserEntity} entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    /**
     * The name of the external project.
     * This field is mandatory and has a maximum length of 120 characters.
     */
    @Column(name = "name", nullable = false, length = 120)
    private String name;


    @Version
    private int version;


    /**
     * Gets the project identifier.
     *
     * @return the project identifier
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Sets the project identifier.
     *
     * @param projectId the project identifier to set
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * Gets the user associated with the external project.
     *
     * @return the user associated with the external project
     */
    public UserEntity getUser() {
        return user;
    }

    /**
     * Sets the user associated with the external project.
     *
     * @param user the user to set
     */
    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Gets the name of the external project.
     *
     * @return the name of the external project
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the external project.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier for the external project.
     *
     * @return the unique identifier for the external project
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the external project.
     *
     * @param id the unique identifier to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
