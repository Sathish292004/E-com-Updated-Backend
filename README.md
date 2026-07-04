# ⚙️ Spring-Ecom — E-Com Backend

REST API backend for the [SK Store](https://github.com/Sathish292004/E-com) e-commerce platform, built with Spring Boot.

🔗 **Frontend:** [E-com](https://github.com/Sathish292004/E-com) · [Live demo](https://ecom-two.vercel.app)

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-runtime-336791?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)

## 📖 Overview

This service is the API layer for the SK Store storefront. The [frontend](https://github.com/Sathish292004/E-com) is configured to call it at `http://localhost:8080` by default (see its `.env`).

> 🚧 **Status:** Early-stage scaffold. The core Spring Boot + JPA + PostgreSQL setup is in place; business logic (controllers, entities, services for products/cart/orders/etc.) is still being built out.

## 🧰 Tech Stack

| Category | Library |
|---|---|
| ☕ Language | Java 21 |
| 🌱 Framework | Spring Boot 4.1.0 |
| 🌐 Web | Spring Web MVC (`spring-boot-starter-webmvc`) |
| 🗄️ Persistence | Spring Data JPA |
| 🐘 Database | PostgreSQL |
| ✨ Boilerplate | Lombok |
| 📊 Monitoring | Spring Boot Actuator |
| 🔨 Build Tool | Maven (with Maven Wrapper) |
| 🐳 Containerization | Docker (multi-stage build) |

## 📁 Project Structure

```
E-com-Backend/
├── .mvn/wrapper/      # Maven wrapper files
├── src/
│   ├── main/java/     # Application source
│   └── main/resources/ # application.properties, static resources
├── Dockerfile         # Multi-stage build (Maven → JRE 21)
├── mvnw / mvnw.cmd    # Maven wrapper scripts
└── pom.xml
```

## 🚀 Getting Started

### ✅ Prerequisites

- Java 21 (JDK)
- PostgreSQL (local instance or remote)
- Maven not required — the project includes the Maven Wrapper (`mvnw`)

### 🔐 Configuration

Set your database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecom
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
```

### ▶️ Running Locally

```bash
git clone https://github.com/Sathish292004/E-com-Backend.git
cd E-com-Backend

# Run with the Maven wrapper
./mvnw spring-boot:run        # macOS/Linux
mvnw.cmd spring-boot:run       # Windows

# Or build a jar and run it
./mvnw clean package -DskipTests
java -jar target/*.jar
```

The API starts on **port 8080** by default.

### 🐳 Running with Docker

```bash
docker build -t ecom-backend .
docker run -p 8080:8080 ecom-backend
```

The included `Dockerfile` does a multi-stage build: it compiles with Maven on `eclipse-temurin:21`, then runs the resulting jar on a lightweight `eclipse-temurin:21-jre` image.

### ❤️ Health Check

Spring Boot Actuator is included, so once running you can check service health at:

```
GET http://localhost:8080/actuator/health
```

## 🗺️ Roadmap

- [ ] 🛒 Product, category, and inventory entities
- [ ] 🔑 User authentication / authorization
- [ ] 🧾 Cart and order management endpoints
- [ ] 📚 API documentation (e.g. springdoc-openapi / Swagger UI)
- [ ] 🧪 Tests

## 👨‍💻 Author

**Sathish Kumar B**

🔗 GitHub: [github.com/Sathish292004](https://github.com/Sathish292004)

---

⭐ If you found this useful, consider giving the repo a star!

