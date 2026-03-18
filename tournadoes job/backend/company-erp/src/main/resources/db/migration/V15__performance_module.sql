-- V16: Performance Management Module — performance_reviews and objectives tables

-- ══════════════════════════════════════════════════════════════════════════════
-- PERFORMANCE REVIEWS TABLE
-- ══════════════════════════════════════════════════════════════════════════════

CREATE TABLE performance_reviews (
    -- BaseEntity fields
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    created_at      TIMESTAMP       NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT now(),
    version         BIGINT          NOT NULL DEFAULT 0,
    
    -- BaseAuditEntity fields
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    
    -- PerformanceReview fields
    employee_id     UUID            NOT NULL,
    employee_name   VARCHAR(100),
    department_id   UUID,
    department_name VARCHAR(100),
    period          VARCHAR(50),
    rating          DOUBLE PRECISION,
    objectives_completed  INT,
    objectives_total      INT,
    feedback        VARCHAR(2000),
    reviewer_id     UUID,
    reviewer_name   VARCHAR(100),
    reviewed_at     DATE,
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    
    CONSTRAINT pk_performance_reviews PRIMARY KEY (id)
);

-- Indexes for performance_reviews
CREATE INDEX idx_pr_employee ON performance_reviews(employee_id);
CREATE INDEX idx_pr_department ON performance_reviews(department_id);
CREATE INDEX idx_pr_status ON performance_reviews(status);
CREATE INDEX idx_pr_period ON performance_reviews(period);
CREATE INDEX idx_pr_rating ON performance_reviews(rating);
CREATE INDEX idx_pr_created ON performance_reviews(created_at);

-- ══════════════════════════════════════════════════════════════════════════════
-- OBJECTIVES TABLE
-- ══════════════════════════════════════════════════════════════════════════════

CREATE TABLE objectives (
    -- BaseEntity fields
    id              UUID            NOT NULL DEFAULT gen_random_uuid(),
    created_at      TIMESTAMP       NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP       NOT NULL DEFAULT now(),
    version         BIGINT          NOT NULL DEFAULT 0,
    
    -- BaseAuditEntity fields
    created_by      VARCHAR(100),
    updated_by      VARCHAR(100),
    
    -- Objective fields
    employee_id     UUID            NOT NULL,
    employee_name   VARCHAR(100),
    title           VARCHAR(200)    NOT NULL,
    description     VARCHAR(1000),
    target          INT,
    achieved        INT,
    status          VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    due_date        DATE,
    
    CONSTRAINT pk_objectives PRIMARY KEY (id)
);

-- Indexes for objectives
CREATE INDEX idx_obj_employee ON objectives(employee_id);
CREATE INDEX idx_obj_status ON objectives(status);
CREATE INDEX idx_obj_due_date ON objectives(due_date);
CREATE INDEX idx_obj_target ON objectives(target);
CREATE INDEX idx_obj_achieved ON objectives(achieved);
CREATE INDEX idx_obj_created ON objectives(created_at);

-- ══════════════════════════════════════════════════════════════════════════════
-- COMMENTS FOR DOCUMENTATION
-- ══════════════════════════════════════════════════════════════════════════════

COMMENT ON TABLE performance_reviews IS 'Stores employee performance review evaluations';
COMMENT ON TABLE objectives IS 'Stores employee performance objectives and goals';

COMMENT ON COLUMN performance_reviews.employee_id IS 'UUID of the employee being reviewed';
COMMENT ON COLUMN performance_reviews.period IS 'Review period (e.g., Q1 2024, Year 2024)';
COMMENT ON COLUMN performance_reviews.rating IS 'Performance rating (1-5 scale)';
COMMENT ON COLUMN performance_reviews.status IS 'Review status: PENDING, IN_PROGRESS, COMPLETED';

COMMENT ON COLUMN objectives.employee_id IS 'UUID of the employee who owns the objective';
COMMENT ON COLUMN objectives.target IS 'Target value to achieve';
COMMENT ON COLUMN objectives.achieved IS 'Current achieved value';
COMMENT ON COLUMN objectives.status IS 'Objective status: PENDING, ACHIEVED, EXCEEDED, AT_RISK';

