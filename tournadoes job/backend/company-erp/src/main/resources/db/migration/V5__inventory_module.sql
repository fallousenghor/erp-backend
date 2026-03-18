-- V6: Inventory module — assets, asset_assignments

-- ─── ASSETS ───────────────────────────────────────────────────────────────────
CREATE TABLE assets (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    asset_code      VARCHAR(20)   NOT NULL,
    name            VARCHAR(200)  NOT NULL,
    description     VARCHAR(500),
    category        VARCHAR(30)   NOT NULL,
    status          VARCHAR(30)   NOT NULL DEFAULT 'AVAILABLE',
    condition_state VARCHAR(20)   NOT NULL DEFAULT 'NEW',
    purchase_date   DATE,
    purchase_price  NUMERIC(15,2),
    serial_number   VARCHAR(100),
    brand           VARCHAR(100),
    model           VARCHAR(100),
    location        VARCHAR(200),
    department_id   UUID,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_assets     PRIMARY KEY (id),
    CONSTRAINT uk_asset_code UNIQUE (asset_code)
);

CREATE INDEX idx_asset_status   ON assets(status);
CREATE INDEX idx_asset_category ON assets(category);
CREATE INDEX idx_asset_dept     ON assets(department_id);

-- ─── ASSET_ASSIGNMENTS ────────────────────────────────────────────────────────
CREATE TABLE asset_assignments (
    id            UUID         NOT NULL DEFAULT gen_random_uuid(),
    asset_id      UUID         NOT NULL,
    employee_id   UUID         NOT NULL,
    employee_name VARCHAR(160) NOT NULL,
    assigned_date DATE         NOT NULL,
    returned_date DATE,
    active        BOOLEAN      NOT NULL DEFAULT true,
    notes         VARCHAR(500),
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now(),
    created_by    VARCHAR(100),
    updated_by    VARCHAR(100),
    version       BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_asset_assignments PRIMARY KEY (id),
    CONSTRAINT fk_assign_asset      FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
);

CREATE INDEX idx_assignment_asset    ON asset_assignments(asset_id);
CREATE INDEX idx_assignment_employee ON asset_assignments(employee_id);
CREATE INDEX idx_assignment_active   ON asset_assignments(asset_id, active) WHERE active = true;

