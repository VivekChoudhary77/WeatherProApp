#!/bin/bash

# WeatherPro Backend - Render Deployment Script
# Usage: ./deploy-to-render.sh YOUR_DOCKERHUB_USERNAME

set -e  # Exit on error

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}WeatherPro Backend Deployment to Render${NC}"
echo -e "${BLUE}========================================${NC}"
echo

# Check if Docker Hub username is provided
if [ -z "$1" ]; then
    echo -e "${YELLOW}Usage: ./deploy-to-render.sh YOUR_DOCKERHUB_USERNAME${NC}"
    echo "Example: ./deploy-to-render.sh vivekchoudhary77"
    exit 1
fi

DOCKERHUB_USERNAME=$1
IMAGE_NAME="weatherpro-backend"
TAG="latest"

echo -e "${GREEN}✓ Docker Hub Username: ${DOCKERHUB_USERNAME}${NC}"
echo

# Step 1: Build the Spring Boot application
echo -e "${BLUE}Step 1: Building Spring Boot application...${NC}"
cd weatherpro-backend
mvn clean package -DskipTests
echo -e "${GREEN}✓ Build complete!${NC}"
echo

# Step 2: Build Docker image
echo -e "${BLUE}Step 2: Building Docker image...${NC}"
docker build -t ${IMAGE_NAME} .
echo -e "${GREEN}✓ Docker image built!${NC}"
echo

# Step 3: Tag the image
echo -e "${BLUE}Step 3: Tagging Docker image...${NC}"
docker tag ${IMAGE_NAME} ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}
echo -e "${GREEN}✓ Image tagged as: ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}${NC}"
echo

# Step 4: Login to Docker Hub
echo -e "${BLUE}Step 4: Logging into Docker Hub...${NC}"
echo "Please enter your Docker Hub credentials:"
docker login
echo -e "${GREEN}✓ Logged in to Docker Hub!${NC}"
echo

# Step 5: Push to Docker Hub
echo -e "${BLUE}Step 5: Pushing image to Docker Hub...${NC}"
docker push ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}
echo -e "${GREEN}✓ Image pushed successfully!${NC}"
echo

# Final instructions
echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}Deployment Preparation Complete! 🎉${NC}"
echo -e "${GREEN}========================================${NC}"
echo
echo -e "${YELLOW}Next Steps:${NC}"
echo "1. Go to: https://dashboard.render.com/"
echo "2. Click 'New +' → 'Web Service'"
echo "3. Select 'Deploy an existing image from a registry'"
echo "4. Enter image: ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}"
echo "5. Configure:"
echo "   - Name: weatherpro-backend"
echo "   - Region: Choose closest"
echo "   - Instance Type: Free"
echo "   - Port: 8080"
echo "6. Add Environment Variables (see RENDER_DEPLOYMENT.md)"
echo "7. Click 'Create Web Service'"
echo
echo -e "${BLUE}Your Docker image is ready!${NC}"
echo "Image: ${DOCKERHUB_USERNAME}/${IMAGE_NAME}:${TAG}"
echo
echo -e "${YELLOW}For detailed instructions, see: RENDER_DEPLOYMENT.md${NC}"

