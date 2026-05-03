# 📚 Bookstore E-Commerce — Microservices Architecture

A full-stack **Bookstore E-Commerce** platform built using **Spring Boot 3.x** and **Spring Cloud** following a microservices architecture. Each service is independently deployable, owns its own database, and communicates via REST (Feign) or events (Kafka).

---

## 🏗️ Architecture Overview

```
Client → API Gateway (8080)
           ↓
    ┌──────────────────────────────────────────┐
    │           Spring Cloud Eureka             │
    │         Service Discovery (8761)          │
    └──────────────────────────────────────────┘
           ↓
    ┌─────────────────────────────────────────────────────────┐
    │  User  │ Product │  Cart  │ Order │ Feedback │ Wishlist │
    │  8081  │  8083   │  8084  │ 8087  │   8088   │   8085   │
    └─────────────────────────────────────────────────────────┘
           ↓
    ┌──────────────────────────────────────────┐
    │         Apache Kafka Event Bus            │
    │    order-events / user-events topics      │
    └──────────────────────────────────────────┘
           ↓
    ┌──────────────────────────────────────────┐
    │        Notification Service (8089)        │
    └──────────────────────────────────────────┘
```

---

## 🧩 Microservices

### 1. 🔐 User Service — Port `8081`
Handles user registration, login, JWT generation, and profile management. Acts as the authentication authority for the entire platform.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/users/register` | No | Register a new user |
| POST | `/api/users/login` | No | Login and receive JWT |
| GET | `/api/users/profile` | JWT | Get user profile |
| PUT | `/api/users/profile` | JWT | Update user profile |
| PUT | `/api/users/change-password` | JWT | Change password |
| DELETE | `/api/users/{id}` | ADMIN | Delete a user |

**Database:** `bookstore_user` (MySQL)

---

### 2. 🛡️ Admin Service — Port `8082`
Manages admin accounts, role assignments, and provides admin-only endpoints. Calls Product Service via Feign Client.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/admin/register` | SUPER_ADMIN | Create admin account |
| GET | `/api/admin/all-users` | ADMIN | List all users |
| PUT | `/api/admin/products/{id}` | ADMIN | Update a product |
| DELETE | `/api/admin/products/{id}` | ADMIN | Delete a product |
| GET | `/api/admin/orders` | ADMIN | View all orders |
| PUT | `/api/admin/orders/{id}/status` | ADMIN | Update order status |

**Database:** `bookstore_admin` (MySQL)

---

### 3. 📦 Product Service — Port `8083`
Full CRUD for books/products including category management, inventory tracking, and image URL storage.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/products` | No | List all products (paginated) |
| GET | `/api/products/{id}` | No | Get product by ID |
| GET | `/api/products/search?q=` | No | Search products |
| GET | `/api/products/category/{id}` | No | Filter by category |
| POST | `/api/products` | ADMIN | Create a product |
| PUT | `/api/products/{id}` | ADMIN | Update a product |
| DELETE | `/api/products/{id}` | ADMIN | Delete a product |
| GET | `/api/categories` | No | List all categories |
| POST | `/api/categories` | ADMIN | Create a category |

**Database:** `bookstore_product` (MySQL)

---

### 4. 🛒 Cart Service — Port `8084`
Manages shopping cart sessions stored in **Redis** for ultra-fast read/write. Validates product existence via Feign Client.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/cart` | JWT | Get current cart |
| POST | `/api/cart/add` | JWT | Add item to cart |
| PUT | `/api/cart/update` | JWT | Update item quantity |
| DELETE | `/api/cart/remove/{productId}` | JWT | Remove item |
| DELETE | `/api/cart/clear` | JWT | Clear entire cart |
| GET | `/api/cart/total` | JWT | Calculate cart total |

**Database:** Redis (in-memory)

---

### 5. ❤️ WishList Service — Port `8085`
Persists user wish lists in MySQL. Each user can maintain one wish list with multiple products.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/wishlist` | JWT | Get wish list |
| POST | `/api/wishlist/add/{productId}` | JWT | Add to wish list |
| DELETE | `/api/wishlist/remove/{productId}` | JWT | Remove from wish list |
| DELETE | `/api/wishlist/clear` | JWT | Clear wish list |

**Database:** `bookstore_wishlist` (MySQL)

---

### 6. 👤 Customer Details Service — Port `8086`
Stores and manages delivery addresses, phone numbers, and personal preferences for registered users.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/customers/details` | JWT | Get customer profile |
| POST | `/api/customers/details` | JWT | Create profile |
| PUT | `/api/customers/details` | JWT | Update profile |
| POST | `/api/customers/addresses` | JWT | Add address |
| GET | `/api/customers/addresses` | JWT | List addresses |
| DELETE | `/api/customers/addresses/{id}` | JWT | Delete address |
| PUT | `/api/customers/addresses/{id}/default` | JWT | Set default address |

**Database:** `bookstore_customer` (MySQL)

---

### 7. 📋 Order Service — Port `8087`
Handles the full order lifecycle — placement, status transitions, and history. Publishes **Kafka events** on order placement and status change.

**Order Status Flow:**
```
PENDING → CONFIRMED → PROCESSING → SHIPPED → DELIVERED
                                           ↘ CANCELLED
```

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/orders` | JWT | Place a new order |
| GET | `/api/orders` | JWT | Get user orders |
| GET | `/api/orders/{id}` | JWT | Get order by ID |
| PUT | `/api/orders/{id}/cancel` | JWT | Cancel order |
| GET | `/api/orders/all` | ADMIN | List all orders |
| PUT | `/api/orders/{id}/status` | ADMIN | Update order status |

**Database:** `bookstore_order` (MySQL)
**Events Published:** `order-events` Kafka topic

---

### 8. ⭐ Feedback Service — Port `8088`
Allows users to submit product reviews and star ratings. Calculates aggregate ratings per product.

**Endpoints:**
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/feedback` | JWT | Submit a review |
| GET | `/api/feedback/product/{id}` | No | Get reviews for product |
| GET | `/api/feedback/product/{id}/rating` | No | Get average rating |
| PUT | `/api/feedback/{id}` | JWT | Edit own review |
| DELETE | `/api/feedback/{id}` | JWT/ADMIN | Delete a review |

**Database:** `bookstore_feedback` (MySQL)

---

### 9. 🔔 Notification Service — Port `8089`
Listens to Kafka topics and sends transactional emails/SMS. Purely event-driven — no REST endpoints.

**Kafka Topics Consumed:**
| Topic | Event | Action |
|-------|-------|--------|
| `order-events` | ORDER_PLACED | Send order confirmation |
| `order-events` | ORDER_SHIPPED | Send shipping update |
| `order-events` | ORDER_DELIVERED | Send delivery confirmation |
| `order-events` | ORDER_CANCELLED | Send cancellation email |
| `user-events` | USER_REGISTERED | Send welcome email |

**Database:** None

---

## 🏗️ Infrastructure Services

### Eureka Server — Port `8761`
Service discovery for all microservices. Every service registers itself on startup.

### Config Server — Port `8888`
Centralises all `application.yml` files. Each microservice fetches its config on startup.

### API Gateway — Port `8080`
Single entry point for all client requests. Handles routing, JWT validation, rate limiting, and CORS.

---

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3.x |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway |
| Config Management | Spring Cloud Config Server |
| Auth / Security | Spring Security + JWT (jjwt 0.12.3) |
| Messaging | Apache Kafka |
| Databases | MySQL 8.x |
| Cache | Redis 7.x |
| Inter-Service Calls | OpenFeign |
| Containerization | Docker + Docker Compose |
| Documentation | SpringDoc OpenAPI 3 / Swagger UI |
| Build Tool | Maven |

---

## 🔐 Security

- JWT-based stateless authentication
- Role-based access control: `GUEST`, `USER`, `ADMIN`, `SUPER_ADMIN`
- JWT validated at API Gateway before forwarding to services
- No session state — fully stateless architecture

---

## 🚀 Running Locally

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.x
- Redis 7.x
- Docker (for Kafka)
- Git

### Step 1 — Start Infrastructure
```bash
docker run -d --name kafka -p 9092:9092 -e KAFKA_NODE_ID=0 -e KAFKA_PROCESS_ROLES=controller,broker -e KAFKA_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093 -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 -e KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT -e KAFKA_CONTROLLER_QUORUM_VOTERS=0@localhost:9093 -e KAFKA_CONTROLLER_LISTENER_NAMES=CONTROLLER apache/kafka:3.7.0
```

### Step 2 — Create Databases
```sql
CREATE DATABASE bookstore_user;
CREATE DATABASE bookstore_admin;
CREATE DATABASE bookstore_product;
CREATE DATABASE bookstore_wishlist;
CREATE DATABASE bookstore_customer;
CREATE DATABASE bookstore_order;
CREATE DATABASE bookstore_feedback;
```

### Step 3 — Start Services in Order
```
1.  eureka-server             (port 8761)
2.  config-server             (port 8888)
3.  api-gateway               (port 8080)
4.  user-service              (port 8081)
5.  admin-service             (port 8082)
6.  product-service           (port 8083)
7.  cart-service              (port 8084)
8.  wishlist-service          (port 8085)
9.  customer-service          (port 8086)
10. order-service             (port 8087)
11. feedback-service          (port 8088)
12. notification-service      (port 8089)
```

### Step 4 — Access Swagger UI
Each service exposes Swagger at:
```
http://localhost:{PORT}/swagger-ui.html
```

---

## 📁 Project Structure

```
bookstore-microservices/
├── eureka-server/
│   └── src/
├── config-server/
│   └── src/
├── api-gateway/
│   └── src/
├── common-lib/
│   └── src/
├── user-service/
│   └── src/
├── admin-service/
│   └── src/
├── product-service/
│   └── src/
├── cart-service/
│   └── src/
├── wishlist-service/
│   └── src/
├── customer-service/
│   └── src/
├── order-service/
│   └── src/
├── feedback-service/
│   └── src/
├── notification-service/
│   └── src/
└── docker-compose.yml
```

---

## 🔄 Inter-Service Communication

### Synchronous (Feign Client)
- Cart Service → Product Service (validate stock)
- Order Service → Cart Service (get cart, clear cart)
- Admin Service → Product Service (update/delete products)

### Asynchronous (Kafka)
| Topic | Producer | Consumer |
|-------|----------|----------|
| `order-events` | Order Service | Notification Service |
| `user-events` | User Service | Notification Service |

---

## 🔀 Git Flow Branching Strategy

```
main        ← README only (production)
develop     ← all merged service code
feature/*   ← individual service branches
```

| Branch | Purpose |
|--------|---------|
| `main` | Production branch — README only |
| `develop` | Integration branch — all merged features |
| `feature/user-service` | User service code |
| `feature/product-service` | Product service code |
| `feature/cart-service` | Cart service code |
| `feature/order-service` | Order service code |
| `feature/feedback-service` | Feedback service code |
| `feature/wishlist-service` | Wishlist service code |
| `feature/customer-service` | Customer service code |
| `feature/notification-service` | Notification service code |
| `feature/admin-service` | Admin service code |
| `feature/api-gateway` | API Gateway code |
| `feature/eureka-server` | Eureka Server code |
| `feature/config-server` | Config Server code |
| `feature/common-lib` | Shared common library |
| `feature/infrastructure` | Docker Compose setup |

---

## 👩‍💻 Author

Shivani S
