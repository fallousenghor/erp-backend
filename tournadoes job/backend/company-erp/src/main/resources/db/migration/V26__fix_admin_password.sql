-- V26: Fix admin password hash to correct BCrypt12 for 'Admin@123'

UPDATE users 
SET password = '$2a$12$RhuFE/xRsn950v8LkCzDku9gBCmCH45Xm/d4o5ppgoLf84EsXV7re', 
    failed_attempts = 0 
WHERE username = 'admin';

COMMENT ON TABLE users IS 'Admin password now correctly hashed for Admin@123';
