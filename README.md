# SalesSavyBackend

SalesSavyBackend is the backend service for the Sales Savvy platform, providing RESTful APIs for user authentication, product management, and order processing. Built with Spring Boot and MySQL, it is designed to be robust and scalable for modern sales and customer management applications.

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Setup & Installation](#setup--installation)
- [Project Structure](#project-structure)
- [API Overview](#api-overview)
- [License](#license)

---

## About

SalesSavyBackend handles all business logic, data storage, security, and API endpoints for the Sales Savvy ecosystem. It powers the SalesSavyFrontend and any similar clients by exposing secure RESTful services.

---

## Features

- **JWT-based user authentication and authorization**
- **Role-based access control**
- **CRUD APIs** for product and order management
- **User registration and management**
- **MySQL integration**
- **Configurable with provided SQL script**
- **Spring Security integration**

---

## Tech Stack

| Category         | Technology             |
|------------------|-----------------------|
| Framework        | Spring Boot           |
| Language         | Java                  |
| Security         | Spring Security (JWT) |
| Database         | MySQL                 |
| API              | RESTful endpoints     |
| Build Tool       | Maven                 |

---

## Setup & Installation

### Prerequisites

- Java 17 (or compatible version)
- SpringBoot
- Maven
- MySQL database

### Steps

1. **Clone this repository:**

    ```
    git clone https://github.com/Chandan-39/SalesSavyBackend.git
    cd SalesSavyBackend
    ```

2. **Set up the database:**

    - Create a new database in MySQL (e.g., `salessavvy`).
    - Run the `salessavvy.sql` script in your database to create required tables.

3. **Update `application.properties`:**

    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/salessavvy
    spring.datasource.username=YOUR_DB_USERNAME
    spring.datasource.password=YOUR_DB_PASSWORD
    spring.jpa.hibernate.ddl-auto=update
    # Include other necessary properties
    ```

4. **Build and run the application:**

    ```
    ./mvnw spring-boot:run
    ```
    Or use your IDE to run the project.

---

## Project Structure

src/
├── main/
│ ├── java/
│ │ └── com/
│ │ └── yourcompany/
│ │ └── salessavy/
│ │ ├── controller/
│ │ ├── model/
│ │ ├── repository/
│ │ ├── service/
│ │ └── ...
│ └── resources/
│ ├── application.properties
│ └── salessavvy.sql
└── pom.xml

---

## API Overview

Here’s a sample of core endpoints (customize as per your actual implementation):

| Endpoint            | Method | Description                |
|---------------------|--------|----------------------------|
| `/api/auth/register`| POST   | User registration          |
| `/api/auth/login`   | POST   | User login, returns JWT    |
| `/api/products`     | GET    | List all products          |
| `/api/products`     | POST   | Add a new product          |
| `/api/orders`       | POST   | Place a new order          |
| `/api/orders`       | GET    | List all orders            |

> **Note**: Protected endpoints require JWT Bearer tokens in the `Authorization` header.

---

## License

This project is licensed under the [MIT License](./LICENSE).  
Feel free to use and modify as needed.

---

**Author:** [Chandan-39](https://github.com/Chandan-39)

