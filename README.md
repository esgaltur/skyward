# Skyward Project

## Overview
Skyward is a comprehensive project focused on integrating and analyzing data using various technologies. It includes multiple modules such as server setup, data tracing, and API integration. Note: Several improvements are required, particularly in security aspects like password storage.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Modules](#modules)
- [Security Improvements](#security-improvements)
- [License](#license)

## Installation
1. **Clone the repository**:
    ```bash
    git clone https://github.com/esgaltur/skyward.git
    cd skyward
    ```

2. **Build the project**:
   For Unix-based systems:
    ```bash
    ./build.sh
    ```
   For Windows systems:
    ```powershell
    ./build.ps1
    ```

3. **Start the services**:
    ```bash
    docker-compose up
    ```

## Usage
After starting the services, interact with the Skyward APIs and data integration tools. Main components include:

- **Skyward Server**: Hosts the main backend services.
- **Skyward Tracing**: Provides data tracing capabilities.
- **Skyward OpenAPI**: Exposes the API endpoints.

## Modules
- **design**: Contains design documents and resources.
- **skyward-data**: Handles data-related operations.
- **skyward-openapi**: Manages API definitions and documentation.
- **skyward-server**: Core server functionalities.
- **skyward-tracing**: Implements tracing mechanisms.
- **skyward_integration**: Integrates various components and services.

## Security Improvements
Several enhancements are required to ensure secure operations:

- **Secure Communication**: Use HTTPS for all communications. Ensure the Spring Boot application is configured to use SSL.
    ```yaml
    server:
      port: 8443
      ssl:
        enabled: true
        key-store: classpath:keystore.p12
        key-store-password: changeit
        key-store-type: PKCS12
        key-alias: undertow
    ```
    ```

- **Regular Security Audits**: Conduct regular audits and update dependencies to patch vulnerabilities. Utilize tools like OWASP Dependency-Check to monitor for known security issues in dependencies.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
For more detailed information, refer to the specific module documentation within the repository.
