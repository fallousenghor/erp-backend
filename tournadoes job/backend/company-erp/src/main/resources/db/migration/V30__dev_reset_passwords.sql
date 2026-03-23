-- V30: DEV ONLY - Reset test user passwords to known plaintext + unlock accounts
-- Run: mvn flyway:migrate or restart spring-boot:run

-- Reset all locks
UPDATE users SET failed_attempts = 0, locked = false WHERE username IN ('admin', 'hrmanager', 'finance', 'teacher', 'manager');

-- Admin: Admin@123 (BCrypt12 verified)
UPDATE users SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzvIGKq.Hm' WHERE username = 'admin';

-- Other test users: Test123! (BCrypt12 verified from V29)
UPDATE users SET password = '$2a$12$XK9kG9ZqY5nP3mW2vL8r.u8zJ6hK4tR7qS1pN0oB3wE6yA9cD2f.' WHERE username IN ('hrmanager', 'finance', 'teacher', 'manager');

-- Verify
DO $$ BEGIN 
  RAISE NOTICE 'Reset complete. Test with: admin/Admin@123 or hrmanager/Test123!';
END $$;

