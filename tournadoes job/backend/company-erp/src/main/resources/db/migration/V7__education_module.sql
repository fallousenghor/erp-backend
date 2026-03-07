-- V7: Education module — training_programs, course_modules, students, teachers, enrollments, grades

-- ─── TEACHERS ─────────────────────────────────────────────────────────────────
CREATE TABLE teachers (
    id              UUID          NOT NULL DEFAULT gen_random_uuid(),
    first_name      VARCHAR(80)   NOT NULL,
    last_name       VARCHAR(80)   NOT NULL,
    email           VARCHAR(150)  NOT NULL,
    phone           VARCHAR(20),
    specialization  VARCHAR(200),
    bio             VARCHAR(1000),
    active          BOOLEAN       NOT NULL DEFAULT true,
    employee_id     UUID,
    created_at      TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP     NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_teachers      PRIMARY KEY (id),
    CONSTRAINT uk_teacher_email UNIQUE (email)
);

-- ─── TRAINING_PROGRAMS ────────────────────────────────────────────────────────
CREATE TABLE training_programs (
    id             UUID           NOT NULL DEFAULT gen_random_uuid(),
    title          VARCHAR(200)   NOT NULL,
    description    VARCHAR(2000),
    level          VARCHAR(20)    NOT NULL,
    duration_weeks INT            NOT NULL,
    max_students   INT,
    start_date     DATE,
    end_date       DATE,
    active         BOOLEAN        NOT NULL DEFAULT true,
    passing_score  NUMERIC(5,2)   NOT NULL DEFAULT 10.00,
    created_at     TIMESTAMP      NOT NULL DEFAULT now(),
    updated_at     TIMESTAMP      NOT NULL DEFAULT now(),
    created_by     VARCHAR(100),
    updated_by     VARCHAR(100),
    version        BIGINT         NOT NULL DEFAULT 0,
    CONSTRAINT pk_training_programs PRIMARY KEY (id)
);

CREATE INDEX idx_program_active ON training_programs(active);

-- ─── COURSE_MODULES ───────────────────────────────────────────────────────────
CREATE TABLE course_modules (
    id              UUID         NOT NULL DEFAULT gen_random_uuid(),
    title           VARCHAR(200) NOT NULL,
    description     VARCHAR(1000),
    duration_hours  INT          NOT NULL,
    order_index     INT          NOT NULL DEFAULT 0,
    coefficient     DOUBLE PRECISION NOT NULL DEFAULT 1.0,
    program_id      UUID         NOT NULL,
    teacher_id      UUID,
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now(),
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    version         BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_course_modules  PRIMARY KEY (id),
    CONSTRAINT fk_module_program  FOREIGN KEY (program_id) REFERENCES training_programs(id) ON DELETE CASCADE,
    CONSTRAINT fk_module_teacher  FOREIGN KEY (teacher_id) REFERENCES teachers(id) ON DELETE SET NULL
);

CREATE INDEX idx_module_program ON course_modules(program_id);

-- ─── STUDENTS ─────────────────────────────────────────────────────────────────
CREATE TABLE students (
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    student_code VARCHAR(20)  NOT NULL,
    first_name   VARCHAR(80)  NOT NULL,
    last_name    VARCHAR(80)  NOT NULL,
    email        VARCHAR(150) NOT NULL,
    phone        VARCHAR(20),
    birth_date   DATE,
    address      VARCHAR(500),
    active       BOOLEAN      NOT NULL DEFAULT true,
    employee_id  UUID,
    created_at   TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at   TIMESTAMP    NOT NULL DEFAULT now(),
    created_by   VARCHAR(100),
    updated_by   VARCHAR(100),
    version      BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_students      PRIMARY KEY (id),
    CONSTRAINT uk_student_code  UNIQUE (student_code),
    CONSTRAINT uk_student_email UNIQUE (email)
);

-- ─── ENROLLMENTS ──────────────────────────────────────────────────────────────
CREATE TABLE enrollments (
    id                 UUID         NOT NULL DEFAULT gen_random_uuid(),
    student_id         UUID         NOT NULL,
    program_id         UUID         NOT NULL,
    enrollment_date    DATE         NOT NULL,
    completion_date    DATE,
    status             VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    final_average      NUMERIC(5,2),
    final_letter_grade VARCHAR(2),
    passed             BOOLEAN      NOT NULL DEFAULT false,
    created_at         TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at         TIMESTAMP    NOT NULL DEFAULT now(),
    created_by         VARCHAR(100),
    updated_by         VARCHAR(100),
    version            BIGINT       NOT NULL DEFAULT 0,
    CONSTRAINT pk_enrollments               PRIMARY KEY (id),
    CONSTRAINT uk_enrollment_student_prog   UNIQUE (student_id, program_id),
    CONSTRAINT fk_enroll_student            FOREIGN KEY (student_id) REFERENCES students(id),
    CONSTRAINT fk_enroll_program            FOREIGN KEY (program_id) REFERENCES training_programs(id)
);

CREATE INDEX idx_enrollment_student ON enrollments(student_id);
CREATE INDEX idx_enrollment_program ON enrollments(program_id);
CREATE INDEX idx_enrollment_status  ON enrollments(status);

-- ─── MODULE_GRADES ────────────────────────────────────────────────────────────
CREATE TABLE module_grades (
    id            UUID          NOT NULL DEFAULT gen_random_uuid(),
    enrollment_id UUID          NOT NULL,
    module_id     UUID          NOT NULL,
    score         NUMERIC(5,2)  NOT NULL,
    max_score     NUMERIC(5,2)  NOT NULL,
    recorded_date DATE          NOT NULL,
    comments      VARCHAR(500),
    created_at    TIMESTAMP     NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP     NOT NULL DEFAULT now(),
    version       BIGINT        NOT NULL DEFAULT 0,
    CONSTRAINT pk_module_grades           PRIMARY KEY (id),
    CONSTRAINT uk_grade_enrollment_module UNIQUE (enrollment_id, module_id),
    CONSTRAINT fk_grade_enrollment        FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE,
    CONSTRAINT fk_grade_module            FOREIGN KEY (module_id) REFERENCES course_modules(id) ON DELETE CASCADE
);

CREATE INDEX idx_grade_enrollment ON module_grades(enrollment_id);
