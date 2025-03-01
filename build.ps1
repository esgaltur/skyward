# PowerShell script to automate Maven build and package process

# Step 1: Clean and install the project
Write-Host "Running mvn clean install..."
mvn clean install

if ($LASTEXITCODE -ne 0) {
    Write-Host "mvn clean install failed!"
    exit $LASTEXITCODE
}

# Step 2: Clean package and repackage the skyward-server module
Write-Host "Running mvn clean package spring-boot:repackage for skyward-server..."
mvn -f skyward-server/pom.xml clean package spring-boot:repackage

if ($LASTEXITCODE -ne 0) {
    Write-Host "mvn clean package spring-boot:repackage for skyward-server failed!"
    exit $LASTEXITCODE
}


docker build --no-cache -t skyward-server:latest .


Write-Host "Build and package process completed successfully!"

