-- V25: Add missing permissions for attendance and leave modules
-- These permissions are required by CustomPermissionEvaluator but don't exist in the database

-- Add missing attendance permissions (one at a time for proper ON CONFLICT handling)
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'attendance:read', 'View attendance records')
ON CONFLICT (name) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'attendance:create', 'Create attendance records')
ON CONFLICT (name) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'attendance:update', 'Update attendance records')
ON CONFLICT (name) DO NOTHING;

INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'attendance:delete', 'Delete attendance records')
ON CONFLICT (name) DO NOTHING;

-- Add missing leave:read permission
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'leave:read', 'View leave requests')
ON CONFLICT (name) DO NOTHING;

-- Add missing employee:read permission
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'employee:read', 'Read employees')
ON CONFLICT (name) DO NOTHING;

-- Assign all roles that should have attendance:read
-- ROLE_ADMIN (should already have from V1, but ensure it's there)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

-- ROLE_HR_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_HR_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:create'
WHERE r.name = 'ROLE_HR_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:update'
WHERE r.name = 'ROLE_HR_MANAGER'
ON CONFLICT DO NOTHING;

-- ROLE_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:create'
WHERE r.name = 'ROLE_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:update'
WHERE r.name = 'ROLE_MANAGER'
ON CONFLICT DO NOTHING;

-- Assign leave:read to roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_HR_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_FINANCE'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_USER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_TEACHER'
ON CONFLICT DO NOTHING;

-- ROLE_EMPLOYEE gets leave:read (new)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'leave:read'
WHERE r.name = 'ROLE_EMPLOYEE'
ON CONFLICT DO NOTHING;

-- ROLE_USER gets attendance:read
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_USER'
ON CONFLICT DO NOTHING;

-- ROLE_FINANCE gets attendance:read
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_FINANCE'
ON CONFLICT DO NOTHING;

-- ROLE_TEACHER gets attendance:read
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_TEACHER'
ON CONFLICT DO NOTHING;

-- ROLE_EMPLOYEE gets attendance:read (new)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'attendance:read'
WHERE r.name = 'ROLE_EMPLOYEE'
ON CONFLICT DO NOTHING;

-- Also ensure employee:read is assigned to roles
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'employee:read'
WHERE r.name = 'ROLE_ADMIN'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'employee:read'
WHERE r.name = 'ROLE_HR_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'employee:read'
WHERE r.name = 'ROLE_MANAGER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'employee:read'
WHERE r.name = 'ROLE_FINANCE'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'employee:read'
WHERE r.name = 'ROLE_USER'
ON CONFLICT DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'employee:read'
WHERE r.name = 'ROLE_EMPLOYEE'
ON CONFLICT DO NOTHING;