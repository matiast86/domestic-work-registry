# 🏗️ Architecture Overview

The **Domestic Work Registry** is designed as a **layered monolithic application** built with **Spring Boot 3**, emphasizing **separation of concerns**, **maintainability**, and **scalability**.

It integrates:

- **Spring MVC** → Request handling & Thymeleaf rendering
- **Spring Data JPA** → Persistence abstraction
- **Spring Security** → Authentication & authorization
- **Spring Mail** → Transactional email delivery

---

## ⚙️ High-Level Architecture

```

┌────────────────────────────────────────────────────┐
│ Presentation Layer (Controllers + Thymeleaf)       │
└────────────────────────────────────────────────────┘
│
▼
┌────────────────────────────────────────────────────┐
│ Service Layer (Business Logic + Transactions)      │
└────────────────────────────────────────────────────┘
│
▼
┌────────────────────────────────────────────────────┐
│ Data Access Layer (Repositories + EntityGraphs)    │
└────────────────────────────────────────────────────┘
│
▼
┌────────────────────────────────────────────────────┐
│ Database (MySQL 8 - Normalized Schema)             │
└────────────────────────────────────────────────────┘

```



## 🧱 Layer Responsibilities

| Layer            | Responsibility                                                                             |
| ---------------- | ------------------------------------------------------------------------------------------ |
| **Presentation** | Handles HTTP requests, DTO binding, form validation, and renders Thymeleaf templates.      |
| **Service**      | Encapsulates business logic, manages transactions, validation, reporting, and email flows. |
| **Data Access**  | Provides repository interfaces and custom queries.                                         |
| **Database**     | Defines normalized schema and entity relationships.                                        |

---

## 🧩 Key Packages

```

controller/      → Web controllers
service/         → Business logic & data aggregation
repository/      → Data persistence layer
entities/        → Domain model
dto/mapper/      → Transfer objects & MapStruct mappers
config/          → Security and app configuration
exceptions/      → Custom exception handlers

```

## 🧠 Design Decisions

| Decision                        | Rationale                                  |
| ------------------------------- | ------------------------------------------ |
| **Layered architecture**        | Enforces clean separation between concerns |
| **EntityGraph usage**           | Prevents N+1 query issues                  |
| **DTO mapping**                 | Keeps entities lightweight                 |
| **Transactional Service Layer** | Guarantees atomic database operations      |
| **Localized messages**          | Supports Spanish-Argentina user base       |

**Related Docs:**  
🔗 [Entity Model](entities.md) · [Business Logic](business-logic.md) · [Security Overview](security.md)
