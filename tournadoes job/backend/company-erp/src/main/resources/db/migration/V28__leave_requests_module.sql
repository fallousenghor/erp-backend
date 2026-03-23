-- V28: Complete Leave Requests Module — leave_requests table for RH Congés sub-module
-- Supports leave requests, approvals, balances, stats

-- ─── DROP OLD TABLE IF EXISTS (schema changed from V3) ────────────────────────
DROP TABLE IF EXISTS leave_requests CASCADE;

-- ─── LEAVE_REQUESTS ──────────────────────────────────────────────────────────
CREATE TABLE leave_requests (
    id                UUID         NOT NULL DEFAULT gen_random_uuid(),
    version           BIGINT       NOT NULL DEFAULT 0,
    
    -- Employee
    employee_id       UUID         NOT NULL,
    employee_number   VARCHAR(50)  NOT NULL,
    employee_name     VARCHAR(160) NOT NULL,
    department_id     UUID,
    department_name   VARCHAR(100),
    
    -- Leave details
    leave_type        VARCHAR(30)  NOT NULL, -- ANNUAL, SICK, MATERNITY, UNPAID, EXCEPTIONAL
    start_date        DATE         NOT NULL,
    end_date          DATE         NOT NULL,
    days_requested    INTEGER      NOT NULL CHECK (days_requested > 0),
    
    -- Request info
    reason            TEXT         NOT NULL,
    status            VARCHAR(20)  NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED')),
    
    -- Workflow
    approved_by       UUID,
    approved_by_name  VARCHAR(160),
    approved_at       TIMESTAMP,
    reject_reason     TEXT,
    
    created_at        TIMESTAMP    NOT NULL DEFAULT now(),
    created_by        UUID,
    updated_at        TIMESTAMP    NOT NULL DEFAULT now(),
    updated_by        UUID,
    
    CONSTRAINT pk_leave_requests PRIMARY KEY (id),
    CONSTRAINT uk_leave_employee_period UNIQUE (employee_id, leave_type, start_date, end_date),
    CONSTRAINT fk_leave_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_leave_employee_id ON leave_requests(employee_id);
CREATE INDEX IF NOT EXISTS idx_leave_status ON leave_requests(status);
CREATE INDEX IF NOT EXISTS idx_leave_dates ON leave_requests(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_leave_type_status ON leave_requests(leave_type, status);
CREATE INDEX IF NOT EXISTS idx_leave_employee_status ON leave_requests(employee_id, status);
CREATE INDEX IF NOT EXISTS idx_leave_created_at ON leave_requests(created_at DESC);

-- ─── VIEWS FOR STATS ─────────────────────────────────────────────────────────
-- Leave balances view (simplified - real balances would integrate with policy)
CREATE OR REPLACE VIEW leave_balances AS
SELECT 
    lr.employee_id,
    e.first_name || ' ' || e.last_name as employee_name,
    lr.department_name,
    COUNT(*) FILTER (WHERE lr.status = 'APPROVED' AND lr.leave_type = 'ANNUAL') as annual_used,
    COUNT(*) FILTER (WHERE lr.status = 'APPROVED' AND lr.leave_type = 'SICK') as sick_used,
    -- Default policy totals (configure per company)
    24 as annual_total,
    10 as sick_total,
    14 as maternity_total,
    30 as unpaid_total,
    5 as exceptional_total
FROM leave_requests lr
JOIN employees e ON lr.employee_id = e.id
GROUP BY lr.employee_id, e.first_name, e.last_name, lr.department_name;

-- ─── SEED TEST DATA ─────────────────────────────────────────────────────────
INSERT INTO leave_requests (employee_id, employee_number, employee_name, department_id, department_name, leave_type, start_date, end_date, days_requested, reason, status)
SELECT 
    e.id, e.employee_number, e.first_name || ' ' || e.last_name, 
    e.department_id, e.department_name,
    CASE 
        WHEN random() > 0.6 THEN 'ANNUAL'
        WHEN random() > 0.4 THEN 'SICK'
        WHEN random() > 0.35 THEN 'UNPAID'
        WHEN random() > 0.3 THEN 'EXCEPTIONAL'
        ELSE 'MATERNITY'
    END as leave_type,
    CURRENT_DATE + ((random() * 90)::integer) as start_date,
    CURRENT_DATE + ((random() * 90)::integer + 1 + (random() * 10)::integer) as end_date,
    1 + (random() * 14)::integer as days_requested,
    CASE 
        WHEN random() > 0.7 THEN 'Congés annuels familiaux'
        WHEN random() > 0.5 THEN 'Maladie légère'
        WHEN random() > 0.3 THEN 'Sans solde formation'
        ELSE 'Congés exceptionnels'
    END as reason,
    CASE 
        WHEN random() > 0.7 THEN 'APPROVED'
        WHEN random() > 0.5 THEN 'REJECTED'
        ELSE 'PENDING'
    END as status
FROM employees e 
WHERE e.status = 'ACTIVE'
LIMIT 150
ON CONFLICT DO NOTHING;

-- Some approvals (using subquery to update limited rows)
UPDATE leave_requests 
SET 
    approved_by = (SELECT id FROM users WHERE username = 'hrmanager' LIMIT 1),
    approved_by_name = 'HR Manager',
    approved_at = created_at + '1 day'::interval,
    status = 'APPROVED'
WHERE id IN (
    SELECT id FROM leave_requests 
    WHERE status = 'PENDING' AND random() > 0.3 
    LIMIT 50
);

-- Verify data
SELECT 
    leave_type, status, COUNT(*) as count,
    AVG(days_requested) as avg_days,
    MIN(start_date) as earliest,
    MAX(end_date) as latest
FROM leave_requests 
GROUP BY leave_type, status 
ORDER BY count DESC;
