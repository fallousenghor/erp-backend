-- V10: Add photo/image/document URL fields for Cloudinary integration

-- Add photo_url to employees table
ALTER TABLE employees ADD COLUMN IF NOT EXISTS photo_url VARCHAR(500);

-- Add image_url and document_url to assets table
ALTER TABLE assets ADD COLUMN IF NOT EXISTS image_url VARCHAR(500);
ALTER TABLE assets ADD COLUMN IF NOT EXISTS document_url VARCHAR(500);

