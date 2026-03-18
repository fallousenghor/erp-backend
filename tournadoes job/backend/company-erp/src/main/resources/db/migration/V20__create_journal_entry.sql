-- V20__create_journal_entry.sql
DO $$ 
BEGIN 
  CREATE TYPE journal_entry_type AS ENUM ('DEBIT', 'CREDIT', 'TRANSFER');
EXCEPTION 
  WHEN duplicate_object THEN null; 
END $$;

CREATE TABLE IF NOT EXISTS journal_entries (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  reference VARCHAR(50) NOT NULL,
  date DATE NOT NULL,
  description TEXT,
  account_code VARCHAR(20) NOT NULL,
  type journal_entry_type NOT NULL,
  debit_amount DECIMAL(15,2) DEFAULT 0,
  credit_amount DECIMAL(15,2) DEFAULT 0,
  currency VARCHAR(3) DEFAULT 'XOF',
  balanced BOOLEAN DEFAULT false,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_journal_entries_reference ON journal_entries(reference);
CREATE INDEX idx_journal_entries_date ON journal_entries(date);
CREATE INDEX idx_journal_entries_account ON journal_entries(account_code);

COMMENT ON TABLE journal_entries IS 'Accounting journal entries - debit/credit records';
