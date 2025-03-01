# Skyward Project

## Overview

Skyward is an example implementation of a REST API server focused on user and project management. It
demonstrates various functionalities including logging, tracing, monitoring, and authentication
using JWT. The project is modular, with components for server setup, data tracing, and API
integration.

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

After starting the services, interact with the Skyward APIs and data integration tools. Main
parts include:

- **Skyward Server**: Hosts the main backend services.
- **Skyward Tracing**: Provides data tracing capabilities.
- **Skyward OpenAPI**: Exposes the API endpoints.

## Modules

- **design**: Contains design documents and resources.
- **skyward-data**: Handles data-related operations.
- **skyward-openapi**: Manages API definitions and documentation.
- **skyward-server**: Core server functionalities.
- **skyward-tracing**: Implements tracing mechanisms.
- **skyward_integration**: Integration tests written in Python

## Security Improvements

Several enhancements are required to ensure secure operations:

- **Secure Communication**: Use HTTPS for all communications. Ensure the Spring Boot application is
  configured to use SSL.
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

- **Environment Variables**: Store sensitive information such as passwords in environment variables
  instead of hardcoding them in the source code. This can be done using an `.env` file and
  configuring the application to load these variables.

  Example `.env` file:
    ```dotenv
    DB_PASSWORD=your_secure_password
    ```

  Example configuration in a Spring Boot application:
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/yourdatabase
        username: yourusername
        password: ${DB_PASSWORD}
    ```

  Ensure the `.env` file is added to `.gitignore` to prevent it from being committed to the
  repository:
    ```gitignore
    # Environment Variables
    .env
    ```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---
For more detailed information, refer to the specific module documentation within the repository.
```
