# ⚙️ Business Logic Documentation

## 🏠 Overview

The **Domestic Work Registry** encapsulates domain-specific logic for managing domestic employment.  
Its core processes revolve around **contract lifecycle**, **attendance and job tracking**, and **payroll calculation**.

All business operations are implemented within the **Service Layer**, ensuring clear separation from controllers and repositories.  
Transactions are managed with `@Transactional` to maintain data consistency.

---

## 🔁 Contract Lifecycle

### 1) Contract Creation

**Actors:** Employer, Employee  
**Purpose:** Formalize the employment relationship.

**Flow**

1. Employer completes contract form.
2. `ContractService.createContract()`:
   - Validates employer & employee exist.
   - Ensures no active contract already exists.
   - Sets `active = true`.
   - Persists contract + schedule if provided.

**Validations**

- Employer/employee must exist and be active.
- Salary > 0.
- Start date ≤ end date.
- Employment type mandatory (`HOURLY` or `MONTHLY`).

---

### 2) Contract Management

| Operation            | Description                                 |
| -------------------- | ------------------------------------------- |
| **Update**           | Modify salary, employment type, or schedule |
| **Deactivate**       | Mark contract inactive when it ends         |
| **Retrieve Summary** | Lightweight fetch for dashboards            |
| **Retrieve Detail**  | Full fetch for reports                      |

---

## 🧾 Job Management

### 1) Job Creation

Log work sessions (hourly or extra hours).  
`JobService.createJob()` validates contract activity, date, hours, and computes totals.

### 2) Monthly Job Reporting

Implemented in `DataCollectionService.getMonthlyJobsByContract()`.  
Filters jobs by month/year, sorts chronologically, maps DTOs, and calculates totals.

---

## 🗓️ Attendance Management

Tracks attendance for monthly employees.  
`AttendanceRecordService` ensures one record per `(entry, date)`.  
Used in payroll for deductions and bonuses.

---

## 💰 Payroll Calculation

| Step        | Description        | Formula                  |
| ----------- | ------------------ | ------------------------ |
| Base Salary | From contract      | `contract.salary`        |
| Extra Hours | From jobs          | `sum(job.total)`         |
| Deductions  | From absences      | `base / days × absences` |
| Gross       | Base + Extras      | `salary + extras`        |
| Net         | Gross - Deductions | `gross - deductions`     |

---

## 🧠 Business Rules Summary

- Employer and employee must be active.
- Jobs cannot be recorded for inactive contracts.
- Only one attendance record per entry/date.
- Expired tokens invalid for activation/reset.
- Payroll includes only active contracts.

---

**Related Docs:**  
🔗 [Entities](entities.md) · [Reports](reports.md)
