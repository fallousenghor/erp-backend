-- Add priority and progress columns to projects table
-- Migration: V18__add_project_priority_progress.sql

ALTER TABLE projects 
ADD COLUMN priority VARCHAR(20) DEFAULT 'MEDIUM',
ADD COLUMN progress INTEGER DEFAULT 0;

-- Set default values for existing records
UPDATE projects SET priority = 'MEDIUM' WHERE priority IS NULL;
UPDATE projects SET progress = 0 WHERE progress IS NULL;

