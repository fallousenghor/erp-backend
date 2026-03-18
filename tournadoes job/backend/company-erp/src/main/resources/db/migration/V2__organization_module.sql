-- V3: Organization module — departments, positions, department_heads

-- ─── DEPARTMENTS ─────────────────────────────────────────────────────────────
CREATE TABLE departments (
    id          UUID         NOT NULL DEFAULT gen_random_uuid(),
    name        VARCHAR(100) NOT NULL,
    code        VARCHAR(10)  NOT NULL,
    description VARCHAR(500),
    active      BOOLEAN      NOT NULL DEFAULT true,
    deleted     BOOLEAN      NOT NULL DEFAULT false,
    deleted_at  TIMESTAMP,
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now(),
    created_by  VARCHAR(100),
    updated_by  VARCHAR(100),
    version     BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_departments     PRIMARY KEY (id),
    CONSTRAINT uk_department_code UNIQUE (code)
);

CREATE INDEX idx_department_code   ON departments(code);
CREATE INDEX idx_department_active ON departments(active) WHERE deleted = false;

-- ─── POSITIONS ────────────────────────────────────────────────────────────────
CREATE TABLE positions (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    title         VARCHAR(100) NOT NULL,
    description   VARCHAR(500),
    min_salary    NUMERIC(15,2),
    max_salary    NUMERIC(15,2),
    active        BOOLEAN      NOT NULL DEFAULT true,
    department_id UUID         NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now(),
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    version       BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_positions            PRIMARY KEY (id),
    CONSTRAINT uk_position_title_dept  UNIQUE (title, department_id),
    CONSTRAINT fk_position_department  FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

CREATE INDEX idx_position_department ON positions(department_id);

-- ─── DEPARTMENT_HEADS ─────────────────────────────────────────────────────────
CREATE TABLE department_heads (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    department_id UUID         NOT NULL,
    employee_id   UUID         NOT NULL,
    employee_name VARCHAR(160) NOT NULL,
    start_date    DATE         NOT NULL,
    end_date      DATE,
    current       BOOLEAN      NOT NULL DEFAULT true,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now(),
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    version       BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_department_heads   PRIMARY KEY (id),
    CONSTRAINT fk_head_department    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE CASCADE
);

CREATE INDEX idx_dept_head_dept    ON department_heads(department_id);
CREATE INDEX idx_dept_head_current ON department_heads(department_id, current) WHERE current = true;

