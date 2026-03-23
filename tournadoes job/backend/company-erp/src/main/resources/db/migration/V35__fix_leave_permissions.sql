-- V35: Fix leave permissions to resolve 403 errors on leave endpoints
-- Creates/ensures leave permissions exist and assigns to all relevant roles

-- Ensure all required leave permissions exist
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'leave:read', 'View leave requests and balances'),
    (gen_random_uuid(), 'leave:request', 'Create leave requests'),
    (gen_random_uuid(), 'leave:approve', 'Approve/reject leave requests')
ON CONFLICT (name) DO NOTHING;

-- Get permission IDs
DO $$
DECLARE
    read_id UUID;
    request_id UUID;
    approve_id UUID;
BEGIN
    SELECT id INTO read_id FROM permissions WHERE name = 'leave:read';
    SELECT id INTO request_id FROM permissions WHERE name = 'leave:request';
    SELECT id INTO approve_id FROM permissions WHERE name = 'leave:approve';
    
    RAISE NOTICE 'leave:read ID: %, leave:request ID: %, leave:approve ID: %', read_id, request_id, approve_id;
END $$;

-- Ensure all relevant roles exist (matching CustomPermissionEvaluator LEAVE_*_ROLES)
INSERT INTO roles (id, name, description) VALUES
    (gen_random_uuid(), 'ROLE_ADMIN', 'Full system administrator'),
    (gen_random_uuid(), 'ROLE_HR_MANAGER', 'HR department manager'),
    (gen_random_uuid(), 'ROLE_MANAGER', 'Department manager'),
    (gen_random_uuid(), 'ROLE_EMPLOYEE', 'Regular employee'),
    (gen_random_uuid(), 'ROLE_FINANCE', 'Finance department'),
    (gen_random_uuid(), 'ROLE_USER', 'General system user'),
    (gen_random_uuid(), 'ROLE_TEACHER', 'School/education module user')
ON CONFLICT (name) DO NOTHING;

-- Clear existing leave permission assignments for clean slate (excluding admin)
DELETE FROM role_permissions 
WHERE permission_id IN (
    SELECT id FROM permissions WHERE name IN ('leave:read', 'leave:request', 'leave:approve')
)
AND role_id NOT IN (SELECT id FROM roles WHERE name = 'ROLE_ADMIN');

-- Assign leave:read to all view roles (ROLE_ADMIN, ROLE_HR_MANAGER, ROLE_MANAGER, ROLE_EMPLOYEE, ROLE_FINANCE, ROLE_USER, ROLE_TEACHER)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE p.name = 'leave:read'
AND r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER', 'ROLE_TEACHER')
ON CONFLICT DO NOTHING;

-- Assign leave:request to request roles (same as view roles)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE p.name = 'leave:request'
AND r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER', 'ROLE_EMPLOYEE', 'ROLE_FINANCE', 'ROLE_USER', 'ROLE_TEACHER')
ON CONFLICT DO NOTHING;

-- Assign leave:approve to approve roles (ROLE_ADMIN, ROLE_HR_MANAGER, ROLE_MANAGER)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE p.name = 'leave:approve'
AND r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_MANAGER')
ON CONFLICT DO NOTHING;

-- Verify final state
DO $$
DECLARE
    perm_name TEXT;
    role_name TEXT;
BEGIN
    RAISE NOTICE 'FINAL STATE - Leave permissions:';
    FOR perm_name, role_name IN
        SELECT p.name, r.name
        FROM permissions p
        JOIN role_permissions rp ON rp.permission_id = p.id
        JOIN roles r ON r.id = rp.role_id
        WHERE p.name LIKE 'leave:%'
        ORDER BY p.name, r.name
    LOOP
        RAISE NOTICE '  % → %', perm_name, role_name;
    END LOOP;
END $$;

