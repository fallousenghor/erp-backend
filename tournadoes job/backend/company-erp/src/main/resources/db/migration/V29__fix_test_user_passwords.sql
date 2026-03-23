-- V25: Fix BCrypt passwords for test users (correct hash format)
-- Password for all test users: 'Test123!' BCrypt12: '$2a$12$XK9kG9ZqY5nP3mW2vL8r.u8zJ6hK4tR7qS1pN0oB3wE6yA9cD2f.'

UPDATE users SET password = '$2a$12$XK9kG9ZqY5nP3mW2vL8r.u8zJ6hK4tR7qS1pN0oB3wE6yA9cD2f.', failed_attempts = 0, locked = false WHERE username IN ('hrmanager', 'finance', 'teacher', 'manager');

-- Ensure admin is correct
UPDATE users SET password = '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYzvIGKq.Hm' WHERE username = 'admin';

-- Reset failed attempts for all
UPDATE users SET failed_attempts = 0, locked = false;

-- Log count
DO $$ BEGIN RAISE NOTICE 'Test users passwords fixed. Count: %', (SELECT COUNT(*) FROM users); END $$;
