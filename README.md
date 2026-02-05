# Demo E-Shop

This project is an implementation of a simple **e-commerce system**, developed with **Spring Boot** and **Angular**.  
It supports **customers** and **shops**, **product management**, **shopping cart** functionality, and **order** processing.

The backend exposes a **REST API** and uses **session-based authentication** with role-based authorization.

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Web (REST)
- Spring Security (Session-based authentication)
- Spring Data JPA (Hibernate)
- MySQL
- Maven
- Docker & Docker Compose
- Swagger / OpenAPI
- Angular 21

---

## Features

### Authentication & Authorization

- User signup as **Customer** or **Shop**
- Login / Logout with session cookies
- Role-based access control (`CUSTOMER`, `SHOP`)

### Customer Features

- Browse and search products with filters
- View product details
- Add / update / remove items from cart
- Checkout cart and create orders
- View order history

### Shop Features

- Create, update, and delete products
- View only own products
- Stock and price management

### System Features

- Automatic database initialization with dummy data
- Transactional checkout with pessimistic locking
- Swagger UI for API testing

---

## Running the Project

```bash
docker compose up --build
```

- Backend: http://localhost:8080
- MySQL: port 3307

---

## API Documentation

Swagger UI:
http://localhost:8080/swagger-ui/index.html

---

## Demo Accounts

### Customer

- customer@demo.com / customer

### Shops

- shop1@demo.com / shop1
- shop2@demo.com / shop2
- shop3@demo.com / shop3

---

## Notes

- Database schema is auto-generated on startup
- Dummy data are inserted automatically
- Authentication is session-based (JSESSIONID)
