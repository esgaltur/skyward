ALTER USER 'root'@'%' IDENTIFIED BY 'toor'; -- Change the password for the root user

GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION; -- Grant all privileges to the root user with the ability to grant those privileges to others

CREATE DATABASE IF NOT EXISTS skyward; -- Create the skyward database if it doesn't already exist

GRANT ALL PRIVILEGES ON skyward.* TO 'skydmin'@'%'; -- Grant all privileges on the skyward database to the user skydmin

FLUSH PRIVILEGES;
-- Reload the privileges from the grant tables in the database

-- Create tb_user table
CREATE TABLE tb_user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'unique identifier of the userEntity', -- Primary key with auto-increment for unique user IDs
    email    VARCHAR(200) NOT NULL COMMENT 'email for userEntity',                            -- User email, cannot be null
    password VARCHAR(129) NOT NULL COMMENT 'password',                                        -- User password, cannot be null
    name     VARCHAR(120) COMMENT 'comment'                                                   -- User name, optional
) COMMENT ='All users';
-- Comment describing the table

-- Create tb_user_external_project table
CREATE TABLE tb_user_external_project
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'primary key of external project', -- Primary key with auto-increment for unique external project IDs
    project_id VARCHAR(200) NOT NULL COMMENT 'identifier of external project',              -- Project ID, cannot be null
    user_id    BIGINT       NOT NULL COMMENT 'unique identifier of the userEntity',         -- Foreign key to the tb_user table, cannot be null
    name       VARCHAR(120) NOT NULL COMMENT 'Name of external project'                     -- Name of the external project, cannot be null
) COMMENT ='External Project identifier for users';
-- Comment describing the table

-- Add foreign key constraint
ALTER TABLE tb_user_external_project
    ADD CONSTRAINT fk_user_external_project_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id) -- Add a foreign key constraint to link user_id in tb_user_external_project to id in tb_user
            ON DELETE CASCADE;
-- Add a foreign key constraint to link user_id in tb_user_external_project to id in tb_user with cascading delete
-- Add columns to tb_user table
ALTER TABLE tb_user
    ADD COLUMN role                VARCHAR(120) NOT NULL DEFAULT 'GUEST' COMMENT 'User role', -- Add a role column with a default value of 'GUEST'
    ADD COLUMN account_expired     BOOLEAN      NOT NULL DEFAULT FALSE, -- Add a column to indicate if the account is expired, default to FALSE
    ADD COLUMN account_locked      BOOLEAN      NOT NULL DEFAULT FALSE, -- Add a column to indicate if the account is locked, default to FALSE
    ADD COLUMN credentials_expired BOOLEAN      NOT NULL DEFAULT FALSE, -- Add a column to indicate if the credentials are expired, default to FALSE
    ADD COLUMN disabled            BOOLEAN      NOT NULL DEFAULT FALSE;
-- Add a column to indicate if the account is disabled, default to FALSE

-- Insert sample data into tb_user table
INSERT INTO tb_user (id, email, password, name, role, account_expired, account_locked, credentials_expired, disabled)
VALUES (1, 'user1@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User One', 'ROLE_USER',
        FALSE, FALSE, FALSE, FALSE), -- Insert a user with ID 1
       (2, 'user2@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Two', 'ROLE_USER',
        FALSE, FALSE, FALSE, FALSE), -- Insert a user with ID 2
       (3, 'user3@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Three',
        'ROLE_ADMIN', FALSE, FALSE, FALSE, FALSE); -- Insert a user with ID 3
