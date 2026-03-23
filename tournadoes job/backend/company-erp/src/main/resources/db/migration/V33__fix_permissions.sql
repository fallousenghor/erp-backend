-- V33: Fix missing permissions for attendance and employee endpoints
-- This migration ensures all necessary permissions are assigned to roles

-- Ensure ROLE_EMPLOYEE exists
INSERT INTO roles (id, name, description) VALUES
    (gen_random_uuid(), 'ROLE_EMPLOYEE', 'Employee role')
ON CONFLICT (name) DO NOTHING;

-- Add missing employee:read permission (in case V1 didn't include it properly)
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'employee:read', 'Read employees')
ON CONFLICT (name) DO NOTHING;

-- Fix: The issue is that ROLE_ADMIN gets permissions in V1, but V25 was supposed to add more.
-- Let's ensure attendance:read is properly assigned to all roles that should have it

-- First, remove any existing attendance:read assignments to avoid conflicts
DELETE FROM role_permissions WHERE permission_id = (SELECT id FROM permissions WHERE name = 'attendance:read');

-- Now reassign attendance:read to all roles that need it
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE p.name = 'attendance:read'
AND r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER', 'ROLE_TEACHER')
ON CONFLICT DO NOTHING;

-- Also ensure employee:read assignments
DELETE FROM role_permissions WHERE permission_id = (SELECT id FROM permissions WHERE name = 'employee:read');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE p.name = 'employee:read'
AND r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER')
ON CONFLICT DO NOTHING;

-- Ensure leave:read is assigned to all roles that need it
DELETE FROM role_permissions WHERE permission_id = (SELECT id FROM permissions WHERE name = 'leave:read');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE p.name = 'leave:read'
AND r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER', 'ROLE_TEACHER')
ON CONFLICT DO NOTHING;

DO $$ BEGIN 
  RAISE NOTICE 'Fixed attendance, employee and leave permissions for all roles';
END $$;