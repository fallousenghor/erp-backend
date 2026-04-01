-- V5: CRM — contacts (clients/fournisseurs unifiés), deals (opportunités)

-- ─── CONTACTS ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS contacts (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    type            VARCHAR(20)   NOT NULL,  -- CLIENT, FOURNISSEUR, PARTENAIRE, PROSPECT
    company_name    VARCHAR(200)  NOT NULL,
    contact_name    VARCHAR(160),  -- Personne principale
    email           VARCHAR(150),
    phone           VARCHAR(20),
    mobile          VARCHAR(20),
    website         VARCHAR(200),
    address         VARCHAR(500),
    city            VARCHAR(100),
    postal_code     VARCHAR(10),
    country         VARCHAR(50)   DEFAULT 'Côte d''Ivoire',
    industry        VARCHAR(100),  -- Secteur d'activité
    company_size    VARCHAR(20),   -- 1-10, 11-50, 51-200, 200+
    tax_id          VARCHAR(50),   -- Numéro fiscal
    registration_id VARCHAR(50),   -- RCCM
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE, INACTIVE, BLACKLISTED
    priority        VARCHAR(10)   DEFAULT 'NORMAL',  -- LOW, NORMAL, HIGH, VIP
    credit_limit    NUMERIC(15,2) DEFAULT 0,
    payment_terms   VARCHAR(100),  -- Ex: "30 jours fin de mois"
    notes           VARCHAR(1000),
    tags            JSONB         DEFAULT '[]'::jsonb,  -- ["important", "récurrent"]
    custom_fields   JSONB         DEFAULT '{}'::jsonb,  -- Champs personnalisés
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_contacts        PRIMARY KEY (id),
    CONSTRAINT uk_contact_email   UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_contacts_type     ON contacts(type);
CREATE INDEX IF NOT EXISTS idx_contacts_status   ON contacts(status);
CREATE INDEX IF NOT EXISTS idx_contacts_priority ON contacts(priority);
CREATE INDEX IF NOT EXISTS idx_contacts_name     ON contacts(company_name);

-- ─── DEALS (Opportunités commerciales) ────────────────────────────────────────
CREATE TABLE IF NOT EXISTS deals (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    title           VARCHAR(200)  NOT NULL,
    contact_id      UUID          NOT NULL,
    contact_name    VARCHAR(200)  NOT NULL,
    amount          NUMERIC(15,2) NOT NULL,
    currency        VARCHAR(3)    NOT NULL DEFAULT 'XOF',
    probability     INT           NOT NULL DEFAULT 50,  -- 0 à 100
    stage           VARCHAR(30)   NOT NULL DEFAULT 'PROSPECTION',  -- PROSPECTION, QUALIFICATION, PROPOSITION, NEGOCIATION, GAGNE, PERDU
    source          VARCHAR(50),   -- WEBSITE, REFERRAL, LINKEDIN, SALON, PUBLICITE
    expected_close  DATE,
    actual_close    DATE,
    description     VARCHAR(1000),
    next_action     VARCHAR(200),  -- Prochaine action à faire
    next_action_date DATE,
    owner_id        UUID,
    owner_name      VARCHAR(160)  NOT NULL,
    status          VARCHAR(20)   NOT NULL DEFAULT 'OPEN',  -- OPEN, WON, LOST, ON_HOLD
    loss_reason     VARCHAR(500),
    notes           VARCHAR(1000),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_deals PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_deals_contact  ON deals(contact_id);
CREATE INDEX IF NOT EXISTS idx_deals_stage    ON deals(stage);
CREATE INDEX IF NOT EXISTS idx_deals_status   ON deals(status);
CREATE INDEX IF NOT EXISTS idx_deals_owner    ON deals(owner_id);

-- ─── SEED DATA — Contacts de démo ─────────────────────────────────────────────
INSERT INTO contacts (id, type, company_name, contact_name, email, phone, city, industry, status, priority, created_at, updated_at, version) VALUES
    (gen_random_uuid(), 'CLIENT', 'Orange Côte d''Ivoire', 'Patrick Somé', 'patrick.some@orange.ci', '0707070710', 'Abidjan', 'Télécommunications', 'ACTIVE', 'VIP', now(), now(), 0),
    (gen_random_uuid(), 'CLIENT', 'MTN Côte d''Ivoire', 'Aminata Sy', 'aminata.sy@mtn.ci', '0707070711', 'Abidjan', 'Télécommunications', 'ACTIVE', 'HIGH', now(), now(), 0),
    (gen_random_uuid(), 'CLIENT', 'Société Générale CI', 'Marc Kouamé', 'mar.kouame@socgen.ci', '0707070712', 'Abidjan', 'Banque', 'ACTIVE', 'NORMAL', now(), now(), 0),
    (gen_random_uuid(), 'FOURNISSEUR', 'Dell Technologies', 'Service Commercial', 'sales@dell.ci', '0707070720', 'Abidjan', 'Informatique', 'ACTIVE', 'NORMAL', now(), now(), 0),
    (gen_random_uuid(), 'FOURNISSEUR', 'AWS Africa', 'Cloud Sales', 'sales@aws.ci', '0707070721', 'Abidjan', 'Cloud', 'ACTIVE', 'NORMAL', now(), now(), 0),
    (gen_random_uuid(), 'PROSPECT', 'NSIA Banque', 'Direction IT', 'direction.it@nsia.ci', '0707070730', 'Abidjan', 'Banque', 'ACTIVE', 'HIGH', now(), now(), 0);

-- ─── SEED DATA — Deals de démo ────────────────────────────────────────────────
INSERT INTO deals (id, title, contact_id, contact_name, amount, currency, probability, stage, expected_close, owner_name, status, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'Modernisation système core banking',
    c.id,
    c.company_name,
    15000000,
    'XOF',
    75,
    'NEGOCIATION',
    '2024-04-30',
    'Commercial Senior',
    'OPEN',
    now(),
    now(),
    0
FROM contacts c WHERE c.company_name = 'NSIA Banque';

INSERT INTO deals (id, title, contact_id, contact_name, amount, currency, probability, stage, expected_close, owner_name, status, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    'Application mobile paiement',
    c.id,
    c.company_name,
    8000000,
    'XOF',
    40,
    'PROPOSITION',
    '2024-05-15',
    'Commercial Junior',
    'OPEN',
    now(),
    now(),
    0
FROM contacts c WHERE c.company_name = 'MTN Côte d''Ivoire';
