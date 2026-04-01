-- V6: Purchases — purchase_orders, purchase_order_items (via JSON dans purchase_orders)

-- ─── PURCHASE_ORDERS ──────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS purchase_orders (
    id                  UUID          NOT NULL DEFAULT gen_random_uuid(),
    po_number           VARCHAR(30)   NOT NULL,
    supplier_id         UUID          NOT NULL,
    supplier_name       VARCHAR(200)  NOT NULL,
    supplier_contact    VARCHAR(160),
    supplier_email      VARCHAR(150),
    supplier_phone      VARCHAR(20),
    order_date          DATE          NOT NULL,
    expected_delivery   DATE,
    actual_delivery     DATE,
    status              VARCHAR(20)   NOT NULL DEFAULT 'DRAFT',  -- DRAFT, PENDING, APPROVED, SENT, PARTIAL, RECEIVED, CANCELLED
    currency            VARCHAR(3)    NOT NULL DEFAULT 'XOF',
    subtotal            NUMERIC(15,2) NOT NULL DEFAULT 0,
    tax_rate            NUMERIC(5,2)  NOT NULL DEFAULT 18,
    tax_amount          NUMERIC(15,2) NOT NULL DEFAULT 0,
    discount_amount     NUMERIC(15,2) DEFAULT 0,
    shipping_cost       NUMERIC(15,2) DEFAULT 0,
    total               NUMERIC(15,2) NOT NULL DEFAULT 0,
    items               JSONB         NOT NULL DEFAULT '[]'::jsonb,  -- [{"product_name": "...", "quantity": 1, "unit_price": 1000, "total": 1000}]
    shipping_address    VARCHAR(500),
    notes               VARCHAR(1000),
    terms               VARCHAR(500),
    approved_by_id      UUID,
    approved_by_name    VARCHAR(160),
    approved_at         TIMESTAMP,
    receiver_id         UUID,
    receiver_name       VARCHAR(160),
    received_at         TIMESTAMP,
    invoice_reference   VARCHAR(100),  -- Référence facture fournisseur
    attachment_url      VARCHAR(500),
    created_at          TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP     NOT NULL DEFAULT now(),
    created_by          VARCHAR(100),
    updated_by          VARCHAR(100),
    version             BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_purchase_orders       PRIMARY KEY (id),
    CONSTRAINT uk_purchase_order_number UNIQUE (po_number)
);

CREATE INDEX IF NOT EXISTS idx_po_supplier  ON purchase_orders(supplier_id);
CREATE INDEX IF NOT EXISTS idx_po_status    ON purchase_orders(status);
CREATE INDEX IF NOT EXISTS idx_po_dates     ON purchase_orders(order_date, expected_delivery);

-- ─── SEED DATA — Commandes d'achat de démo ────────────────────────────────────
INSERT INTO purchase_orders (id, po_number, supplier_id, supplier_name, order_date, expected_delivery, status, currency, subtotal, tax_rate, tax_amount, total, items, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'PO-2024-001',
    c.id,
    c.company_name,
    '2024-02-01',
    '2024-02-15',
    'RECEIVED',
    'XOF',
    1000000,
    18,
    180000,
    1180000,
    '[{"product_name": "Laptop Dell Latitude 5520", "quantity": 3, "unit_price": 350000, "total": 1050000}, {"product_name": "Souris sans fil", "quantity": 5, "unit_price": 15000, "total": 75000}]'::jsonb,
    now(),
    now(),
    0
FROM contacts c WHERE c.company_name = 'Dell Technologies';

INSERT INTO purchase_orders (id, po_number, supplier_id, supplier_name, order_date, expected_delivery, status, currency, subtotal, tax_rate, tax_amount, total, items, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'PO-2024-002',
    c.id,
    c.company_name,
    '2024-02-10',
    '2024-03-10',
    'PENDING',
    'XOF',
    500000,
    18,
    90000,
    590000,
    '[{"product_name": "Serveur AWS EC2 - 1 an", "quantity": 1, "unit_price": 500000, "total": 500000}]'::jsonb,
    now(),
    now(),
    0
FROM contacts c WHERE c.company_name = 'AWS Africa';
