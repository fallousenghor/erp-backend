#!/bin/bash
set -e

cd "$(dirname "$0")"

echo "🚀 Starting Company ERP (dev mode)..."

# Load .env if exists
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
  echo ".env loaded"
fi

# Use mvnw wrapper
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"

