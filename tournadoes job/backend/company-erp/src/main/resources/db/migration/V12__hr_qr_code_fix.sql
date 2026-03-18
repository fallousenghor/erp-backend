-- V13: Fix HR module - Add missing qr_code_url column to employees table
-- This migration adds the qr_code_url column that was added to the Employee entity

ALTER TABLE employees ADD COLUMN IF NOT EXISTS qr_code_url VARCHAR(500);

COMMENT ON COLUMN employees.qr_code_url IS 'URL to the QR code generated for employee identification';

