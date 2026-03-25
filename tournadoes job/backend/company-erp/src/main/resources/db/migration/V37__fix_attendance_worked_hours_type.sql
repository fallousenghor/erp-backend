-- V37: Fix attendance_records worked_hours column type from INTERVAL to VARCHAR
-- This migration changes the worked_hours column from PostgreSQL INTERVAL to VARCHAR(20)
-- to store duration in "HH:MM" format

-- Step 1: Add a temporary column with the new type
ALTER TABLE attendance_records 
ADD COLUMN worked_hours_temp VARCHAR(20);

-- Step 2: Convert existing INTERVAL data to HH:MM format
UPDATE attendance_records 
SET worked_hours_temp = 
  CASE 
    WHEN worked_hours IS NOT NULL THEN
      LPAD(EXTRACT(HOUR FROM worked_hours)::TEXT, 2, '0') || ':' ||
      LPAD(LPAD(EXTRACT(MINUTE FROM worked_hours)::TEXT, 2, '0'), 2, '0')
    ELSE NULL
  END;

-- Step 3: Drop the old column
ALTER TABLE attendance_records 
DROP COLUMN worked_hours;

-- Step 4: Rename the temporary column to worked_hours
ALTER TABLE attendance_records 
RENAME COLUMN worked_hours_temp TO worked_hours;

-- Step 5: Add a comment for documentation
COMMENT ON COLUMN attendance_records.worked_hours IS 'Worked hours in HH:MM format (e.g., 08:30)';
