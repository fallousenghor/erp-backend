-- V31: Verify and reset admin password
-- This migration ensures admin user exists with correct password

-- Delete existing admin if exists
DELETE FROM user_roles WHERE user_id IN (SELECT id FROM users WHERE username = 'admin');
DELETE FROM users WHERE username = 'admin';

-- Insert admin user with password hash for 'Admin@123' (BCrypt12)
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, locked, failed_attempts, created_at, updated_at, version)
VALUES (
    gen_random_uuid(),
    'admin',
    'admin@company.com',
    '$2a$12$M6Dhq1mJUzN3MnHygNo0lOiYVqNMYdfFFoaCF/kcTjn6VgZ.oRBfm',
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

DO $$ BEGIN 
  RAISE NOTICE 'Admin user verified/reset. Password: Admin@123';
END $$;
