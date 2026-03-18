-- V14: Add performance management permissions and missing roles

-- Add missing roles (referenced in CustomPermissionEvaluator but not in V2__auth_module.sql)
INSERT INTO roles (id, name, description) VALUES
    (gen_random_uuid(), 'ROLE_MANAGER',  'Manager role for employees'),
    (gen_random_uuid(), 'ROLE_EMPLOYEE', 'Employee role');

-- Add performance permissions
INSERT INTO permissions (id, name, description) VALUES
    (gen_random_uuid(), 'performance:view',   'View performance reviews and objectives'),
    (gen_random_uuid(), 'performance:create', 'Create performance reviews and objectives'),
    (gen_random_uuid(), 'performance:update', 'Update performance reviews and objectives');

-- Assign performance:view and management permissions to ROLE_HR_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('performance:view', 'performance:create', 'performance:update')
WHERE r.name = 'ROLE_HR_MANAGER';

-- Assign performance permissions to ROLE_MANAGER
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name IN ('performance:view', 'performance:create', 'performance:update')
WHERE r.name = 'ROLE_MANAGER';

-- Assign performance:view to ROLE_EMPLOYEE (can view their own performance)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'performance:view'
WHERE r.name = 'ROLE_EMPLOYEE';

-- Assign performance:view to ROLE_TEACHER (can view their own performance)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'performance:view'
WHERE r.name = 'ROLE_TEACHER';

-- Assign performance:view to ROLE_USER (basic users can view their own performance)
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.name = 'performance:view'
WHERE r.name = 'ROLE_USER';
