-- Fix media URLs by removing /v1/ prefix from Cloudinary URLs
UPDATE medias 
SET file_url = REPLACE(file_url, '/image/upload/v1/', '/image/upload/')
WHERE file_url LIKE '%/image/upload/v1/%';
