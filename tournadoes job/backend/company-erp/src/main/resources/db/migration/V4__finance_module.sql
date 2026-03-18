-- V5: Finance module — invoices, invoice_items, payments, expenses

-- ─── INVOICES ─────────────────────────────────────────────────────────────────
CREATE TABLE invoices (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    invoice_number  VARCHAR(30)   NOT NULL,
    client_name     VARCHAR(200)  NOT NULL,
    client_email    VARCHAR(150),
    client_address  VARCHAR(500),
    issued_by_id    UUID,
    issued_by_name  VARCHAR(160),
    issue_date      DATE          NOT NULL,
    due_date        DATE          NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'DRAFT',
    currency        VARCHAR(3)    NOT NULL DEFAULT 'XOF',
    subtotal        NUMERIC(15,2) NOT NULL DEFAULT 0,
    tax_rate        NUMERIC(5,2)  NOT NULL DEFAULT 0,
    tax_amount      NUMERIC(15,2) NOT NULL DEFAULT 0,
    total           NUMERIC(15,2) NOT NULL DEFAULT 0,
    notes           VARCHAR(1000),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_invoices            PRIMARY KEY (id),
    CONSTRAINT uk_invoice_number      UNIQUE (invoice_number)
);

CREATE INDEX idx_invoice_status     ON invoices(status);
CREATE INDEX idx_invoice_client     ON invoices(client_name);
CREATE INDEX idx_invoice_issue_date ON invoices(issue_date);

-- ─── INVOICE_ITEMS ────────────────────────────────────────────────────────────
CREATE TABLE invoice_items (
    id          UUID          NOT NULL DEFAULT gen_random_uuid(),
    invoice_id  UUID          NOT NULL,
    description VARCHAR(500)  NOT NULL,
    quantity    INT           NOT NULL,
    unit_price  NUMERIC(15,2) NOT NULL,
    line_total  NUMERIC(15,2) NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP     NOT NULL DEFAULT now(),
    version     BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_invoice_items    PRIMARY KEY (id),
    CONSTRAINT fk_item_invoice     FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

-- ─── PAYMENTS ─────────────────────────────────────────────────────────────────
CREATE TABLE payments (
    id             UUID          NOT NULL DEFAULT gen_random_uuid(),
    invoice_id     UUID          NOT NULL,
    amount         NUMERIC(15,2) NOT NULL,
    currency       VARCHAR(3)    NOT NULL,
    payment_method VARCHAR(30)   NOT NULL,
    payment_date   DATE          NOT NULL,
    reference      VARCHAR(100),
    notes          VARCHAR(500),
    created_at     TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP     NOT NULL DEFAULT now(),
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    version        BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_payments     PRIMARY KEY (id),
    CONSTRAINT fk_pay_invoice  FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE
);

CREATE INDEX idx_payment_invoice ON payments(invoice_id);

-- ─── EXPENSES ─────────────────────────────────────────────────────────────────
CREATE TABLE expenses (
    id                 UUID          NOT NULL DEFAULT gen_random_uuid(),
    title              VARCHAR(200)  NOT NULL,
    description        VARCHAR(1000),
    category           VARCHAR(30)   NOT NULL,
    amount             NUMERIC(15,2) NOT NULL,
    currency           VARCHAR(3)    NOT NULL,
    expense_date       DATE          NOT NULL,
    status             VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
    submitted_by_id    UUID,
    submitted_by_name  VARCHAR(160),
    approved_by        VARCHAR(100),
    department_id      UUID,
    receipt_reference  VARCHAR(100),
    created_at         TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP     NOT NULL DEFAULT now(),
    created_by         VARCHAR(100),
    updated_by         VARCHAR(100),
    version            BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_expenses PRIMARY KEY (id)
);

CREATE INDEX idx_expense_status   ON expenses(status);
CREATE INDEX idx_expense_category ON expenses(category);
CREATE INDEX idx_expense_date     ON expenses(expense_date);

