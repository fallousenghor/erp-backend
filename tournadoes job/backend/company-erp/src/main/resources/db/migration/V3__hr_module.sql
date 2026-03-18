-- V4: HR module — employees, leave_requests, attendances

-- ─── EMPLOYEES ────────────────────────────────────────────────────────────────
CREATE TABLE employees (
    id                UUID          NOT NULL DEFAULT gen_random_uuid(),
    employee_number   VARCHAR(20)   NOT NULL,
    first_name        VARCHAR(80)   NOT NULL,
    last_name         VARCHAR(80)   NOT NULL,
    email             VARCHAR(150)  NOT NULL,
    phone             VARCHAR(20),
    birth_date        DATE,
    hire_date         DATE          NOT NULL,
    termination_date  DATE,
    status            VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    base_salary       NUMERIC(15,2),
    currency          VARCHAR(3),
    contract_type     VARCHAR(20),
    contract_start_date DATE,
    contract_end_date   DATE,
    department_id     UUID,
    department_name   VARCHAR(100),
    position_id       UUID,
    position_title    VARCHAR(100),
    leave_balance     INT           NOT NULL DEFAULT 20,
    created_at        TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP     NOT NULL DEFAULT now(),
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    version           BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_employees             PRIMARY KEY (id),
    CONSTRAINT uk_employee_number       UNIQUE (employee_number),
    CONSTRAINT uk_employee_email        UNIQUE (email)
);

CREATE INDEX idx_employee_department ON employees(department_id);
CREATE INDEX idx_employee_status     ON employees(status);
CREATE INDEX idx_employee_name       ON employees(last_name, first_name);

-- ─── LEAVE_REQUESTS ───────────────────────────────────────────────────────────
CREATE TABLE leave_requests (
    id               UUID         NOT NULL DEFAULT gen_random_uuid(),
    employee_id      UUID         NOT NULL,
    employee_name    VARCHAR(160) NOT NULL,
    leave_type       VARCHAR(20)  NOT NULL,
    start_date       DATE         NOT NULL,
    end_date         DATE         NOT NULL,
    days_requested   INT          NOT NULL,
    reason           VARCHAR(500),
    status           VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    approved_by      VARCHAR(100),
    rejection_reason VARCHAR(500),
    created_at       TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at       TIMESTAMP    NOT NULL DEFAULT now(),
    created_by       VARCHAR(100),
    updated_by       VARCHAR(100),
    version          BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_leave_requests PRIMARY KEY (id)
);

CREATE INDEX idx_leave_employee ON leave_requests(employee_id);
CREATE INDEX idx_leave_status   ON leave_requests(status);

-- ─── ATTENDANCES ──────────────────────────────────────────────────────────────
CREATE TABLE attendances (
    id              UUID      NOT NULL DEFAULT gen_random_uuid(),
    employee_id     UUID      NOT NULL,
    attendance_date DATE      NOT NULL,
    check_in        TIME,
    check_out       TIME,
    status          VARCHAR(20) NOT NULL DEFAULT 'PRESENT',
    notes           VARCHAR(500),
    created_at      TIMESTAMP NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP NOT NULL DEFAULT now(),
    version         BIGINT    NOT NULL DEFAULT 0,
    CONSTRAINT pk_attendances              PRIMARY KEY (id),
    CONSTRAINT uk_attendance_employee_date UNIQUE (employee_id, attendance_date)
);

CREATE INDEX idx_attendance_employee ON attendances(employee_id);
CREATE INDEX idx_attendance_date     ON attendances(attendance_date);

