-- V27: Complete Attendance Module — attendance_records table with employee relations
-- Supports Presence sub-module: daily tracking, stats, presence rate calculations

-- ─── ATTENDANCE_RECORDS ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS attendance_records (
    id              UUID         NOT NULL DEFAULT gen_random_uuid(),
    version         BIGINT       NOT NULL DEFAULT 0,
    employee_id     UUID         NOT NULL,
    employee_number VARCHAR(50)  NOT NULL,
    employee_name   VARCHAR(160) NOT NULL,
    department_id   UUID,
    department_name VARCHAR(100),
    
    -- Date & Time
    record_date     DATE         NOT NULL,
    check_in_time   TIME,
    check_out_time  TIME,
    worked_hours    INTERVAL,    -- Example: '08:30:00'
    
    -- Status (PRESENT, ABSENT, LATE, ON_LEAVE, REMOTE)
    status          VARCHAR(20)  NOT NULL CHECK (status IN ('PRESENT', 'ABSENT', 'LATE', 'ON_LEAVE', 'REMOTE')),
    
    -- Late tracking
    late_minutes    INTEGER      DEFAULT 0 CHECK (late_minutes >= 0),
    
    -- Notes & Location
    notes           TEXT,
    location        VARCHAR(200),
    ip_address      VARCHAR(45), -- IPv4/IPv6
    device_info     VARCHAR(255),
    
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    created_by      UUID,
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_by      UUID,
    
    -- Constraints
    CONSTRAINT pk_attendance_records PRIMARY KEY (id),
    CONSTRAINT uk_attendance_unique UNIQUE (employee_id, record_date),
    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    
    -- Indexes for performance
    CONSTRAINT chk_status CHECK (status IN ('PRESENT', 'ABSENT', 'LATE', 'ON_LEAVE', 'REMOTE'))
);

CREATE INDEX IF NOT EXISTS idx_attendance_employee_date ON attendance_records(employee_id, record_date);
CREATE INDEX IF NOT EXISTS idx_attendance_date_status ON attendance_records(record_date, status);
CREATE INDEX IF NOT EXISTS idx_attendance_status ON attendance_records(status);
CREATE INDEX IF NOT EXISTS idx_attendance_employee_status ON attendance_records(employee_id, status);
CREATE INDEX IF NOT EXISTS idx_attendance_date ON attendance_records(record_date);
CREATE INDEX IF NOT EXISTS idx_attendance_department_date ON attendance_records(department_id, record_date);

COMMENT ON TABLE attendance_records IS 'Daily attendance tracking for RH Presence module';
COMMENT ON COLUMN attendance_records.status IS 'PRESENT/ABSENT/LATE/ON_LEAVE/REMOTE';
COMMENT ON COLUMN attendance_records.late_minutes IS 'Minutes late from expected check-in (08:00)';
COMMENT ON COLUMN attendance_records.worked_hours IS 'Actual hours worked (check_out - check_in)';

-- ─── SEED TEST DATA ────────────────────────────────────────────────────────────
INSERT INTO attendance_records (employee_id, employee_number, employee_name, department_id, department_name, record_date, status, late_minutes, notes)
SELECT 
    e.id, e.employee_number, e.first_name || ' ' || e.last_name, 
    e.department_id, e.department_name,
    CURRENT_DATE - (random() * 30)::integer as record_date,
    CASE 
        WHEN random() > 0.9 THEN 'ABSENT'
        WHEN random() > 0.8 THEN 'LATE'
        WHEN random() > 0.75 THEN 'ON_LEAVE'
        WHEN random() > 0.7 THEN 'REMOTE'
        ELSE 'PRESENT'
    END as status,
    CASE WHEN random() > 0.8 THEN (random() * 45)::integer ELSE 0 END as late_minutes,
    CASE 
        WHEN random() > 0.95 THEN 'Réunion externe'
        WHEN random() > 0.9 THEN 'Maladie'
        ELSE NULL
    END as notes
FROM employees e 
WHERE e.status = 'ACTIVE'
LIMIT 500
ON CONFLICT DO NOTHING;

-- Update worked_hours for PRESENT records (sample data)
UPDATE attendance_records 
SET 
    check_in_time = CASE 
        WHEN status = 'PRESENT' THEN ('08:00'::time + ((random() * 60)::integer || ' minutes')::interval)
        ELSE NULL 
    END,
    check_out_time = CASE 
        WHEN status = 'PRESENT' THEN ('17:00'::time + ((random() * 120)::integer || ' minutes')::interval)
        ELSE NULL 
    END,
    worked_hours = CASE 
        WHEN status = 'PRESENT' AND check_out_time > check_in_time 
        THEN check_out_time - check_in_time 
        ELSE NULL::interval
    END
WHERE status IN ('PRESENT', 'REMOTE')
AND random() > 0.2;

-- Verify data
SELECT 
    status, COUNT(*) as count,
    ROUND(AVG(late_minutes)::numeric, 1) as avg_late_min,
    AVG(EXTRACT(epoch FROM worked_hours)/3600) as avg_hours
FROM attendance_records 
GROUP BY status 
ORDER BY count DESC;
