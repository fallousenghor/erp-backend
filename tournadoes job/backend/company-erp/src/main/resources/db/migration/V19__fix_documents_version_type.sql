-- V19: Fix documents version column type to match Java Long
-- The BaseEntity uses Long for version, but V18 created INTEGER

-- Drop the existing version column and recreate as BIGINT
ALTER TABLE documents DROP COLUMN IF EXISTS version;
ALTER TABLE documents ADD COLUMN version BIGINT NOT NULL DEFAULT 0;

