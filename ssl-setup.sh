#!/bin/bash

# SSL Certificate Setup Script for Cavacamisa Card Game
# This script generates self-signed SSL certificates for development

echo "Setting up SSL certificates for HTTPS support..."

# Create SSL directory if it doesn't exist
mkdir -p ssl

# Generate self-signed certificate
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
    -keyout ssl/server.key \
    -out ssl/server.crt \
    -subj "/C=US/ST=State/L=City/O=Cavacamisa/CN=localhost"

# Set appropriate permissions
chmod 600 ssl/server.key
chmod 644 ssl/server.crt

echo "SSL certificates generated successfully!"
echo "Files created:"
echo "  - ssl/server.crt (certificate)"
echo "  - ssl/server.key (private key)"
echo ""
echo "You can now run your application with HTTPS support:"
echo "  Development: docker-compose -f docker-compose.dev.yml up --build"
echo "  Production:  docker-compose -f docker-compose.prod.yml up --build"
echo ""
echo "Your application will be available at:"
echo "  - HTTPS: https://localhost"
echo "  - HTTP:  http://localhost (redirects to HTTPS)"