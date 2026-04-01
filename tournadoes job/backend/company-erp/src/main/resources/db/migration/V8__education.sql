-- V8: Education — students, training_programs (avec modules JSON), enrollments (avec grades JSON)
-- Simplification: teachers fusionné dans employees/users, course_modules dans training_programs, module_grades dans enrollments

-- ─── STUDENTS ─────────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS students (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    student_code    VARCHAR(20)   NOT NULL,
    first_name      VARCHAR(80)   NOT NULL,
    last_name       VARCHAR(80)   NOT NULL,
    email           VARCHAR(150)  NOT NULL,
    phone           VARCHAR(20),
    birth_date      DATE,
    gender          VARCHAR(10),
    address         VARCHAR(500),
    city            VARCHAR(100),
    postal_code     VARCHAR(10),
    country         VARCHAR(50)   DEFAULT 'Côte d''Ivoire',
    photo_url       VARCHAR(500),
    emergency_contact VARCHAR(100),
    emergency_phone VARCHAR(20),
    emergency_relation VARCHAR(50),
    education_level VARCHAR(50),
    occupation      VARCHAR(100),
    company_name    VARCHAR(200),
    status          VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE, GRADUATED, SUSPENDED, DROPPED_OUT
    notes           VARCHAR(1000),
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_students      PRIMARY KEY (id),
    CONSTRAINT uk_student_code  UNIQUE (student_code),
    CONSTRAINT uk_student_email UNIQUE (email)
);

CREATE INDEX IF NOT EXISTS idx_students_status ON students(status);
CREATE INDEX IF NOT EXISTS idx_students_name   ON students(last_name, first_name);

-- ─── TRAINING_PROGRAMS ────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS training_programs (
    id              UUID           NOT NULL DEFAULT gen_random_uuid(),
    title           VARCHAR(200)   NOT NULL,
    description     VARCHAR(2000),
    category        VARCHAR(50),   -- TECH, MANAGEMENT, LANGUAGE, SOFT_SKILLS, CERTIFICATION
    level           VARCHAR(20)    NOT NULL,  -- BEGINNER, INTERMEDIATE, ADVANCED
    duration_weeks  INT            NOT NULL,
    duration_hours  INT            NOT NULL,
    max_students    INT,
    start_date      DATE,
    end_date        DATE,
    schedule        VARCHAR(200),  -- Ex: "Lun-Mer-Ven 18h-20h"
    location        VARCHAR(200),  -- Adresse ou "En ligne"
    price           NUMERIC(15,2)  NOT NULL DEFAULT 0,
    currency        VARCHAR(3)     DEFAULT 'XOF',
    teacher_id      UUID,
    teacher_name    VARCHAR(160),
    modules         JSONB          DEFAULT '[]'::jsonb,  -- [{"title": "Intro Java", "duration_hours": 20, "order": 1}, ...]
    prerequisites   VARCHAR(500),
    certification   BOOLEAN        DEFAULT false,
    active          BOOLEAN        NOT NULL DEFAULT true,
    passing_score   NUMERIC(5,2)   NOT NULL DEFAULT 10.00,
    enrolled_count  INT            DEFAULT 0,
    completed_count INT            DEFAULT 0,
    created_at      TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP      NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT         NOT NULL DEFAULT 0,
    CONSTRAINT pk_training_programs PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS idx_programs_active ON training_programs(active);
CREATE INDEX IF NOT EXISTS idx_programs_category ON training_programs(category);

-- ─── ENROLLMENTS ──────────────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS enrollments (
    id                 UUID          NOT NULL DEFAULT gen_random_uuid(),
    student_id         UUID          NOT NULL,
    student_name       VARCHAR(160)  NOT NULL,
    student_code       VARCHAR(20),
    program_id         UUID          NOT NULL,
    program_name       VARCHAR(200)  NOT NULL,
    enrollment_date    DATE          NOT NULL,
    completion_date    DATE,
    status             VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',  -- ACTIVE, COMPLETED, DROPPED_OUT, FAILED
    payment_status     VARCHAR(20)   DEFAULT 'PENDING',  -- PENDING, PARTIAL, PAID
    amount_paid        NUMERIC(15,2) DEFAULT 0,
    final_average      NUMERIC(5,2),
    final_letter_grade VARCHAR(2),   -- A, B, C, D, F
    passed             BOOLEAN       DEFAULT false,
    grades             JSONB         DEFAULT '[]'::jsonb,  -- [{"module": "Intro Java", "score": 15, "max_score": 20, "date": "2024-01-15"}, ...]
    attendance_rate    NUMERIC(5,2),
    notes              VARCHAR(1000),
    created_at         TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP     NOT NULL DEFAULT now(),
    created_by         VARCHAR(100),
    updated_by         VARCHAR(100),
    version            BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_enrollments             PRIMARY KEY (id),
    CONSTRAINT uk_enrollment_student_prog UNIQUE (student_id, program_id),
    CONSTRAINT fk_enroll_student          FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enroll_program          FOREIGN KEY (program_id) REFERENCES training_programs(id)
);

CREATE INDEX IF NOT EXISTS idx_enrollment_student ON enrollments(student_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_program ON enrollments(program_id);
CREATE INDEX IF NOT EXISTS idx_enrollment_status  ON enrollments(status);

-- ─── SEED DATA — Programmes de formation ──────────────────────────────────────
INSERT INTO training_programs (id, title, description, category, level, duration_weeks, duration_hours, max_students, start_date, end_date, schedule, location, price, modules, active, passing_score, created_at, updated_at, version)
VALUES 
    (gen_random_uuid(), 'Développement Full Stack JavaScript', 'Formation complète pour devenir développeur Full Stack avec Node.js et React', 'TECH', 'INTERMEDIATE', 12, 180, 20, '2024-03-01', '2024-05-31', 'Lun-Ven 09h-17h', 'Centre de Formation - Salle A', 500000,
     '[{"title": "Introduction à JavaScript", "duration_hours": 20, "order": 1}, {"title": "Node.js Backend", "duration_hours": 40, "order": 2}, {"title": "React Frontend", "duration_hours": 40, "order": 3}, {"title": "Base de données", "duration_hours": 30, "order": 4}, {"title": "Projet Final", "duration_hours": 50, "order": 5}]'::jsonb,
     true, 10.00, now(), now(), 0),
    (gen_random_uuid(), 'Gestion de Projet Agile', 'Maîtrisez les méthodologies Agile et Scrum', 'MANAGEMENT', 'BEGINNER', 4, 40, 25, '2024-04-01', '2024-04-30', 'Sam 08h-18h', 'Centre de Formation - Salle B', 200000,
     '[{"title": "Fondamentaux Agile", "duration_hours": 8, "order": 1}, {"title": "Scrum Master", "duration_hours": 16, "order": 2}, {"title": "Atelier Pratique", "duration_hours": 16, "order": 3}]'::jsonb,
     true, 10.00, now(), now(), 0),
    (gen_random_uuid(), 'Anglais Professionnel', 'Améliorez votre anglais pour le monde professionnel', 'LANGUAGE', 'INTERMEDIATE', 8, 64, 15, '2024-03-15', '2024-05-15', 'Mar-Jeu 18h-20h', 'En ligne', 150000,
     '[{"title": "Communication écrite", "duration_hours": 20, "order": 1}, {"title": "Communication orale", "duration_hours": 24, "order": 2}, {"title": "Présentations business", "duration_hours": 20, "order": 3}]'::jsonb,
     true, 10.00, now(), now(), 0);

-- ─── SEED DATA — Étudiants de démo ────────────────────────────────────────────
INSERT INTO students (id, student_code, first_name, last_name, email, phone, birth_date, education_level, occupation, status, created_at, updated_at, version)
VALUES 
    (gen_random_uuid(), 'STU-2024-001', 'Kouassi', 'N''Guessan', 'kouassi.nguessan@email.com', '0707070740', '2000-05-15', 'Bac+2', 'Étudiant', 'ACTIVE', now(), now(), 0),
    (gen_random_uuid(), 'STU-2024-002', 'Fatoumata', 'Diarra', 'fatoumata.diarra@email.com', '0707070741', '1999-08-22', 'Bac+3', 'Employée', 'ACTIVE', now(), now(), 0),
    (gen_random_uuid(), 'STU-2024-003', 'Mohamed', 'Camara', 'mohamed.camara@email.com', '0707070742', '2001-03-10', 'Bac', 'Étudiant', 'ACTIVE', now(), now(), 0),
    (gen_random_uuid(), 'STU-2024-004', 'Awa', 'Bamba', 'awa.bamba@email.com', '0707070743', '1998-12-05', 'Bac+4', 'En recherche', 'ACTIVE', now(), now(), 0);

-- ─── SEED DATA — Inscriptions de démo ─────────────────────────────────────────
INSERT INTO enrollments (id, student_id, student_name, student_code, program_id, program_name, enrollment_date, status, payment_status, amount_paid, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    s.id,
    s.first_name || ' ' || s.last_name,
    s.student_code,
    p.id,
    p.title,
    '2024-02-15',
    'ACTIVE',
    'PARTIAL',
    250000,
    now(),
    now(),
    0
FROM students s, training_programs p 
WHERE s.student_code = 'STU-2024-001' AND p.title = 'Développement Full Stack JavaScript';

INSERT INTO enrollments (id, student_id, student_name, student_code, program_id, program_name, enrollment_date, status, payment_status, amount_paid, created_at, updated_at, version)
SELECT 
    gen_random_uuid(),
    s.id,
    s.first_name || ' ' || s.last_name,
    s.student_code,
    p.id,
    p.title,
    '2024-02-20',
    'ACTIVE',
    'PAID',
    200000,
    now(),
    now(),
    0
FROM students s, training_programs p 
WHERE s.student_code = 'STU-2024-002' AND p.title = 'Gestion de Projet Agile';
