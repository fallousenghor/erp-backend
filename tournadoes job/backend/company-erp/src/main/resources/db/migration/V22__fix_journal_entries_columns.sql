-- V22__fix_journal_entries_columns.sql
-- Add missing columns to journal_entries table to match JournalEntry entity

-- First, add columns that can be nullable
ALTER TABLE IF EXISTS journal_entries
ADD COLUMN IF NOT EXISTS account_name VARCHAR(255),
ADD COLUMN IF NOT EXISTS entry_date TIMESTAMP,
ADD COLUMN IF NOT EXISTS notes VARCHAR(1000);

-- Copy data from date to entry_date if date column exists
DO $$ 
BEGIN 
  IF EXISTS (
    SELECT 1 FROM information_schema.columns 
    WHERE table_name = 'journal_entries' AND column_name = 'date'
  ) THEN
    UPDATE journal_entries 
    SET entry_date = date::timestamp 
    WHERE entry_date IS NULL;
    
    ALTER TABLE journal_entries DROP COLUMN date;
  END IF;
END $$;

-- Make entry_date NOT NULL with proper default
ALTER TABLE IF EXISTS journal_entries
ALTER COLUMN entry_date SET DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE IF EXISTS journal_entries
ALTER COLUMN entry_date SET NOT NULL;

-- Set default for account_name if it was nullable
UPDATE journal_entries
SET account_name = 'Default Account'
WHERE account_name IS NULL;

ALTER TABLE IF EXISTS journal_entries
ALTER COLUMN account_name SET NOT NULL;

-- Update journal_entry_type ENUM to include ADJUSTMENT if not present
DO $$
BEGIN
  ALTER TYPE journal_entry_type ADD VALUE 'ADJUSTMENT';
EXCEPTION 
  WHEN duplicate_object THEN null;
END $$;

-- Add index for entry_date for better query performance
CREATE INDEX IF NOT EXISTS idx_journal_entries_entry_date ON journal_entries(entry_date);
