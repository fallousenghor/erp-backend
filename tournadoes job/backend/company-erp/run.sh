#!/bin/bash

# =============================================================================
# Company ERP — Development Run Script
# Loads environment variables from .env and starts the application
# =============================================================================

# Load environment variables from .env
export $(grep -v '^#' .env | xargs)

echo "✓ Environment variables loaded from .env"
echo "✓ DB_URL: ${DB_URL}"
echo "✓ CLOUDINARY_CLOUD_NAME: ${CLOUDINARY_CLOUD_NAME}"
echo ""

# Run the application using Maven
./mvnw spring-boot:run
