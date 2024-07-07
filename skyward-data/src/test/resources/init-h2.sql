-- Create tb_user table
CREATE TABLE tb_user
(
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY, -- Primary key with auto-increment for unique user IDs
    email               VARCHAR(200) NOT NULL,             -- User email, cannot be null
    password            VARCHAR(129) NOT NULL,             -- User password, cannot be null
    name                VARCHAR(120),                      -- User name, optional
    role                VARCHAR(120) NOT NULL DEFAULT 'GUEST', -- User role with a default value of 'GUEST'
    account_expired     BOOLEAN      NOT NULL DEFAULT FALSE, -- Account expired, default to FALSE
    account_locked      BOOLEAN      NOT NULL DEFAULT FALSE, -- Account locked, default to FALSE
    credentials_expired BOOLEAN      NOT NULL DEFAULT FALSE, -- Credentials expired, default to FALSE
    disabled            BOOLEAN      NOT NULL DEFAULT FALSE, -- Account disabled, default to FALSE
    version             INT          NOT NULL DEFAULT 0     -- Version for optimistic locking
);

-- Create tb_user_external_project table
CREATE TABLE tb_user_external_project
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY, -- Primary key with auto-increment for unique external project IDs
    project_id VARCHAR(200) NOT NULL,             -- Project ID, cannot be null
    user_id    BIGINT       NOT NULL,             -- Foreign key to the tb_user table, cannot be null
    name       VARCHAR(120) NOT NULL,             -- Name of external project, cannot be null
    version    INT          NOT NULL DEFAULT 0    -- Version for optimistic locking
);

-- Create index on user_id and project_id in tb_user_external_project table
CREATE INDEX idx_user_project ON tb_user_external_project(user_id, project_id);

-- Add foreign key constraint to link user_id in tb_user_external_project to id in tb_user with cascading delete
ALTER TABLE tb_user_external_project
    ADD CONSTRAINT fk_user_external_project_user
        FOREIGN KEY (user_id)
            REFERENCES tb_user (id)
            ON DELETE CASCADE;

-- Insert sample data into tb_user table
INSERT INTO tb_user (email, password, name, role, account_expired, account_locked, credentials_expired, disabled)
VALUES
    ('user1@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User One', 'ROLE_USER', FALSE, FALSE, FALSE, FALSE),
    ('user2@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Two', 'ROLE_USER', FALSE, FALSE, FALSE, FALSE),
    ('user3@example.com', '$2b$12$B86ivK35iY.25RDvRJ7I1ucNvlYwWfCvvMatfTjMLKqxS6Z/fvHN6', 'User Three', 'ROLE_ADMIN', FALSE, FALSE, FALSE, FALSE);
