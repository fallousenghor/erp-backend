-- V2: Organization — departments (avec positions intégrées)
-- Simplification: positions fusionnées dans departments, department_heads supprimé

-- ─── DEPARTMENTS ─────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS departments (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    name            VARCHAR(100)  NOT NULL,
    code            VARCHAR(10)   NOT NULL,
    description     VARCHAR(500),
    budget          NUMERIC(15,2) DEFAULT 0,
    manager_id      UUID,  -- Référence à employees (géré en application)
    manager_name    VARCHAR(160),
    positions       JSONB       DEFAULT '[]'::jsonb,  -- Liste des postes: [{"title": "Développeur", "min_salary": 30000, "max_salary": 50000}]
    active          BOOLEAN       NOT NULL DEFAULT true,
    deleted         BOOLEAN       NOT NULL DEFAULT false,
    deleted_at      TIMESTAMP,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_departments     PRIMARY KEY (id),
    CONSTRAINT uk_department_code UNIQUE (code)
);

CREATE INDEX IF NOT EXISTS idx_department_code    ON departments(code);
CREATE INDEX IF NOT EXISTS idx_department_active  ON departments(active) WHERE deleted = false;

-- ─── SEED DATA — Départements standards ───────────────────────────────────────
INSERT INTO departments (id, name, code, description, budget, positions, active, created_at, updated_at, version) VALUES
    (gen_random_uuid(), 'Direction Générale', 'DG', 'Direction et administration', 500000, 
     '[{"title": "Directeur Général", "min_salary": 80000, "max_salary": 150000}, {"title": "Assistant(e) de Direction", "min_salary": 35000, "max_salary": 45000}]'::jsonb,
     true, now(), now(), 0),
    (gen_random_uuid(), 'Ressources Humaines', 'RH', 'Gestion du personnel et formation', 200000,
     '[{"title": "Responsable RH", "min_salary": 50000, "max_salary": 70000}, {"title": "Chargé de Recrutement", "min_salary": 35000, "max_salary": 45000}]'::jsonb,
     true, now(), now(), 0),
    (gen_random_uuid(), 'Finance & Comptabilité', 'FIN', 'Gestion financière et comptable', 300000,
     '[{"title": "Directeur Financier", "min_salary": 70000, "max_salary": 100000}, {"title": "Comptable", "min_salary": 40000, "max_salary": 55000}]'::jsonb,
     true, now(), now(), 0),
    (gen_random_uuid(), 'Commercial & Marketing', 'COM', 'Ventes et communication', 400000,
     '[{"title": "Directeur Commercial", "min_salary": 60000, "max_salary": 90000}, {"title": "Commercial", "min_salary": 30000, "max_salary": 50000}]'::jsonb,
     true, now(), now(), 0),
    (gen_random_uuid(), 'Informatique & Développement', 'IT', 'Développement et support technique', 350000,
     '[{"title": "CTO", "min_salary": 70000, "max_salary": 100000}, {"title": "Développeur Full Stack", "min_salary": 45000, "max_salary": 65000}, {"title": "DevOps", "min_salary": 50000, "max_salary": 70000}]'::jsonb,
     true, now(), now(), 0),
    (gen_random_uuid(), 'Production & Opérations', 'PROD', 'Gestion de la production', 250000,
     '[{"title": "Responsable Production", "min_salary": 55000, "max_salary": 75000}, {"title": "Chef de Projet", "min_salary": 45000, "max_salary": 60000}]'::jsonb,
     true, now(), now(), 0);
