-- V9: Seed default admin user with correct password
-- Password: Admin@123 (BCrypt hash, strength 12)

-- First delete any existing admin user
DELETE FROM user_roles WHERE user_id IN (SELECT id FROM users WHERE username = 'admin');
DELETE FROM users WHERE username = 'admin';

-- Insert admin user with correct password hash for "Admin@123"
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, locked, failed_attempts, created_at, updated_at, version)
VALUES (
    gen_random_uuid(),
    'admin',
    'admin@company.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzvIGKq.Hm',  -- Admin@123
    'System',
    'Administrator',
    true,
    false,
    0,
    now(),
    now(),
    0
);

-- Assign ROLE_ADMIN to admin user
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_ADMIN'
WHERE u.username = 'admin';
