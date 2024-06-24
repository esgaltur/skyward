package com.sosnovich.skyward.data.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.IdentityGenerator;

import java.util.Objects;
import java.util.Set;

/**
 * Entity class representing a user in the application.
 */
@Entity
@Table(name = "tb_user")
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

    /**
     * The set of external projects associated with the user.
     * This field is managed by the {@link UserExternalProjectEntity} entity.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserExternalProjectEntity> externalProjects;

    // Getters and setters

    public Boolean getAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(Boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public Boolean getAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public Boolean getCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(Boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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

    public Set<UserExternalProjectEntity> getExternalProjects() {
        return externalProjects;
    }

    public void setExternalProjects(Set<UserExternalProjectEntity> externalProjects) {
        this.externalProjects = externalProjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return accountExpired == that.accountExpired &&
                accountLocked == that.accountLocked &&
                credentialsExpired == that.credentialsExpired &&
                disabled == that.disabled &&
                Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(name, that.name) &&
                Objects.equals(role, that.role) &&
                Objects.equals(externalProjects, that.externalProjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, role, accountExpired, accountLocked, credentialsExpired, disabled, externalProjects);
    }
}
