-- V10: Dashboard views & Final seed data

-- ─── VUE: Dashboard Statistics ────────────────────────────────────────────────
-- Vue pour les statistiques générales du dashboard

CREATE OR REPLACE VIEW dashboard_stats AS
SELECT 
    'employees' as entity,
    COUNT(*) as total,
    COUNT(*) FILTER (WHERE status = 'ACTIVE') as active,
    COUNT(*) FILTER (WHERE status = 'SUSPENDED') as suspended,
    COUNT(*) FILTER (WHERE status = 'TERMINATED') as terminated
FROM employees
UNION ALL
SELECT 
    'invoices_client' as entity,
    COUNT(*) as total,
    COUNT(*) FILTER (WHERE status = 'PAID') as paid,
    COUNT(*) FILTER (WHERE status = 'SENT' OR status = 'PARTIAL') as pending,
    COUNT(*) FILTER (WHERE status = 'OVERDUE') as overdue
FROM invoices WHERE type = 'CLIENT'
UNION ALL
SELECT 
    'deals' as entity,
    COUNT(*) as total,
    COUNT(*) FILTER (WHERE status = 'WON') as won,
    COUNT(*) FILTER (WHERE status = 'OPEN') as open,
    COUNT(*) FILTER (WHERE status = 'LOST') as lost
FROM deals
UNION ALL
SELECT 
    'projects' as entity,
    COUNT(*) as total,
    COUNT(*) FILTER (WHERE status = 'IN_PROGRESS') as in_progress,
    COUNT(*) FILTER (WHERE status = 'PLANNING') as planning,
    COUNT(*) FILTER (WHERE status = 'COMPLETED') as completed
FROM projects
UNION ALL
SELECT 
    'students' as entity,
    COUNT(*) as total,
    COUNT(*) FILTER (WHERE status = 'ACTIVE') as active,
    COUNT(*) FILTER (WHERE status = 'GRADUATED') as graduated,
    0 as dropped
FROM students;

-- ─── VUE: Revenue Monthly ─────────────────────────────────────────────────────
CREATE OR REPLACE VIEW revenue_monthly AS
SELECT 
    DATE_TRUNC('month', issue_date) as month,
    SUM(total) as total_revenue,
    SUM(paid_amount) as paid_revenue,
    SUM(remaining) as pending_revenue
FROM invoices 
WHERE type = 'CLIENT'
GROUP BY DATE_TRUNC('month', issue_date)
ORDER BY month DESC
LIMIT 12;

-- ─── VUE: Cash Flow ───────────────────────────────────────────────────────────
CREATE OR REPLACE VIEW cash_flow AS
SELECT 
    DATE_TRUNC('month', payment_date) as month,
    SUM(amount) as inflow,
    0 as outflow
FROM payments
GROUP BY DATE_TRUNC('month', payment_date)
UNION ALL
SELECT 
    DATE_TRUNC('month', expense_date) as month,
    0 as inflow,
    SUM(amount) as outflow
FROM expenses
GROUP BY DATE_TRUNC('month', expense_date)
ORDER BY month DESC
LIMIT 12;

-- ─── SEED DATA — Utilisateurs de test ─────────────────────────────────────────
-- Mot de passe pour tous: Test123! (BCrypt hash)
-- Hash: $2a$12$XZq... (même hash pour tous les users de test)

INSERT INTO users (id, username, email, password, first_name, last_name, phone, department_id, position_title, enabled, locked, failed_attempts, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'hrmanager',
    'hr@tornadoesjob.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'Marie',
    'Kouassi',
    '0707070701',
    d.id,
    'Responsable RH',
    true,
    false,
    0,
    now(),
    now(),
    0
FROM departments d WHERE d.code = 'RH'
ON CONFLICT (username) DO NOTHING;

-- Assigner ROLE_HR_MANAGER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_HR_MANAGER'
WHERE u.username = 'hrmanager'
ON CONFLICT DO NOTHING;

INSERT INTO users (id, username, email, password, first_name, last_name, phone, department_id, position_title, enabled, locked, failed_attempts, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'finance',
    'finance@tornadoesjob.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'Jean',
    'Traoré',
    '0707070702',
    d.id,
    'Comptable Senior',
    true,
    false,
    0,
    now(),
    now(),
    0
FROM departments d WHERE d.code = 'FIN'
ON CONFLICT (username) DO NOTHING;

-- Assigner ROLE_FINANCE
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_FINANCE'
WHERE u.username = 'finance'
ON CONFLICT DO NOTHING;

INSERT INTO users (id, username, email, password, first_name, last_name, phone, department_id, position_title, enabled, locked, failed_attempts, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'manager',
    'manager@tornadoesjob.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'Aminata',
    'Diallo',
    '0707070703',
    d.id,
    'Chef de Projet',
    true,
    false,
    0,
    now(),
    now(),
    0
FROM departments d WHERE d.code = 'IT'
ON CONFLICT (username) DO NOTHING;

-- Assigner ROLE_MANAGER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_MANAGER'
WHERE u.username = 'manager'
ON CONFLICT DO NOTHING;

INSERT INTO users (id, username, email, password, first_name, last_name, phone, department_id, position_title, enabled, locked, failed_attempts, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'user',
    'user@tornadoesjob.com',
    '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
    'Utilisateur',
    'Test',
    '0707070704',
    NULL,
    'Employé',
    true,
    false,
    0,
    now(),
    now(),
    0
ON CONFLICT (username) DO NOTHING;

-- Assigner ROLE_USER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
JOIN roles r ON r.name = 'ROLE_USER'
WHERE u.username = 'user'
ON CONFLICT DO NOTHING;

-- ─── Résumé des données de démo ───────────────────────────────────────────────
-- Cette vue aide à vérifier que les données sont bien chargées

CREATE OR REPLACE VIEW seed_data_summary AS
SELECT 'Users' as entity, COUNT(*) as count FROM users
UNION ALL
SELECT 'Roles', COUNT(*) FROM roles
UNION ALL
SELECT 'Departments', COUNT(*) FROM departments
UNION ALL
SELECT 'Employees', COUNT(*) FROM employees
UNION ALL
SELECT 'Leave Requests', COUNT(*) FROM leave_requests
UNION ALL
SELECT 'Attendances', COUNT(*) FROM attendances
UNION ALL
SELECT 'Invoices', COUNT(*) FROM invoices
UNION ALL
SELECT 'Payments', COUNT(*) FROM payments
UNION ALL
SELECT 'Expenses', COUNT(*) FROM expenses
UNION ALL
SELECT 'Contacts', COUNT(*) FROM contacts
UNION ALL
SELECT 'Deals', COUNT(*) FROM deals
UNION ALL
SELECT 'Purchase Orders', COUNT(*) FROM purchase_orders
UNION ALL
SELECT 'Assets', COUNT(*) FROM assets
UNION ALL
SELECT 'Students', COUNT(*) FROM students
UNION ALL
SELECT 'Programs', COUNT(*) FROM training_programs
UNION ALL
SELECT 'Enrollments', COUNT(*) FROM enrollments
UNION ALL
SELECT 'Projects', COUNT(*) FROM projects
UNION ALL
SELECT 'Documents', COUNT(*) FROM documents;
