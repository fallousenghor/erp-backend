-- V2: Auth module — users, roles, permissions, user_roles, role_permissions, refresh_tokens

-- ─── PERMISSIONS ─────────────────────────────────────────────────────────────
CREATE TABLE permissions (
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    name       VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at TIMESTAMP   NOT NULL DEFAULT now(),
    version    BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT pk_permissions PRIMARY KEY (id),
    CONSTRAINT uk_permission_name UNIQUE (name)
);

-- ─── ROLES ────────────────────────────────────────────────────────────────────
CREATE TABLE roles (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now(),
    version     BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_roles PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE (name)
);

-- ─── ROLE_PERMISSIONS ─────────────────────────────────────────────────────────
CREATE TABLE role_permissions (
    role_id       UUID NOT NULL,
    permission_id UUID NOT NULL,
    CONSTRAINT pk_role_permissions PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_rp_role       FOREIGN KEY (role_id)       REFERENCES roles(id)       ON DELETE CASCADE,
    CONSTRAINT fk_rp_permission FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- ─── USERS ────────────────────────────────────────────────────────────────────
CREATE TABLE users (
    id              UUID         NOT NULL DEFAULT gen_random_uuid(),
    username        VARCHAR(50)  NOT NULL,
    email           VARCHAR(150) NOT NULL,
    password        VARCHAR(255) NOT NULL,
    first_name      VARCHAR(80)  NOT NULL,
    last_name       VARCHAR(80)  NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT true,
    locked          BOOLEAN      NOT NULL DEFAULT false,
    failed_attempts INT          NOT NULL DEFAULT 0,
    lock_time       TIMESTAMP,
    last_login      TIMESTAMP,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_users          PRIMARY KEY (id),
    CONSTRAINT uk_user_username  UNIQUE (username),
    CONSTRAINT uk_user_email     UNIQUE (email)
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email    ON users(email);

-- ─── USER_ROLES ───────────────────────────────────────────────────────────────
CREATE TABLE user_roles (
    user_id UUID NOT NULL,
    role_id UUID NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- ─── REFRESH_TOKENS ───────────────────────────────────────────────────────────
CREATE TABLE refresh_tokens (
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

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);

-- ─── SEED DATA — Roles & Permissions ──────────────────────────────────────────
INSERT INTO roles (id, name, description) VALUES
    (gen_random_uuid(), 'ROLE_ADMIN',       'Full system access'),
    (gen_random_uuid(), 'ROLE_HR_MANAGER',  'Human Resources management'),
    (gen_random_uuid(), 'ROLE_FINANCE',     'Finance and invoicing'),
    (gen_random_uuid(), 'ROLE_TEACHER',     'Education module — teacher'),
    (gen_random_uuid(), 'ROLE_STUDENT',     'Education module — student'),
    (gen_random_uuid(), 'ROLE_USER',        'Default authenticated user');

INSERT INTO permissions (id, name, description) VALUES
    -- user
    (gen_random_uuid(), 'user:create',    'Create users'),
    (gen_random_uuid(), 'user:read',      'Read users'),
    (gen_random_uuid(), 'user:update',    'Update users'),
    (gen_random_uuid(), 'user:delete',    'Delete users'),
    -- department
    (gen_random_uuid(), 'department:create', 'Create departments'),
    (gen_random_uuid(), 'department:read',   'Read departments'),
    (gen_random_uuid(), 'department:update', 'Update departments'),
    (gen_random_uuid(), 'department:delete', 'Delete departments'),
    -- employee
    (gen_random_uuid(), 'employee:create', 'Create employees'),
    (gen_random_uuid(), 'employee:read',   'Read employees'),
    (gen_random_uuid(), 'employee:update', 'Update employees'),
    -- leave
    (gen_random_uuid(), 'leave:request',  'Submit leave requests'),
    (gen_random_uuid(), 'leave:approve',  'Approve leave requests'),
    -- invoice
    (gen_random_uuid(), 'invoice:create', 'Create invoices'),
    (gen_random_uuid(), 'invoice:read',   'Read invoices'),
    (gen_random_uuid(), 'invoice:update', 'Update invoices'),
    -- payment
    (gen_random_uuid(), 'payment:process', 'Process payments'),
    -- asset
    (gen_random_uuid(), 'asset:create', 'Create assets'),
    (gen_random_uuid(), 'asset:read',   'Read assets'),
    (gen_random_uuid(), 'asset:assign', 'Assign assets'),
    -- education
    (gen_random_uuid(), 'program:create',    'Create training programs'),
    (gen_random_uuid(), 'program:update',    'Update training programs'),
    (gen_random_uuid(), 'enrollment:manage', 'Manage enrollments'),
    (gen_random_uuid(), 'grade:record',      'Record grades'),
    (gen_random_uuid(), 'student:read',      'Read student data'),
    -- dashboard
    (gen_random_uuid(), 'dashboard:view', 'View dashboard statistics'),
    -- audit
    (gen_random_uuid(), 'audit:read', 'Read audit logs');


-- ─── SEED DATA — Role-Permission assignments ──────────────────────────────────
-- ROLE_ADMIN gets ALL permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ROLE_ADMIN';

-- ROLE_HR_MANAGER permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'employee:create', 'employee:read', 'employee:update',
    'leave:request', 'leave:approve',
    'department:read',
    'asset:read',
    'student:read',
    'dashboard:view'
)
WHERE r.name = 'ROLE_HR_MANAGER';

-- ROLE_FINANCE permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'invoice:create', 'invoice:read', 'invoice:update',
    'payment:process',
    'employee:read',
    'dashboard:view'
)
WHERE r.name = 'ROLE_FINANCE';

-- ROLE_TEACHER permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'student:read',
    'grade:record',
    'program:update'
)
WHERE r.name = 'ROLE_TEACHER';

-- ROLE_STUDENT permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'student:read'
)
WHERE r.name = 'ROLE_STUDENT';

-- ROLE_USER permissions (minimal)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN (
    'department:read',
    'employee:read',
    'leave:request'
)
WHERE r.name = 'ROLE_USER';

-- ─── SEED DATA — Default Admin User ────────────────────────────────────────────
-- Password: Admin@123 (BCrypt hash, strength 12)
-- CHANGE THIS IN PRODUCTION via the /v1/auth/change-password endpoint
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, locked, failed_attempts, created_at, updated_at, version)
VALUES (
    gen_random_uuid(),
    'admin',
    'admin@company.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'System',
    'Administrator',
    true,
    false,
    0,
    now(),
    now(),
    0
);

-- Assign ROLE_ADMIN to default admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'admin';
