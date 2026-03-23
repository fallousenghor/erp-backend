-- V34: Debug and fix attendance permissions
-- This migration logs current permission state and ensures attendance:read works

-- First, let's check which roles have attendance:read permission
DO $$ 
DECLARE 
    r_name VARCHAR(50);
    p_id UUID;
BEGIN
    -- Get attendance:read permission ID
    SELECT id INTO p_id FROM permissions WHERE name = 'attendance:read';
    
    RAISE NOTICE 'attendance:read permission ID: %', p_id;
    
    -- Log each role that has attendance:read
    FOR r_name IN 
        SELECT r.name 
        FROM roles r 
        JOIN role_permissions rp ON rp.role_id = r.id 
        WHERE rp.permission_id = p_id
    LOOP
        RAISE NOTICE 'Role with attendance:read: %', r_name;
    END LOOP;
END $$;

-- Verify the admin user's roles
DO $$
DECLARE 
    u_id UUID;
    r_name VARCHAR(50);
BEGIN
    SELECT id INTO u_id FROM users WHERE username = 'admin';
    RAISE NOTICE 'Admin user ID: %', u_id;
    
    FOR r_name IN 
        SELECT r.name 
        FROM roles r 
        JOIN user_roles ur ON ur.role_id = r.id 
        WHERE ur.user_id = u_id
    LOOP
        RAISE NOTICE 'Admin role: %', r_name;
    END LOOP;
END $$;

-- Ensure ROLE_MANAGER exists and has attendance:read
INSERT INTO roles (id, name, description) VALUES
    (gen_random_uuid(), 'ROLE_MANAGER', 'Manager role for employees')
ON CONFLICT (name) DO NOTHING;

-- Ensure attendance:read permission exists
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'attendance:read', 'View attendance records')
ON CONFLICT (name) DO NOTHING;

-- Now do a fresh assignment of attendance:read to all roles
DELETE FROM role_permissions 
WHERE permission_id = (SELECT id FROM permissions WHERE name = 'attendance:read')
AND role_id NOT IN (SELECT id FROM roles WHERE name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER', 'ROLE_TEACHER'));

-- Re-add all roles that should have attendance:read
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER', 'ROLE_TEACHER')
ON CONFLICT DO NOTHING;

-- Verify the final state
DO $$ 
DECLARE 
    r_name VARCHAR(50);
    p_id UUID;
BEGIN
    SELECT id INTO p_id FROM permissions WHERE name = 'attendance:read';
    
    RAISE NOTICE 'FINAL: Roles with attendance:read:';
    FOR r_name IN 
        SELECT r.name 
        FROM roles r 
        JOIN role_permissions rp ON rp.role_id = r.id 
        WHERE rp.permission_id = p_id
    LOOP
        RAISE NOTICE '  - %', r_name;
    END LOOP;
END $$;