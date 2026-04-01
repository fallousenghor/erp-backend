-- V4: Finance — invoices (avec items JSON), payments, expenses
-- Simplification: invoice_items fusionné dans invoices via JSON, journal_entries supprimé

-- ─── INVOICES ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS invoices (
    id              UUID           NOT NULL DEFAULT gen_random_uuid(),
    invoice_number  VARCHAR(30)    NOT NULL,
    type            VARCHAR(10)    NOT NULL DEFAULT 'CLIENT',  -- CLIENT or FOURNISSEUR
    contact_id      UUID,  -- Référence à contacts
    contact_name    VARCHAR(200)   NOT NULL,
    contact_email   VARCHAR(150),
    contact_phone   VARCHAR(20),
    contact_address VARCHAR(500),
    issued_by_id    UUID,
    issued_by_name  VARCHAR(160),
    issue_date      DATE           NOT NULL,
    due_date        DATE           NOT NULL,
    status          VARCHAR(20)    NOT NULL DEFAULT 'DRAFT',  -- DRAFT, SENT, PAID, PARTIAL, OVERDUE, CANCELLED
    currency        VARCHAR(3)     NOT NULL DEFAULT 'XOF',
    subtotal        NUMERIC(15,2)  NOT NULL DEFAULT 0,
    tax_rate        NUMERIC(5,2)   NOT NULL DEFAULT 18,
    tax_amount      NUMERIC(15,2)  NOT NULL DEFAULT 0,
    discount_amount NUMERIC(15,2)  DEFAULT 0,
    total           NUMERIC(15,2)  NOT NULL DEFAULT 0,
    paid_amount     NUMERIC(15,2)  DEFAULT 0,
    remaining       NUMERIC(15,2)  DEFAULT 0,
    items           JSONB          NOT NULL DEFAULT '[]'::jsonb,  -- [{"description": "...", "quantity": 1, "unit_price": 1000, "total": 1000}]
    notes           VARCHAR(1000),
    terms           VARCHAR(500),
    attachment_url  VARCHAR(500),
    created_at      TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP      NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT         NOT NULL DEFAULT 0,
    CONSTRAINT pk_invoices            PRIMARY KEY (id),
    CONSTRAINT uk_invoice_number      UNIQUE (invoice_number)
);

CREATE INDEX IF NOT EXISTS idx_invoice_type      ON invoices(type);
CREATE INDEX IF NOT EXISTS idx_invoice_status    ON invoices(status);
CREATE INDEX IF NOT EXISTS idx_invoice_contact   ON invoices(contact_id);
CREATE INDEX IF NOT EXISTS idx_invoice_dates     ON invoices(issue_date, due_date);

-- ─── PAYMENTS ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS payments (
    id             UUID          NOT NULL DEFAULT gen_random_uuid(),
    invoice_id     UUID          NOT NULL,
    contact_id     UUID,
    amount         NUMERIC(15,2) NOT NULL,
    currency       VARCHAR(3)    NOT NULL DEFAULT 'XOF',
    payment_method VARCHAR(30)   NOT NULL,  -- CASH, BANK_TRANSFER, CHECK, MOBILE_MONEY, CARD
    payment_date   DATE          NOT NULL,
    reference      VARCHAR(100),  -- Référence de transaction
    notes          VARCHAR(500),
    attachment_url VARCHAR(500),  -- Preuve de paiement
    created_at     TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP     NOT NULL DEFAULT now(),
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    version        BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_payments     PRIMARY KEY (id),
    CONSTRAINT fk_pay_invoice  FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_payment_invoice ON payments(invoice_id);
CREATE INDEX IF NOT EXISTS idx_payment_date    ON payments(payment_date);

-- ─── EXPENSES ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS expenses (
    id                UUID          NOT NULL DEFAULT gen_random_uuid(),
    title             VARCHAR(200)  NOT NULL,
    description       VARCHAR(1000),
    category          VARCHAR(50)   NOT NULL,  -- TRANSPORT, MEAL, EQUIPMENT, TRAVEL, TRAINING, OTHER
    amount            NUMERIC(15,2) NOT NULL,
    currency          VARCHAR(3)    NOT NULL DEFAULT 'XOF',
    expense_date      DATE          NOT NULL,
    status            VARCHAR(20)   NOT NULL DEFAULT 'PENDING',  -- PENDING, APPROVED, REJECTED, PAID
    submitted_by_id   UUID,
    submitted_by_name VARCHAR(160),
    employee_id       UUID,
    approved_by_id    UUID,
    approved_by_name  VARCHAR(160),
    approved_at       TIMESTAMP,
    department_id     UUID,
    department_name   VARCHAR(100),
    payment_method    VARCHAR(30),  -- CASH, CARD, BANK_TRANSFER
    receipt_url       VARCHAR(500),
    notes             VARCHAR(500),
    created_at        TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at        TIMESTAMP     NOT NULL DEFAULT now(),
    created_by        VARCHAR(100),
    updated_by        VARCHAR(100),
    version           BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_expenses PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_expense_status    ON expenses(status);
CREATE INDEX IF NOT EXISTS idx_expense_category  ON expenses(category);
CREATE INDEX IF NOT EXISTS idx_expense_date      ON expenses(expense_date);
CREATE INDEX IF NOT EXISTS idx_expense_employee  ON expenses(employee_id);

-- ─── SEED DATA — Factures de démo ─────────────────────────────────────────────
INSERT INTO invoices (id, invoice_number, type, contact_name, contact_email, issue_date, due_date, status, currency, subtotal, tax_rate, tax_amount, total, paid_amount, remaining, items, created_at, updated_at, version)
VALUES 
    (gen_random_uuid(), 'FAC-2024-001', 'CLIENT', 'Orange CI', 'contact@orange.ci', '2024-01-15', '2024-02-15', 'PAID', 'XOF', 500000, 18, 90000, 590000, 590000, 0,
     '[{"description": "Développement application mobile", "quantity": 1, "unit_price": 500000, "total": 500000}]'::jsonb, now(), now(), 0),
    (gen_random_uuid(), 'FAC-2024-002', 'CLIENT', 'MTN CI', 'contact@mtn.ci', '2024-02-01', '2024-03-01', 'SENT', 'XOF', 750000, 18, 135000, 885000, 0, 885000,
     '[{"description": "Maintenance système", "quantity": 3, "unit_price": 250000, "total": 750000}]'::jsonb, now(), now(), 0),
    (gen_random_uuid(), 'FAC-2024-003', 'CLIENT', 'Société Générale', 'contact@socgen.ci', '2024-02-15', '2024-03-15', 'PARTIAL', 'XOF', 1200000, 18, 216000, 1416000, 500000, 916000,
     '[{"description": "Formation équipe IT", "quantity": 5, "unit_price": 240000, "total": 1200000}]'::jsonb, now(), now(), 0);

-- ─── SEED DATA — Dépenses de démo ─────────────────────────────────────────────
INSERT INTO expenses (id, title, description, category, amount, currency, expense_date, status, submitted_by_name, department_name, created_at, updated_at, version)
VALUES 
    (gen_random_uuid(), 'Achat matériel informatique', '3 laptops pour nouveaux développeurs', 'EQUIPMENT', 1500000, 'XOF', '2024-02-01', 'APPROVED', 'Marie Kouassi', 'Informatique & Développement', now(), now(), 0),
    (gen_random_uuid(), 'Déjeuner client', 'Repas avec prospect Orange CI', 'MEAL', 50000, 'XOF', '2024-02-10', 'PAID', 'Jean Traoré', 'Commercial & Marketing', now(), now(), 0),
    (gen_random_uuid(), 'Transport mission', 'Taxi pour réunion client', 'TRANSPORT', 15000, 'XOF', '2024-02-12', 'PENDING', 'Aminata Diallo', 'Informatique & Développement', now(), now(), 0);
