-- V23__fix_positions_salary_column_types.sql
-- Convert salary columns from NUMERIC to DOUBLE PRECISION to match entity definition

-- PostgreSQL doesn't allow direct type conversion of columns with data
-- So we need to use USING clause or create new columns
ALTER TABLE IF EXISTS positions
ALTER COLUMN min_salary TYPE DOUBLE PRECISION USING min_salary::DOUBLE PRECISION;

ALTER TABLE IF EXISTS positions  
ALTER COLUMN max_salary TYPE DOUBLE PRECISION USING max_salary::DOUBLE PRECISION;
