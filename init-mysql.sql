-- Change the password for the root user
ALTER USER 'root'@'%' IDENTIFIED BY 'toor';

-- Grant all privileges to the root user with the ability to grant those privileges to others
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- Create the skyward database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS skyward;

-- Grant all privileges on the skyward database to the user skydmin
GRANT ALL PRIVILEGES ON skyward.* TO 'skydmin'@'%';

-- Reload the privileges from the grant tables in the database
FLUSH PRIVILEGES;

-- Create tb_user table
CREATE TABLE tb_user
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'unique identifier of the userEntity', -- Primary key with auto-increment for unique user IDs
    email               VARCHAR(200) NOT NULL COMMENT 'email for userEntity',                            -- User email, cannot be null
    password            VARCHAR(129) NOT NULL COMMENT 'password',                                        -- User password, cannot be null
    name                VARCHAR(120) COMMENT 'User name',                                                -- User name, optional
    role                VARCHAR(120) NOT NULL DEFAULT 'GUEST' COMMENT 'User role',                       -- Add a role column with a default value of 'GUEST'
    account_expired     BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Indicate if the account is expired', -- Account expired, default to FALSE
    account_locked      BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Indicate if the account is locked',  -- Account locked, default to FALSE
    credentials_expired BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Indicate if the credentials are expired', -- Credentials expired, default to FALSE
    disabled            BOOLEAN      NOT NULL DEFAULT FALSE COMMENT 'Indicate if the account is disabled', -- Account disabled, default to FALSE
    version             INT          NOT NULL DEFAULT 0 COMMENT 'Version for optimistic locking'         -- Version for optimistic locking
) COMMENT ='All users';

-- Create tb_user_external_project table
CREATE TABLE tb_user_external_project
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'primary key of external project', -- Primary key with auto-increment for unique external project IDs
    project_id VARCHAR(200) NOT NULL COMMENT 'identifier of external project',              -- Project ID, cannot be null
    user_id    BIGINT       NOT NULL COMMENT 'unique identifier of the userEntity',         -- Foreign key to the tb_user table, cannot be null
    name       VARCHAR(120) NOT NULL COMMENT 'Name of external project',                    -- Name of the external project, cannot be null
    version    INT          NOT NULL DEFAULT 0 COMMENT 'Version for optimistic locking',    -- Version for optimistic locking
    INDEX idx_user_project (user_id, project_id)                                            -- Index on user_id and project_id
) COMMENT ='External Project identifier for users';

-- Add foreign key constraint to link user_id in tb_user_external_project to id in tb_user with cascading delete
ALTER TABLE tb_user_external_project
    ADD CONSTRAINT fk_user_external_project_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE;

-- Insert sample data into tb_user table
INSERT INTO tb_user (id, email, password, name, role, account_expired, account_locked, credentials_expired, disabled)
VALUES
    (1, 'user1@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User One', 'ROLE_USER', FALSE, FALSE, FALSE, FALSE),
    (2, 'user2@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Two', 'ROLE_USER', FALSE, FALSE, FALSE, FALSE),
    (3, 'user3@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Three', 'ROLE_ADMIN', FALSE, FALSE, FALSE, FALSE);
