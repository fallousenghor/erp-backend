-- V1: Auth & Users — roles (avec permissions JSON), users, user_roles, refresh_tokens
-- Simplification: permissions stockées en JSON dans roles (pas de table séparée)

-- ─── ROLES ────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS roles (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),
    permissions JSONB        NOT NULL DEFAULT '[]'::jsonb,  -- Liste des permissions ex: ["user:create", "employee:read"]
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now(),
    version     BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE (name)
);

-- ─── USERS ────────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id              UUID         NOT NULL DEFAULT gen_random_uuid(),
    username        VARCHAR(50)  NOT NULL,
    email           VARCHAR(150) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(80)  NOT NULL,
    last_name       VARCHAR(80)  NOT NULL,
    phone           VARCHAR(20),
    avatar_url      VARCHAR(500),
    enabled         BOOLEAN      NOT NULL DEFAULT true,
    locked          BOOLEAN      NOT NULL DEFAULT false,
    failed_attempts INT          NOT NULL DEFAULT 0,
    lock_time       TIMESTAMP,
    last_login      TIMESTAMP,
    department_id   UUID,
    position_title  VARCHAR(100),
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_users          PRIMARY KEY (id),
    CONSTRAINT uk_user_username  UNIQUE (username),
    CONSTRAINT uk_user_email     UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_users_username ON users(username);
CREATE INDEX IF NOT EXISTS idx_users_email    ON users(email);

-- ─── USER_ROLES ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- ─── REFRESH_TOKENS ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS refresh_tokens (
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    token      VARCHAR(500) NOT NULL,
    user_id    UUID         NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT false,
    revoked_at TIMESTAMP,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL DEFAULT now(),
    version    BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_refresh_tokens PRIMARY KEY (id),
    CONSTRAINT uk_refresh_token  UNIQUE (token),
    CONSTRAINT fk_rt_user        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user ON refresh_tokens(user_id);

-- ─── SEED DATA — Roles avec permissions ───────────────────────────────────────
INSERT INTO roles (id, name, description, permissions) VALUES
    (gen_random_uuid(), 'ROLE_ADMIN', 'Administrateur système - Accès complet', 
     '["user:*", "employee:*", "department:*", "leave:*", "invoice:*", "payment:*", "expense:*", "asset:*", "student:*", "program:*", "enrollment:*", "project:*", "document:*", "contact:*", "deal:*", "purchase:*", "dashboard:*", "audit:*", "settings:*"]'::jsonb),
    (gen_random_uuid(), 'ROLE_HR_MANAGER', 'Responsable Ressources Humaines', 
     '["employee:*", "department:read", "leave:*", "attendance:*", "asset:read", "dashboard:read"]'::jsonb),
    (gen_random_uuid(), 'ROLE_FINANCE', 'Responsable Finance', 
     '["invoice:*", "payment:*", "expense:*", "contact:read", "dashboard:read"]'::jsonb),
    (gen_random_uuid(), 'ROLE_MANAGER', 'Manager / Chef de projet', 
     '["employee:read", "department:read", "project:*", "leave:read", "dashboard:read"]'::jsonb),
    (gen_random_uuid(), 'ROLE_USER', 'Utilisateur standard', 
     '["employee:read", "department:read", "leave:request", "dashboard:read"]'::jsonb);

-- ─── SEED DATA — Admin par défaut ─────────────────────────────────────────────
-- Mot de passe: Admin@123 (BCrypt hash)
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, locked, failed_attempts, created_at, updated_at, version)
VALUES (
    gen_random_uuid(),
    'admin',
    'admin@tornadoesjob.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'Admin',
    'Tornadoes',
    true,
    false,
    0,
    now(),
    now(),
    0
);

-- Assigner ROLE_ADMIN à l'admin
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'admin';
