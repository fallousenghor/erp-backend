-- V26: Increase employees.photo_url/qr_code_url size AND fix admin password

-- 1. Schema change: Increase employees photo/qr_code URL column sizes for Cloudinary
ALTER TABLE employees 
ALTER COLUMN photo_url TYPE VARCHAR(1000),
ALTER COLUMN qr_code_url TYPE VARCHAR(1000);

COMMENT ON COLUMN employees.photo_url IS 'Cloudinary public URL for employee avatar (resized/rounded)';
COMMENT ON COLUMN employees.qr_code_url IS 'Cloudinary public URL for employee QR code';

-- 2. Data update: Fix admin password hash to correct BCrypt12 for 'Admin@123'
UPDATE users 
SET password = '$2a$12$RhuFE/xRsn950v8LkCzDku9gBCmCH45Xm/d4o5ppgoLf84EsXV7re', 
    failed_attempts = 0 
WHERE username = 'admin';

COMMENT ON TABLE users IS 'Admin password now correctly hashed for Admin@123';
