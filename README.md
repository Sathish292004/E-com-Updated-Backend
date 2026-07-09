# ⚙️ Spring-Ecom — E-Com Backend (Updated)

Full-featured REST API backend for the **SK Store** e-commerce platform, built with Spring Boot. This is the fleshed-out version of the backend — JWT + Google OAuth2 auth, an admin panel, and full product/cart/order/review flows are all wired up end-to-end.

🔗 **Frontend:** [E-com-new](https://github.com/Sathish292004/E-comUpdatedVersion)  🌐[Live demo](https://sk-store-drab.vercel.app)

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-6DB33F?logo=springboot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-runtime-336791?logo=postgresql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT%20%2B%20Google%20OAuth2-000000?logo=jsonwebtokens&logoColor=white)
![Swagger](https://img.shields.io/badge/API%20Docs-Swagger%20UI-85EA2D?logo=swagger&logoColor=black)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)

## 📖 Overview
This service is the API layer for the SK Store storefront. Customers can browse products/categories, manage a cart and wishlist, place and track orders, leave reviews, and manage their profile/addresses. Admins get a separate login and dashboard for managing customers, orders, and reviews.

> ✅ **Status:** Actively developed. Core commerce flows (auth, catalog, cart, wishlist, orders, reviews, addresses) are implemented end-to-end; automated test coverage is still minimal.

## 🧰 Tech Stack
| Category | Library |
|---|---|
| ☕ Language | Java 21 |
| 🌱 Framework | Spring Boot 4.1.0 |
| 🌐 Web | Spring Web MVC (`spring-boot-starter-webmvc`) |
| 🗄️ Persistence | Spring Data JPA (+ JPA Specifications for filtering) |
| 🐘 Database | PostgreSQL |
| 🔐 Security | Spring Security, BCrypt password hashing |
| 🔑 Auth | JWT (`jjwt` 0.12.7) + Google OAuth2 login |
| 📜 API Docs | springdoc-openapi / Swagger UI |
| ✅ Validation | Spring Boot Validation |
| ✨ Boilerplate | Lombok |
| 📊 Monitoring | Spring Boot Actuator |
| 🔨 Build Tool | Maven (with Maven Wrapper) |
| 🐳 Containerization | Docker (multi-stage build) |
| ⚙️ CI/CD | GitHub Actions → Azure Web App |

## 📁 Project Structure
```
E-com-Updated-Backend/
├── .github/workflows/         # CI/CD — build & deploy to Azure Web App
├── .mvn/wrapper/               # Maven wrapper files
├── src/
│   ├── main/java/Sathish292004/
│   │   ├── config/             # Security, CORS, Swagger, password encoder
│   │   ├── controller/         # REST controllers
│   │   ├── dto/
│   │   │   ├── request/        # Request payloads
│   │   │   └── response/       # Response payloads
│   │   ├── exception/          # Custom exceptions + global handler
│   │   ├── model/               # JPA entities
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── security/            # JWT filter/service, OAuth2 success handler
│   │   ├── service/             # Business logic
│   │   ├── specification/      # Dynamic product filtering (JPA Specifications)
│   │   └── SpringEcomApplication.java
│   ├── main/resources/
│   │   └── application.properties
│   └── test/java/Sathish292004/
├── Dockerfile                  # Multi-stage build (Maven → JRE 21)
├── mvnw / mvnw.cmd              # Maven wrapper scripts
└── pom.xml
```

## 🚀 Getting Started

### ✅ Prerequisites
- Java 21 (JDK)
- PostgreSQL (local instance or remote)
- A Google OAuth2 Client ID/Secret (see note below)
- Maven not required — the project includes the Maven Wrapper (`mvnw`)

### 🔐 Configuration
This app reads config from **environment variables**, not hardcoded defaults in `application.properties`. All five must be set, or Spring Boot will fail to start (the placeholders can't resolve otherwise):

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/ecom` |
| `DB_USERNAME` | PostgreSQL username |
| `DB_PASSWORD` | PostgreSQL password |
| `GOOGLE_CLIENT_ID` | Google OAuth2 client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 client secret |

```bash
export DB_URL=jdbc:postgresql://localhost:5432/ecom
export DB_USERNAME=your_db_username
export DB_PASSWORD=your_db_password
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
```
Hibernate is set to `ddl-auto=update`, so tables are created/updated automatically on startup.

### ▶️ Running Locally
```bash
git clone https://github.com/Sathish292004/E-com-Updated-Backend.git
cd E-com-Updated-Backend

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
docker run -p 8080:8080 \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/ecom \
  -e DB_USERNAME=your_db_username \
  -e DB_PASSWORD=your_db_password \
  -e GOOGLE_CLIENT_ID=your_google_client_id \
  -e GOOGLE_CLIENT_SECRET=your_google_client_secret \
  ecom-backend
```
The `Dockerfile` does a multi-stage build: it compiles with Maven on `maven:3.9.8-eclipse-temurin-21`, then runs the resulting jar on a lightweight `eclipse-temurin:21-jre` image.

### 📚 API Docs
Interactive Swagger UI (with a "bearer token" authorize button for JWTs) is available once the app is running:
```
http://localhost:8080/swagger-ui.html
```
Raw OpenAPI spec: `GET http://localhost:8080/v3/api-docs`

### ❤️ Health Check
```
GET http://localhost:8080/actuator/health
```

## 🔑 Authentication & Roles
There are two user types, distinguished by role (`CUSTOMER` / `ADMIN`) and enforced with Spring Security:

| Flow | Endpoint | Notes |
|---|---|---|
| Customer register | `POST /api/auth/register` | Creates a customer, password hashed with BCrypt |
| Customer login | `POST /api/auth/login` | Returns a JWT |
| Customer Google login | `GET /oauth2/authorization/google` | Auto-creates a customer on first login, redirects to the frontend with a JWT |
| Admin login | `POST /api/admin/login` | Returns a JWT with the `ADMIN` role |

Protected endpoints expect:
```
Authorization: Bearer <token>
```

## 🗂️ API Modules
| Module | Base path | Access |
|---|---|---|
| Customer auth | `/api/auth/**` | Public |
| Admin auth | `/api/admin/login` | Public |
| Products & categories | `/api/products/**`, `/api/product/**`, `/api/categories`, `/api/category/**` | Public browsing/search |
| Cart, wishlist, addresses, profile | `/api/customer/**` | `CUSTOMER` role |
| Orders | `/api/orders/**` | Authenticated |
| Reviews | `/api/products/{id}/reviews`, `/api/customer/products/{id}/reviews` | Reading needs auth; posting needs `CUSTOMER` role |
| Admin dashboard, customers, orders, reviews | `/api/admin/**` | `ADMIN` role |

## 🗺️ Roadmap
- [x] 🛒 Product, category, cart, wishlist & order entities
- [x] 🔑 JWT + Google OAuth2 authentication, role-based access
- [x] 🧾 Cart, wishlist, and order management endpoints
- [x] 📚 API documentation via Swagger / OpenAPI
- [ ] 🧪 Real test coverage (currently just a context-load smoke test)
- [ ] 💳 Payment gateway integration
- [ ] 🖼️ Move product images out of the DB into object storage

## 👨‍💻 Author

**Sathish Kumar B**

🔗 GitHub: [github.com/Sathish292004](https://github.com/Sathish292004)

---
⭐ If you found this useful, consider giving the repo a star!
