#!/bin/bash

# Local Pipeline Script for SonarQube Analysis
# Execute from WSL in the project root directory

set -e

echo "=== Local CI/CD Pipeline with SonarQube ==="
echo ""

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker Desktop."
    exit 1
fi

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    print_error "Maven is not installed. Please install Maven first."
    exit 1
fi

# Check if Java is installed
if ! command -v java &> /dev/null; then
    print_error "Java is not installed. Please install Java 17 or higher."
    exit 1
fi

# Step 1: Start SonarQube with Docker Compose
print_status "Starting SonarQube with Docker Compose..."
docker-compose up -d

# Wait for SonarQube to be ready
print_status "Waiting for SonarQube to be ready..."
timeout=300  # 5 minutes timeout
elapsed=0
while ! curl -s http://localhost:9000/api/system/status | grep -q '"status":"UP"'; do
    if [ $elapsed -ge $timeout ]; then
        print_error "SonarQube failed to start within 5 minutes"
        exit 1
    fi
    echo -n "."
    sleep 5
    elapsed=$((elapsed + 5))
done
echo ""
print_status "SonarQube is ready!"

# Step 2: Build and Test
print_status "Step 1/3: Building and Testing..."
cd ccrHospitalManagement

# Clean and compile
print_status "Compiling project..."
mvn clean compile

# Run tests
print_status "Running tests..."
mvn test

# Generate JaCoCo coverage report
print_status "Generating coverage report..."
mvn jacoco:report

# Step 3: SonarQube Analysis
print_status "Step 2/3: Running SonarQube Analysis..."

# Default SonarQube token (admin/admin default setup)
SONAR_TOKEN="squ_83671d99a4a3d5b4e69c6e2d7b3f2e1c4d5a6b7c"
SONAR_HOST_URL="http://localhost:9000"
PROJECT_KEY="ccrHospitalManagement-local"

print_warning "Using default SonarQube token. For production, generate a new token!"

# Run SonarQube analysis
mvn sonar:sonar \
  -Dsonar.projectKey=${PROJECT_KEY} \
  -Dsonar.projectName="CCR Hospital Management (Local)" \
  -Dsonar.host.url=${SONAR_HOST_URL} \
  -Dsonar.token=${SONAR_TOKEN} \
  -Dsonar.sources=src/main \
  -Dsonar.tests=src/test \
  -Dsonar.java.binaries=target/classes \
  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

# Step 4: Package
print_status "Step 3/3: Packaging application..."
mvn package -DskipTests=true

cd ..

print_status "Pipeline completed successfully!"
echo ""
print_status "Results:"
echo "  - SonarQube Dashboard: http://localhost:9000"
echo "  - Project: ${PROJECT_KEY}"
echo "  - WAR file: ccrHospitalManagement/target/*.war"
echo "  - Test reports: ccrHospitalManagement/target/surefire-reports/"
echo "  - Coverage reports: ccrHospitalManagement/target/site/jacoco/"
echo ""
print_warning "To stop SonarQube: docker-compose down"
print_warning "To stop and remove volumes: docker-compose down -v"