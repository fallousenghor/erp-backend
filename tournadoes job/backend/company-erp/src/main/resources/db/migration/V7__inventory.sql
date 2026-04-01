-- V7: Inventory — assets, asset_assignments

-- ─── ASSETS ───────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS assets (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    asset_code      VARCHAR(30)   NOT NULL,
    name            VARCHAR(200)  NOT NULL,
    description     VARCHAR(500),
    category        VARCHAR(50)   NOT NULL,  -- ELECTRONIC, FURNITURE, VEHICLE, SOFTWARE, OTHER
    type            VARCHAR(50),   -- Laptop, Phone, Desk, Car, etc.
    brand           VARCHAR(100),
    model           VARCHAR(100),
    serial_number   VARCHAR(100),
    purchase_date   DATE,
    purchase_price  NUMERIC(15,2),
    supplier_id     UUID,
    supplier_name   VARCHAR(200),
    warranty_until  DATE,
    status          VARCHAR(20)   NOT NULL DEFAULT 'AVAILABLE',  -- AVAILABLE, ASSIGNED, MAINTENANCE, BROKEN, DISPOSED
    condition       VARCHAR(20)   DEFAULT 'GOOD',  -- NEW, GOOD, FAIR, POOR
    location        VARCHAR(200),
    assigned_to_id  UUID,
    assigned_to_name VARCHAR(160),
    department_id   UUID,
    department_name VARCHAR(100),
    notes           VARCHAR(1000),
    photo_url       VARCHAR(500),
    qr_code_url     VARCHAR(500),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_assets            PRIMARY KEY (id),
    CONSTRAINT uk_asset_code        UNIQUE (asset_code),
    CONSTRAINT uk_asset_serial      UNIQUE (serial_number)
);

CREATE INDEX IF NOT EXISTS idx_assets_category   ON assets(category);
CREATE INDEX IF NOT EXISTS idx_assets_status     ON assets(status);
CREATE INDEX IF NOT EXISTS idx_assets_assigned   ON assets(assigned_to_id);

-- ─── ASSET_ASSIGNMENTS (Historique des affectations) ─────────────────────────
CREATE TABLE IF NOT EXISTS asset_assignments (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    asset_id        UUID          NOT NULL,
    asset_name      VARCHAR(200)  NOT NULL,
    assigned_to_id  UUID          NOT NULL,
    assigned_to_name VARCHAR(160) NOT NULL,
    assigned_to_type VARCHAR(20)  NOT NULL,  -- EMPLOYEE, DEPARTMENT
    department_id   UUID,
    department_name VARCHAR(100),
    assignment_date DATE          NOT NULL,
    return_date     DATE,
    reason          VARCHAR(500),
    condition_at_assignment  VARCHAR(20) DEFAULT 'GOOD',
    condition_at_return    VARCHAR(20),
    notes           VARCHAR(1000),
    assigned_by_id  UUID,
    assigned_by_name VARCHAR(160),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_asset_assignments PRIMARY KEY (id),
    CONSTRAINT fk_assign_asset      FOREIGN KEY (asset_id) REFERENCES assets(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_assignment_asset    ON asset_assignments(asset_id);
CREATE INDEX IF NOT EXISTS idx_assignment_employee ON asset_assignments(assigned_to_id);

-- ─── SEED DATA — Actifs de démo ───────────────────────────────────────────────
INSERT INTO assets (id, asset_code, name, description, category, type, brand, model, serial_number, purchase_date, purchase_price, supplier_name, status, condition, location, created_at, updated_at, version)
VALUES 
    (gen_random_uuid(), 'AST-IT-001', 'MacBook Pro 16"', 'Laptop pour développeurs', 'ELECTRONIC', 'Laptop', 'Apple', 'MacBook Pro 16" M2', 'SN-APPLE-001', '2023-06-01', 1500000, 'Apple Reseller', 'ASSIGNED', 'GOOD', 'Bureau 201', now(), now(), 0),
    (gen_random_uuid(), 'AST-IT-002', 'Dell Latitude 5520', 'Laptop standard', 'ELECTRONIC', 'Laptop', 'Dell', 'Latitude 5520', 'SN-DELL-002', '2023-08-15', 500000, 'Dell Technologies', 'AVAILABLE', 'NEW', 'Stock IT', now(), now(), 0),
    (gen_random_uuid(), 'AST-IT-003', 'iPhone 14 Pro', 'Téléphone pour commerciaux', 'ELECTRONIC', 'Phone', 'Apple', 'iPhone 14 Pro', 'SN-APPLE-003', '2023-09-01', 800000, 'Apple Reseller', 'ASSIGNED', 'GOOD', 'Commercial', now(), now(), 0),
    (gen_random_uuid(), 'AST-FURN-001', 'Bureau ergonomique', 'Bureau assis-debout', 'FURNITURE', 'Desk', 'IKEA', 'Bekant', 'SN-IKEA-001', '2023-05-01', 250000, 'IKEA', 'ASSIGNED', 'GOOD', 'Open Space', now(), now(), 0),
    (gen_random_uuid(), 'AST-VEH-001', 'Toyota RAV4', 'Véhicule de service', 'VEHICLE', 'Car', 'Toyota', 'RAV4 2022', 'SN-TOYOTA-001', '2022-01-15', 15000000, 'Toyota CI', 'AVAILABLE', 'GOOD', 'Parking', now(), now(), 0);

-- Assignation d'actifs aux employés de démo
INSERT INTO asset_assignments (id, asset_id, asset_name, assigned_to_id, assigned_to_name, assigned_to_type, assignment_date, condition_at_assignment, assigned_by_name, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    a.id,
    a.name,
    e.id,
    e.first_name || ' ' || e.last_name,
    'EMPLOYEE',
    '2023-06-01',
    'GOOD',
    'Responsable IT',
    now(),
    now(),
    0
FROM assets a, employees e 
WHERE a.asset_code = 'AST-IT-001' AND e.employee_number = 'EMP-003';

INSERT INTO asset_assignments (id, asset_id, asset_name, assigned_to_id, assigned_to_name, assigned_to_type, assignment_date, condition_at_assignment, assigned_by_name, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    a.id,
    a.name,
    e.id,
    e.first_name || ' ' || e.last_name,
    'EMPLOYEE',
    '2023-05-01',
    'GOOD',
    'Responsable IT',
    now(),
    now(),
    0
FROM assets a, employees e 
WHERE a.asset_code = 'AST-FURN-001' AND e.employee_number = 'EMP-001';
