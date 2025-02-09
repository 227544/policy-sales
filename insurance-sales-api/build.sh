#!/bin/bash

# Function to remove existing files and create necessary directories
prepare_directories() {
    # Remove existing files
    rm -rf src/main/resources/tls/ca.crt
    rm -rf src/main/resources/msp/*
    rm -rf src/main/resources/credentials/*

    # Create necessary directories
    mkdir -p src/main/resources/tls
    mkdir -p src/main/resources/msp
    mkdir -p src/main/resources/credentials
    chmod -R 777 src/main/resources
}

# Function to copy necessary files
copy_files() {
    cp ../fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/peers/peer0.org1.example.com/tls/ca.crt src/main/resources/tls/ca.crt
    cp -r ../fabric-operations-console/fabric-samples/test-network/organizations/ordererOrganizations/example.com/msp/* src/main/resources/msp
    cp ../fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore/* src/main/resources/credentials/admin-key.pem
    cp ../fabric-operations-console/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/* src/main/resources/credentials/admin-cert.pem
}

# Function to build and run Docker container
build_and_run_docker() {
    # Build the project
    mvn clean package

    # Remove existing Docker container if it exists
    docker rm -f dev-insurance-sales-api || true

    # Remove existing Docker image if it exists
    docker rmi -f insurance-sales-api || true

    # Build the Docker image
    docker build -t insurance-sales-api .

    # Run the Docker container
    docker run -d --name dev-insurance-sales-api -p 8080:8080 --network fabric_test insurance-sales-api
}

# Call the functions
prepare_directories
copy_files
build_and_run_docker