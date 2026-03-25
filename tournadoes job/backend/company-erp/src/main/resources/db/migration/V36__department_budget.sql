-- V36: Add department budget support
-- Add budget column and seed test budgets for existing departments

-- Add budget column
ALTER TABLE departments 
ADD COLUMN IF NOT EXISTS budget DECIMAL(15,2) DEFAULT 0 CHECK (budget >= 0);

-- Seed realistic test budgets for common departments (update if they exist)
UPDATE departments 
SET budget = 750000.00 
WHERE code IN ('ADMIN', 'RH');

UPDATE departments 
SET budget = 2000000.00 
WHERE code IN ('FIN', 'FINANCE');

UPDATE departments 
SET budget = 1500000.00 
WHERE code IN ('TECH', 'IT');

UPDATE departments 
SET budget = 1200000.00 
WHERE code IN ('COM', 'MARKETING');

-- Clean migration - Flyway handles history automatically
-- COMMENT handled by JPA or later


