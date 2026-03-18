-- V18: Add version column to documents table for optimistic locking
-- This column is required by BaseAuditEntity (which extends BaseEntity with @Version)

ALTER TABLE documents ADD COLUMN IF NOT EXISTS version INTEGER NOT NULL DEFAULT 0;

