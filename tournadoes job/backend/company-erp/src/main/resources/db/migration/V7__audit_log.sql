-- V8: Audit log table

CREATE TABLE audit_logs (
    id            UUID          NOT NULL DEFAULT gen_random_uuid(),
    username      VARCHAR(100)  NOT NULL,
    action        VARCHAR(100)  NOT NULL,
    entity_type   VARCHAR(100),
    entity_id     VARCHAR(100),
    old_value     TEXT,
    new_value     TEXT,
    ip_address    VARCHAR(45),
    user_agent    VARCHAR(500),
    success       BOOLEAN       NOT NULL DEFAULT true,
    error_message TEXT,
    created_at    TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT pk_audit_logs PRIMARY KEY (id)
);

CREATE INDEX idx_audit_user      ON audit_logs(username);
CREATE INDEX idx_audit_entity    ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_timestamp ON audit_logs(created_at);
CREATE INDEX idx_audit_action    ON audit_logs(action);

