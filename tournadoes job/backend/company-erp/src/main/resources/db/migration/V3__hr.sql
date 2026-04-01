-- V3: HR — employees, leave_requests, attendances

-- ─── EMPLOYEES ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS employees (
    id                  UUID          NOT NULL DEFAULT gen_random_uuid(),
    employee_number     VARCHAR(20)   NOT NULL,
    first_name          VARCHAR(80)   NOT NULL,
    last_name           VARCHAR(80)   NOT NULL,
    email               VARCHAR(150)  NOT NULL,
    phone               VARCHAR(20),
    birth_date          DATE,
    gender              VARCHAR(10),
    address             VARCHAR(500),
    city                VARCHAR(100),
    postal_code         VARCHAR(10),
    country             VARCHAR(50)   DEFAULT 'Côte d''Ivoire',
    photo_url           VARCHAR(500),
    qr_code_url         VARCHAR(500),
    hire_date           DATE          NOT NULL,
    termination_date    DATE,
    status              VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE, SUSPENDED, TERMINATED
    base_salary         NUMERIC(15,2),
    currency            VARCHAR(3)    DEFAULT 'XOF',
    contract_type       VARCHAR(20)   DEFAULT 'CDI',  -- CDI, CDD, STAGE, ALTERNANCE
    contract_start_date DATE,
    contract_end_date   DATE,
    department_id       UUID,
    department_name     VARCHAR(100),
    position_title      VARCHAR(100),
    manager_id          UUID,  -- N+1
    leave_balance       INT           NOT NULL DEFAULT 22,
    emergency_contact   VARCHAR(100),
    emergency_phone     VARCHAR(20),
    emergency_relation  VARCHAR(50),
    user_id             UUID,  -- Lien vers users si l'employé a un compte
    skills              JSONB       DEFAULT '[]'::jsonb,
    created_at          TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP     NOT NULL DEFAULT now(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_employees             PRIMARY KEY (id),
    CONSTRAINT uk_employee_number       UNIQUE (employee_number),
    CONSTRAINT uk_employee_email        UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_employee_department ON employees(department_id);
CREATE INDEX IF NOT EXISTS idx_employee_status      ON employees(status);
CREATE INDEX IF NOT EXISTS idx_employee_name        ON employees(last_name, first_name);

-- ─── LEAVE_REQUESTS ───────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS leave_requests (
    id               UUID          NOT NULL DEFAULT gen_random_uuid(),
    employee_id      UUID          NOT NULL,
    employee_name    VARCHAR(160)  NOT NULL,
    employee_number  VARCHAR(20),
    leave_type       VARCHAR(30)   NOT NULL,  -- PAID, UNPAID, SICK, MATERNITY, PATERNITY, SPECIAL
    start_date       DATE          NOT NULL,
    end_date         DATE          NOT NULL,
    days_requested   NUMERIC(5,1)  NOT NULL,
    reason           VARCHAR(1000),
    status           VARCHAR(20)   NOT NULL DEFAULT 'PENDING',  -- PENDING, APPROVED, REJECTED, CANCELLED
    approved_by_id   UUID,
    approved_by_name VARCHAR(160),
    approved_at      TIMESTAMP,
    rejection_reason VARCHAR(1000),
    attachment_url   VARCHAR(500),  -- Justificatif
    created_at       TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP     NOT NULL DEFAULT now(),
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    version          BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_leave_requests PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_leave_employee ON leave_requests(employee_id);
CREATE INDEX IF NOT EXISTS idx_leave_status   ON leave_requests(status);
CREATE INDEX IF NOT EXISTS idx_leave_dates    ON leave_requests(start_date, end_date);

-- ─── ATTENDANCES ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS attendances (
    id              UUID        NOT NULL DEFAULT gen_random_uuid(),
    employee_id     UUID        NOT NULL,
    employee_name   VARCHAR(160),
    attendance_date DATE        NOT NULL,
    check_in        TIME,
    check_out       TIME,
    worked_hours    NUMERIC(5,2),
    status          VARCHAR(20) NOT NULL DEFAULT 'PRESENT',  -- PRESENT, ABSENT, LATE, HALF_DAY, REMOTE
    notes           VARCHAR(500),
    location        VARCHAR(200),  -- Pour check-in à distance
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_attendances              PRIMARY KEY (id),
    CONSTRAINT uk_attendance_employee_date UNIQUE (employee_id, attendance_date)
);

CREATE INDEX IF NOT EXISTS idx_attendance_employee ON attendances(employee_id);
CREATE INDEX IF NOT EXISTS idx_attendance_date     ON attendances(attendance_date);

-- ─── SEED DATA — Employés de démo ─────────────────────────────────────────────
-- Employé 1: Responsable RH
INSERT INTO employees (id, employee_number, first_name, last_name, email, phone, hire_date, status, base_salary, department_id, department_name, position_title, leave_balance, created_at, updated_at, version)
VALUES (gen_random_uuid(), 'EMP-001', 'Marie', 'Kouassi', 'marie.kouassi@tornadoesjob.com', '0707070701', '2023-01-15', 'ACTIVE', 55000, 
        (SELECT id FROM departments WHERE code = 'RH'), 'Ressources Humaines', 'Responsable RH', 25, now(), now(), 0);

-- Employé 2: Comptable
INSERT INTO employees (id, employee_number, first_name, last_name, email, phone, hire_date, status, base_salary, department_id, department_name, position_title, leave_balance, created_at, updated_at, version)
VALUES (gen_random_uuid(), 'EMP-002', 'Jean', 'Traoré', 'jean.traore@tornadoesjob.com', '0707070702', '2023-03-01', 'ACTIVE', 45000,
        (SELECT id FROM departments WHERE code = 'FIN'), 'Finance & Comptabilité', 'Comptable Senior', 22, now(), now(), 0);

-- Employé 3: Développeur
INSERT INTO employees (id, employee_number, first_name, last_name, email, phone, hire_date, status, base_salary, department_id, department_name, position_title, leave_balance, created_at, updated_at, version)
VALUES (gen_random_uuid(), 'EMP-003', 'Aminata', 'Diallo', 'aminata.diallo@tornadoesjob.com', '0707070703', '2023-06-01', 'ACTIVE', 50000,
        (SELECT id FROM departments WHERE code = 'IT'), 'Informatique & Développement', 'Développeuse Full Stack', 22, now(), now(), 0);
