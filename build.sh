#!/bin/bash

# Bash script to automate Maven build and package process

# Step 1: Clean and install the project
echo "Running mvn clean install..."
mvn clean install

if [ $? -ne 0 ]; then
    echo "mvn clean install failed!"
    exit $?
fi

# Step 2: Clean package and repackage the skyward-server module
echo "Running mvn clean package spring-boot:repackage for skyward-server..."
mvn -f skyward-server/pom.xml clean package spring-boot:repackage

if [ $? -ne 0 ]; then
    echo "mvn clean package spring-boot:repackage for skyward-server failed!"
    exit $?
fi

# Step 3: Build the Docker image
echo "Building Docker image for skyward-server..."
docker build --no-cache -t skyward-server:latest .

if [ $? -ne 0 ]; then
    echo "Docker build failed!"
    exit $?
fi

echo "Build and package process completed successfully!"
