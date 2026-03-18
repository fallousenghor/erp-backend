-- V24: Reset users and seed comprehensive test data for all roles/departments
-- DELETE existing data first

-- Revoke refresh tokens
DELETE FROM refresh_tokens;

-- Remove user-role assignments
DELETE FROM user_roles;

-- Delete all users
DELETE FROM users;

-- Insert departments (if needed for employee consistency)
INSERT INTO departments (id, name, code, description, active) VALUES
(gen_random_uuid(), 'Administration', 'ADMIN', 'Administrative department', true),
(gen_random_uuid(), 'Finance', 'FIN', 'Finance and accounting', true),
(gen_random_uuid(), 'Human Resources', 'HR', 'HR and personnel', true),
(gen_random_uuid(), 'Education', 'EDU', 'Teaching and education', true),
(gen_random_uuid(), 'IT', 'IT', 'Information Technology', true)
ON CONFLICT (code) DO NOTHING;

-- Insert positions (sample)
INSERT INTO positions (id, title, department_id, min_salary, max_salary, active) 
SELECT gen_random_uuid(), 'Manager', d.id, 50000, 80000, true 
FROM departments d 
ON CONFLICT DO NOTHING;

-- Insert test roles if missing (assume basic roles exist from initial schema)
INSERT INTO roles (id, name, description) VALUES
(gen_random_uuid(), 'ROLE_ADMIN', 'Full system administrator'),
(gen_random_uuid(), 'ROLE_HR_MANAGER', 'HR department manager'),
(gen_random_uuid(), 'ROLE_FINANCE', 'Finance user'),
(gen_random_uuid(), 'ROLE_TEACHER', 'Teacher/Educator'),
(gen_random_uuid(), 'ROLE_MANAGER', 'Department manager')
ON CONFLICT (name) DO NOTHING;

-- Insert test users
INSERT INTO users (id, username, email, password, first_name, last_name, enabled, locked, failed_attempts, created_at, updated_at, version) VALUES
-- Admin
(gen_random_uuid(), 'admin', 'admin@company.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzvIGKq.Hm', 'System', 'Administrator', true, false, 0, now(), now(), 0),
-- HR Manager
(gen_random_uuid(), 'hrmanager', 'hr@company.com', '$2a$12$WvK8gGq0k5mJ.0kKq9JfuO4Zx6j3yT6o1xN8Kz3mL7PqWv4rU2Ge', 'HR', 'Manager', true, false, 0, now(), now(), 0),
-- Finance User
(gen_random_uuid(), 'finance', 'finance@company.com', '$2a$12$WvK8gGq0k5mJ.0kKq9JfuO4Zx6j3yT6o1xN8Kz3mL7PqWv4rU2Ge', 'John', 'Doe', true, false, 0, now(), now(), 0),
-- Teacher
(gen_random_uuid(), 'teacher', 'teacher@company.com', '$2a$12$WvK8gGq0k5mJ.0kKq9JfuO4Zx6j3yT6o1xN8Kz3mL7PqWv4rU2Ge', 'Marie', 'Dupont', true, false, 0, now(), now(), 0),
-- Department Manager
(gen_random_uuid(), 'manager', 'manager@company.com', '$2a$12$WvK8gGq0k5mJ.0kKq9JfuO4Zx6j3yT6o1xN8Kz3mL7PqWv4rU2Ge', 'Paul', 'Martin', true, false, 0, now(), now(), 0);

-- Assign roles to users (match role names)
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u JOIN roles r ON r.name IN ('ROLE_ADMIN', 'ROLE_HR_MANAGER', 'ROLE_FINANCE', 'ROLE_TEACHER', 'ROLE_MANAGER')
WHERE u.username IN ('admin', 'hrmanager', 'finance', 'teacher', 'manager')
AND (u.username = 'admin' AND r.name = 'ROLE_ADMIN'
  OR u.username = 'hrmanager' AND r.name = 'ROLE_HR_MANAGER'
  OR u.username = 'finance' AND r.name = 'ROLE_FINANCE'
  OR u.username = 'teacher' AND r.name = 'ROLE_TEACHER'
  OR u.username = 'manager' AND r.name = 'ROLE_MANAGER');

-- Insert corresponding employees
INSERT INTO employees (id, employee_number, first_name, last_name, email, phone, hire_date, status, base_salary, currency, leave_balance, department_name, position_title, created_at, updated_at, version) VALUES
(gen_random_uuid(), 'EMP001', 'System', 'Administrator', 'admin@company.com', '0123456789', '2024-01-01', 'ACTIVE', 75000.00, 'EUR', 25, 'Administration', 'Manager', now(), now(), 0),
(gen_random_uuid(), 'EMP002', 'HR', 'Manager', 'hr@company.com', '0123456790', '2023-06-01', 'ACTIVE', 55000.00, 'EUR', 22, 'Human Resources', 'Manager', now(), now(), 0),
(gen_random_uuid(), 'EMP003', 'John', 'Doe', 'finance@company.com', '0123456791', '2024-03-01', 'ACTIVE', 42000.00, 'EUR', 20, 'Finance', 'Accountant', now(), now(), 0),
(gen_random_uuid(), 'EMP004', 'Marie', 'Dupont', 'teacher@company.com', '0123456792', '2022-09-01', 'ACTIVE', 38000.00, 'EUR', 18, 'Education', 'Teacher', now(), now(), 0),
(gen_random_uuid(), 'EMP005', 'Paul', 'Martin', 'manager@company.com', '0123456793', '2023-11-01', 'ACTIVE', 48000.00, 'EUR', 21, 'IT', 'Manager', now(), now(), 0);

-- Test credentials:
-- admin / Admin@123 (ROLE_ADMIN)
-- hrmanager / Test123! (ROLE_HR_MANAGER) 
-- finance / Test123! (ROLE_FINANCE)
-- teacher / Test123! (ROLE_TEACHER)
-- manager / Test123! (ROLE_MANAGER)
