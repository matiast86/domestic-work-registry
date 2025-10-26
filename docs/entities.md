# 🧱 Entity Model Documentation

## 🏠 Overview

The **Domestic Work Registry** domain model manages employment relationships between **Employers** and **Employees** through a **Contract** entity.  
Contracts define working conditions, track **Jobs**, **Attendance**, and serve as the foundation for reporting and payroll.

---

## 👤 User (Abstract)

Base authentication class implementing `UserDetails` for Spring Security.

| Field                             | Type                       | Description                |
| --------------------------------- | -------------------------- | -------------------------- |
| `id`                              | `String`                   | Primary key (UUID or hash) |
| `email`                           | `String`                   | Unique login identifier    |
| `password`                        | `String`                   | Encrypted                  |
| `role`                            | `Role`                     | Defines access level       |
| `active`                          | `boolean`                  | Activation status          |
| `resetToken` / `resetTokenExpiry` | `String` / `LocalDateTime` | Password reset logic       |
| `createdAt`                       | `LocalDateTime`            | Registration timestamp     |

Extended by `Employer` and `Employee`.

---

## 👩‍💼 Employer

| Field       | Type             | Description          |
| ----------- | ---------------- | -------------------- |
| `id`        | `String`         | Inherits from `User` |
| `dni`       | `String`         | Identification       |
| `address`   | `Address`        | Residence            |
| `contracts` | `List<Contract>` | Managed contracts    |

Relationships:  
`@OneToMany(mappedBy="employer")` → `Contract`  
`@OneToOne` → `Address`

---

## 👩‍🧹 Employee

| Field       | Type             | Description           |
| ----------- | ---------------- | --------------------- |
| `id`        | `String`         | Inherits from `User`  |
| `dni`       | `String`         | Identification        |
| `address`   | `Address`        | Residence             |
| `contracts` | `List<Contract>` | Active/past contracts |

Relationships:  
`@OneToMany(mappedBy="employee")` → `Contract`  
`@OneToOne` → `Address`

---

## 📄 Contract

Defines employment conditions.

| Field                   | Type             | Description                     |
| ----------------------- | ---------------- | ------------------------------- |
| `id`                    | `Integer`        | Primary key                     |
| `employer` / `employee` | Entities         | Relationship ends               |
| `jobType`               | `JobType`        | Role (housekeeper, nanny, etc.) |
| `employmentType`        | `EmploymentType` | Hourly or Monthly               |
| `salary`                | `BigDecimal`     | Agreed salary                   |
| `startDate` / `endDate` | `LocalDate`      | Duration                        |
| `active`                | `boolean`        | Contract status                 |

Relationships:  
`@ManyToOne` → Employer / Employee  
`@OneToOne` → Schedule  
`@OneToMany(mappedBy="contract")` → Job, AttendanceRecord

---

## ⏰ Schedule / 📅 ScheduleEntry

Define recurring workdays and hours.

| Entity          | Role                          |
| --------------- | ----------------------------- |
| `Schedule`      | Weekly structure per contract |
| `ScheduleEntry` | Single day/time entry         |

Used to pre-generate attendance records.

---

## 🧾 Job

Logs work sessions for hourly or extra hours.

| Field         | Type         | Description      |
| ------------- | ------------ | ---------------- |
| `date`        | `LocalDate`  | Work date        |
| `hoursWorked` | `BigDecimal` | Hours            |
| `hourlyFee`   | `BigDecimal` | Rate             |
| `total`       | `BigDecimal` | Calculated total |
| `notes`       | `String`     | Remarks          |

---

## 🗓️ AttendanceRecord

Tracks attendance or absences for monthly employees.

| Field           | Type               | Description         |
| --------------- | ------------------ | ------------------- |
| `scheduleEntry` | `ScheduleEntry`    | Expected work entry |
| `date`          | `LocalDate`        | Record date         |
| `status`        | `AttendanceStatus` | Attendance enum     |
| `comment`       | `String`           | Optional note       |

---

## 🧠 Enums

| Enum               | Values                                                   |
| ------------------ | -------------------------------------------------------- |
| `Role`             | EMPLOYER, EMPLOYEE, ADMIN                                |
| `EmploymentType`   | HOURLY, MONTHLY                                          |
| `JobType`          | HOUSEKEEPER, NANNY, CAREGIVER                            |
| `AttendanceStatus` | PRESENT, JUSTIFIED_ABSENCE, UNJUSTIFIED_ABSENCE, HOLIDAY |

---

**Related Docs:**  
🔗 [Architecture](architecture.md) · [Business Logic](business-logic.md)
