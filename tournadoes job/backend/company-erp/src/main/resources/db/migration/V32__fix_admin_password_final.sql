-- V32: Fix admin password with correct BCrypt hash
-- This migration fixes the admin password with the correct hash for 'Admin@123'

UPDATE users 
SET password = '$2a$12$M6Dhq1mJUzN3MnHygNo0lOiYVqNMYdfFFoaCF/kcTjn6VgZ.oRBfm', 
    failed_attempts = 0, 
    locked = false 
WHERE username = 'admin';

DO $$ BEGIN 
  RAISE NOTICE 'Admin password fixed to: Admin@123';
END $$;
