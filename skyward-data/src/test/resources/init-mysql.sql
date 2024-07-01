-- Create tb_user table
CREATE TABLE tb_user
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY, -- Primary key with auto-increment for unique user IDs
    email    VARCHAR(200) NOT NULL,             -- User email, cannot be null
    password VARCHAR(129) NOT NULL,             -- User password, cannot be null
    name     VARCHAR(120)                       -- User name, optional
);
-- Comment describing the table

-- Create tb_user_external_project table
CREATE TABLE tb_user_external_project
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY, -- Primary key with auto-increment for unique external project IDs
    project_id VARCHAR(200) NOT NULL,              -- Project ID, cannot be null
    user_id    BIGINT       NOT NULL,              -- Foreign key to the tb_user table, cannot be null
    name       VARCHAR(120) NOT NULL               -- Name of the external project, cannot be null
);
-- Comment describing the table

-- Add foreign key constraint
ALTER TABLE tb_user_external_project
    ADD CONSTRAINT fk_user_external_project_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id) -- Add a foreign key constraint to link user_id in tb_user_external_project to id in tb_user
            ON DELETE CASCADE;
-- Add a foreign key constraint to link user_id in tb_user_external_project to id in tb_user with cascading delete

-- Insert sample data into tb_user table
INSERT INTO tb_user (email, password, name)
VALUES ('user1@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User One'), -- Insert a user
       ('user2@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Two'), -- Insert a user
       ('user3@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Three'); -- Insert a user

-- Add columns to tb_user table
ALTER TABLE tb_user
    ADD COLUMN role                VARCHAR(120) NOT NULL DEFAULT 'ROLE_GUEST';
ALTER TABLE tb_user
    ADD COLUMN account_expired     BOOLEAN      NOT NULL DEFAULT FALSE;
ALTER TABLE tb_user
    ADD COLUMN account_locked      BOOLEAN      NOT NULL DEFAULT FALSE;
ALTER TABLE tb_user
    ADD COLUMN credentials_expired BOOLEAN      NOT NULL DEFAULT FALSE;
ALTER TABLE tb_user
    ADD COLUMN disabled            BOOLEAN      NOT NULL DEFAULT FALSE;

-- Update sample data with roles
UPDATE tb_user SET role = 'ROLE_USER' WHERE email IN ('user1@example.com', 'user2@example.com');
UPDATE tb_user SET role = 'ROLE_ADMIN' WHERE email = 'user3@example.com';
