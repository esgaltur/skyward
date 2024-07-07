package com.sosnovich.skyward.data.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.IdentityGenerator;

import java.util.Set;

/**
 * Entity class representing a user in the application.
 */
@Entity
@Table(name = "tb_user")
@EqualsAndHashCode
public class UserEntity {

    /**
     * The unique identifier for the user.
     * Generated automatically using the specified strategy and generator.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "tb_user_seq")
    @GenericGenerator(name = "tb_user_seq", type = IdentityGenerator.class)
    private Long id;

    /**
     * The email of the user.
     * This field is mandatory and has a maximum length of 200 characters.
     */
    @Column(name = "email", nullable = false, length = 200)
    private String email;

    /**
     * The password of the user.
     * This field is mandatory and has a maximum length of 129 characters.
     */
    @Column(name = "password", nullable = false, length = 129)
    private String password;

    /**
     * The name of the user.
     * This field is optional and has a maximum length of 120 characters.
     */
    @Column(name = "name", length = 120)
    private String name;

    /**
     * The role of the user.
     * This field is mandatory and has a maximum length of 50 characters.
     */
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    /**
     * Indicates whether the user's account is expired.
     */
    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired;

    /**
     * Indicates whether the user's account is locked.
     */
    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked;

    /**
     * Indicates whether the user's credentials are expired.
     */
    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired;

    /**
     * Indicates whether the user's account is disabled.
     */
    @Column(nullable = false)
    private boolean disabled;

    @Version
    private int version;


    /**
     * The set of external projects associated with the user.
     * This field is managed by the {@link UserExternalProjectEntity} entity.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserExternalProjectEntity> externalProjects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Set<UserExternalProjectEntity> getExternalProjects() {
        return externalProjects;
    }

    public void setExternalProjects(Set<UserExternalProjectEntity> externalProjects) {
        this.externalProjects = externalProjects;
    }
}
