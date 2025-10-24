### 🏠 Overview

**Domestic Work Registry** is a web application built with **Spring Boot**, **Thymeleaf**, and **MySQL**, designed to register, manage, and formalize domestic employment relationships between employers and employees.

It allows users to:

- Register as an **Employer** or **Employee**.
- Create and manage **Contracts** defining job type, employment type, salary, and schedule.
- Track **Jobs**, **Attendance**, and generate **Monthly Reports**.
- Manage **Account activation** and **Password recovery** via secure email tokens.

The project follows a **layered architecture** ensuring scalability, maintainability, and clean separation of concerns.

---

### ⚙️ Tech Stack

| Layer      | Technology                                                            |
| ---------- | --------------------------------------------------------------------- |
| Backend    | Java 24, Spring Boot 3, Spring Data JPA, Spring Security, Spring Mail |
| View       | Thymeleaf, Bootstrap 5                                                |
| Database   | MySQL                                                                 |
| Build Tool | Maven                                                                 |
| Mapping    | MapStruct                                                             |
| Auth       | Custom login + activation + password reset via email                  |
| Other      | Lombok, Jakarta Validation, EntityGraph optimization                  |

---

### 🧩 Architecture

The system follows a **three-layer architecture**:

```
┌────────────────────────────────────┐
│           Presentation Layer        │
│  (Controllers + Thymeleaf Views)    │
└────────────────────────────────────┘
               │
               ▼
┌────────────────────────────────────┐
│            Service Layer            │
│   (Business logic, transactions)   │
└────────────────────────────────────┘
               │
               ▼
┌────────────────────────────────────┐
│           Data Access Layer         │
│ (JPA Repositories, EntityGraphs)   │
└────────────────────────────────────┘
```

- **Controllers** handle routes and form submissions.
- **Services** encapsulate business logic and transaction management.
- **Repositories** provide persistence with JPA and custom queries.
- **DTOs & Mappers (MapStruct)** decouple entities from views.

---

### 🧱 Domain Model

| Entity                       | Description                                                                                                                           |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| **User (abstract)**          | Base authentication entity implementing `UserDetails`. Holds credentials, roles, and activation/reset tokens.                         |
| **Employer / Employee**      | Domain-specific extensions of User. Contain personal details, contact info, and address.                                              |
| **Address**                  | Separate entity to normalize geographic data.                                                                                         |
| **Contract**                 | Core entity linking Employer ↔ Employee. Stores type, salary, start/end dates, employment mode, and references to schedules and jobs. |
| **Schedule / ScheduleEntry** | Defines work days and hours for contracts.                                                                                            |
| **Job**                      | Represents individual work sessions or additional hours. Used for monthly/annual reports.                                             |
| **AttendanceRecord**         | Marks employee presence, justification, or holidays for monthly workers.                                                              |
| **Payslip / Report DTOs**    | Generated summaries and financial data for reporting.                                                                                 |

---

### 🔐 Security Overview

- **Spring Security** with role-based access (`EMPLOYER`, `EMPLOYEE`, `ADMIN`).
- **Custom authentication** using `UserDetailsService` and `DaoAuthenticationProvider`.
- **Account Activation** via tokenized email link.
- **Password Reset** with expiration-based token validation.
- **Custom login page and redirect behavior** for first-time logins and role-based routing.

---

### 💾 Database Schema

Main relationships:

```
Employer ──< Contract >── Employee
                    │
                    ├─< Schedule
                    │     └─< ScheduleEntry
                    │
                    ├─< Job
                    └─< AttendanceRecord
```

- One **Contract** binds a single Employer and Employee.
- Each Contract has a **Schedule** and multiple **Jobs** or **AttendanceRecords**.
- Jobs feed data to monthly/annual reports.

---

### 🧮 Business Logic Highlights

| Feature                 | Description                                                       |
| ----------------------- | ----------------------------------------------------------------- |
| **Contract Management** | Employers can create and update active contracts.                 |
| **Attendance Tracking** | Records employee presence or absence per scheduled entry.         |
| **Job Reporting**       | Calculates totals, averages, and monthly summaries per contract.  |
| **Payroll Logic**       | Aggregates payments based on schedule, absences, and extra hours. |
| **Email Integration**   | Sends activation and reset password links.                        |

---

### 🚀 Getting Started

#### Prerequisites

- Java 21+ (Java 24 recommended)
- Maven 3.9+
- MySQL 8+
- Environment variables configured

#### Environment Variables

Create a `.env` or system variables with:

```
DB_PORT=3306
DB_NAME=domestic-work-registry
DB_USERNAME=root
DB_PASSWORD=root
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

#### Run the app

```bash
mvn spring-boot:run
```

Visit [http://localhost:8080](http://localhost:8080)

---

### 🧠 Testing Notes

- Use sample data in `data.sql` or insert test entities manually.
- Spring profiles available for `dev` and `prod`.
- Unit tests under `/src/test/java` cover services and repositories.

---

### 🧰 Useful Commands

| Command                                     | Description                  |
| ------------------------------------------- | ---------------------------- |
| `mvn clean install`                         | Build and run tests          |
| `mvn spring-boot:run`                       | Start the development server |
| `mvn package`                               | Create a runnable `.jar`     |
| `java -jar target/domesticworkregistry.jar` | Run packaged app             |

---

### 🌱 Future Improvements

- REST API endpoints (for React/PWA integration)
- Admin dashboard
- Multi-language support
- Payroll generation (PDF payslips)
- Integration with government registry or tax APIs

---

### 👨‍💻 Author

**Matías Tailler**
Full Stack Developer — Java · Spring Boot · React · Node · Nest.js
📧 [matiastailler@gmail.com](mailto:matiastailler@gmail.com)
🌐 [GitHub](https://github.com/matiast86)

---
