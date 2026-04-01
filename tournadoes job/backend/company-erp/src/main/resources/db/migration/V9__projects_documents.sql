-- V9: Projects & Documents

-- ─── PROJECTS ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS projects (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    project_code    VARCHAR(20)   NOT NULL,
    name            VARCHAR(200)  NOT NULL,
    description     VARCHAR(2000),
    type            VARCHAR(30),   -- INTERNAL, CLIENT, RESEARCH
    contact_id      UUID,  -- Client si projet client
    contact_name    VARCHAR(200),
    status          VARCHAR(20)   NOT NULL DEFAULT 'PLANNING',  -- PLANNING, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    priority        VARCHAR(10)   DEFAULT 'MEDIUM',  -- LOW, MEDIUM, HIGH, CRITICAL
    start_date      DATE,
    end_date        DATE,
    deadline        DATE,
    budget          NUMERIC(15,2),
    spent           NUMERIC(15,2) DEFAULT 0,
    currency        VARCHAR(3)    DEFAULT 'XOF',
    progress        INT           DEFAULT 0,  -- 0 à 100
    manager_id      UUID,
    manager_name    VARCHAR(160),
    team_members    JSONB         DEFAULT '[]'::jsonb,  -- [{"id": "...", "name": "...", "role": "..."}]
    milestones      JSONB         DEFAULT '[]'::jsonb,  -- [{"title": "...", "date": "...", "status": "..."}]
    risks           VARCHAR(1000),
    notes           VARCHAR(1000),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_projects          PRIMARY KEY (id),
    CONSTRAINT uk_project_code      UNIQUE (project_code)
);

CREATE INDEX IF NOT EXISTS idx_projects_status   ON projects(status);
CREATE INDEX IF NOT EXISTS idx_projects_priority ON projects(priority);
CREATE INDEX IF NOT EXISTS idx_projects_contact  ON projects(contact_id);

-- ─── DOCUMENTS ────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS documents (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    title           VARCHAR(200)  NOT NULL,
    description     VARCHAR(500),
    category        VARCHAR(50)   NOT NULL,  -- CONTRACT, INVOICE, REPORT, POLICY, CERTIFICATE, OTHER
    type            VARCHAR(50),
    file_name       VARCHAR(200)  NOT NULL,
    file_path       VARCHAR(500)  NOT NULL,
    file_url        VARCHAR(500)  NOT NULL,
    file_type       VARCHAR(20),   -- PDF, DOCX, XLSX, PNG, JPG, etc.
    file_size       BIGINT,        -- En bytes
    version         VARCHAR(20)   DEFAULT '1.0',
    status          VARCHAR(20)   NOT NULL DEFAULT 'DRAFT',  -- DRAFT, REVIEW, APPROVED, PUBLISHED, ARCHIVED
    tags            JSONB         DEFAULT '[]'::jsonb,
    related_type    VARCHAR(50),   -- EMPLOYEE, PROJECT, INVOICE, etc.
    related_id      UUID,
    related_name    VARCHAR(200),
    uploaded_by_id  UUID,
    uploaded_by_name VARCHAR(160) NOT NULL,
    approved_by_id  UUID,
    approved_by_name VARCHAR(160),
    approved_at     TIMESTAMP,
    expires_at      TIMESTAMP,
    is_public       BOOLEAN       DEFAULT false,
    download_count  INT           DEFAULT 0,
    notes           VARCHAR(1000),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version_num     BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_documents PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_documents_category ON documents(category);
CREATE INDEX IF NOT EXISTS idx_documents_status   ON documents(status);
CREATE INDEX IF NOT EXISTS idx_documents_related  ON documents(related_type, related_id);

-- ─── AUDIT_LOGS ───────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS audit_logs (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    action          VARCHAR(50)   NOT NULL,  -- CREATE, UPDATE, DELETE, LOGIN, LOGOUT, EXPORT
    entity_type     VARCHAR(50)   NOT NULL,  -- EMPLOYEE, INVOICE, PROJECT, etc.
    entity_id       UUID,
    entity_name     VARCHAR(200),
    user_id         UUID,
    user_name       VARCHAR(160),
    ip_address      VARCHAR(50),
    user_agent      VARCHAR(500),
    changes         JSONB,  -- {"field": {"old": "...", "new": "..."}}
    metadata        JSONB,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_audit_logs PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_audit_action    ON audit_logs(action);
CREATE INDEX IF NOT EXISTS idx_audit_entity    ON audit_logs(entity_type, entity_id);
CREATE INDEX IF NOT EXISTS idx_audit_user      ON audit_logs(user_id);
CREATE INDEX IF NOT EXISTS idx_audit_date      ON audit_logs(created_at);

-- ─── SEED DATA — Projets de démo ──────────────────────────────────────────────
INSERT INTO projects (id, project_code, name, description, type, contact_name, status, priority, start_date, deadline, budget, progress, manager_name, team_members, milestones, created_at, updated_at, version)
VALUES 
    (gen_random_uuid(), 'PRJ-2024-001', 'Refonte site web Orange CI', 'Développement d''un nouveau site web institutionnel', 'CLIENT', 'Orange Côte d''Ivoire', 'IN_PROGRESS', 'HIGH', '2024-01-15', '2024-04-30', 5000000, 60, 'Chef de Projet Senior',
     '[{"id": "emp-001", "name": "Aminata Diallo", "role": "Lead Dev"}, {"id": "emp-002", "name": "Jean Traoré", "role": "Backend Dev"}]'::jsonb,
     '[{"title": "Maquettes", "date": "2024-02-15", "status": "DONE"}, {"title": "Développement Frontend", "date": "2024-03-31", "status": "IN_PROGRESS"}, {"title": "Recette", "date": "2024-04-15", "status": "PENDING"}]'::jsonb,
     now(), now(), 0),
    (gen_random_uuid(), 'PRJ-2024-002', 'Application mobile interne', 'Application de gestion des congés et présences', 'INTERNAL', NULL, 'PLANNING', 'MEDIUM', '2024-03-01', '2024-06-30', 3000000, 10, 'Chef de Projet Junior',
     '[{"id": "emp-003", "name": "Marie Kouassi", "role": "Product Owner"}]'::jsonb,
     '[{"title": "Spécifications", "date": "2024-03-31", "status": "IN_PROGRESS"}, {"title": "Développement", "date": "2024-05-31", "status": "PENDING"}]'::jsonb,
     now(), now(), 0),
    (gen_random_uuid(), 'PRJ-2024-003', 'Migration cloud AWS', 'Migration de l''infrastructure vers AWS', 'CLIENT', 'Société Générale CI', 'IN_PROGRESS', 'CRITICAL', '2024-02-01', '2024-05-15', 8000000, 35, 'Chef de Projet Senior',
     '[{"id": "emp-004", "name": "Expert Cloud", "role": "Tech Lead"}]'::jsonb,
     '[{"title": "Audit infrastructure", "date": "2024-02-28", "status": "DONE"}, {"title": "Migration", "date": "2024-04-30", "status": "IN_PROGRESS"}]'::jsonb,
     now(), now(), 0);

-- ─── SEED DATA — Documents de démo ────────────────────────────────────────────
INSERT INTO documents (id, title, description, category, file_name, file_path, file_url, file_type, file_size, status, uploaded_by_name, tags, created_at, updated_at, version_num)
VALUES 
    (gen_random_uuid(), 'Contrat de travail - Marie Kouassi', 'Contrat CDI Responsable RH', 'CONTRACT', 'contrat_marie_kouassi.pdf', '/documents/contracts/contrat_marie_kouassi.pdf', 'https://storage.tornadoesjob.com/docs/contrat_marie_kouassi.pdf', 'PDF', 524288, 'APPROVED', 'Admin RH', '["rh", "contrat"]'::jsonb, now(), now(), 0),
    (gen_random_uuid(), 'Rapport financier Q1 2024', 'Rapport trimestriel', 'REPORT', 'rapport_q1_2024.pdf', '/documents/reports/rapport_q1_2024.pdf', 'https://storage.tornadoesjob.com/docs/rapport_q1_2024.pdf', 'PDF', 1048576, 'PUBLISHED', 'Directeur Financier', '["finance", "rapport"]'::jsonb, now(), now(), 0),
    (gen_random_uuid(), 'Politique de télétravail', 'Document interne', 'POLICY', 'politique_teletravail.pdf', '/documents/policies/politique_teletravail.pdf', 'https://storage.tornadoesjob.com/docs/politique_teletravail.pdf', 'PDF', 262144, 'PUBLISHED', 'Responsable RH', '["rh", "politique"]'::jsonb, now(), now(), 0);
