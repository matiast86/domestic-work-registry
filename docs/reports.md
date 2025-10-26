# 📊 Reports & Data Collection

## 🧠 Overview

The **reporting system** in Domestic Work Registry centralizes information about **jobs**, **attendance**, and **payroll calculations** for each contract.  
It allows employers to review work done in a given month or year and ensures transparent tracking of hours, payments, and absences.

All reports are generated through the **`DataCollectionService`**, which aggregates data from multiple related entities and outputs DTOs used by the presentation layer.

---

## ⚙️ Main Objectives

- Provide **monthly summaries** of jobs performed.
- Offer **annual overviews** of total work and payments.
- Serve as input for **payslip (payroll) generation**.
- Ensure data accuracy via centralized logic in the service layer.
- Deliver results ready to render in **Thymeleaf** templates.

---

## 🧩 Core Components

| Component               | Purpose                                                  |
| ----------------------- | -------------------------------------------------------- |
| `DataCollectionService` | Central service performing aggregations and calculations |
| `JobsMonthlyReportDto`  | Main DTO for monthly reports                             |
| `JobsMonthlyTableDto`   | Represents each job row within a report                  |
| `PayslipDto`            | (Future) Aggregated data for payroll                     |
| `ContractRepository`    | Provides pre-fetched data via `@EntityGraph`             |
| `Job` entity            | Source of work-hour and payment data                     |

---

## 📅 Monthly Job Report

### Purpose

Shows all jobs recorded for a given **contract**, **month**, and **year**, including totals and averages.

### Flow

1. Employer selects a contract and date range (month/year).
2. Controller calls:
   ```java
   getMonthlyJobsByContract(contractId, year, month)
   ```

````

3. Service fetches contract with jobs using `@EntityGraph`:

   ```java
   @EntityGraph(attributePaths = {
       "employee",
       "employee.address",
       "jobs",
       "jobs.contract",
       "jobs.contract.schedule",
       "jobs.contract.schedule.entries"
   })
   Optional<Contract> findDetailById(Integer id);
   ```

4. Jobs are **filtered**, **sorted**, and **mapped** to DTOs.
5. Totals and averages are computed.
6. Thymeleaf renders the results in a tabular view.

---

### Implementation Example

```java
@Override
public JobsMonthlyReportDto getMonthlyJobsByContract(int contractId, int year, int month) {
    Contract contract = contractService.findById(contractId);
    List<Job> jobs = contract.getJobs().stream()
        .filter(job -> job.getDate().getYear() == year && job.getDate().getMonthValue() == month)
        .sorted(Comparator.comparing(Job::getDate))
        .toList();

    List<JobsMonthlyTableDto> tables = jobs.stream()
        .map(job -> tableMapper.toDo(job))
        .toList();

    BigDecimal total = dataCollectionService.calculateAverage(jobs);

    JobsMonthlyReportDto report = new JobsMonthlyReportDto();
    report.setMonth(getMonthNameInSpanish(month));
    report.setJobs(tables);
    report.setTotal(total);
    return report;
}
```

---

### DTO Structure

#### `JobsMonthlyTableDto`

| Field         | Type         | Description      |
| ------------- | ------------ | ---------------- |
| `date`        | `LocalDate`  | Day worked       |
| `hoursWorked` | `BigDecimal` | Hours performed  |
| `hourlyFee`   | `BigDecimal` | Hourly rate      |
| `subtotal`    | `BigDecimal` | Hours × Fee      |
| `notes`       | `String`     | Optional remarks |

#### `JobsMonthlyReportDto`

| Field          | Type                        | Description                       |
| -------------- | --------------------------- | --------------------------------- |
| `contractId`   | `Integer`                   | Contract reference                |
| `month`        | `String`                    | Month name (localized)            |
| `year`         | `int`                       | Year                              |
| `jobs`         | `List<JobsMonthlyTableDto>` | Job entries                       |
| `total`        | `BigDecimal`                | Monthly total payment             |
| `averageHours` | `BigDecimal`                | (Optional) Average hours per week |

---

### Thymeleaf Integration

Rendered in `templates/jobs/jobs-monthly.html`:

```html
<table class="table table-striped mt-3">
  <thead>
    <tr>
      <th>Fecha</th>
      <th>Horas</th>
      <th>Tarifa</th>
      <th>Total</th>
      <th>Notas</th>
    </tr>
  </thead>
  <tbody>
    <tr th:each="job : ${report.jobs}">
      <td th:text="${#temporals.format(job.date, 'dd/MM/yyyy')}"></td>
      <td th:text="${job.hoursWorked}"></td>
      <td
        th:text="${#numbers.formatDecimal(job.hourlyFee, 1, 'COMMA', 2, 'POINT')}"
      ></td>
      <td
        th:text="${#numbers.formatDecimal(job.subtotal, 1, 'COMMA', 2, 'POINT')}"
      ></td>
      <td th:text="${job.notes}"></td>
    </tr>
  </tbody>
  <tfoot>
    <tr class="fw-bold">
      <td colspan="3">Total</td>
      <td
        th:text="${#numbers.formatDecimal(report.total, 1, 'COMMA', 2, 'POINT')}"
      ></td>
      <td></td>
    </tr>
  </tfoot>
</table>
```

**Formatting:**

- Dates localized to Spanish (`dd/MM/yyyy`).
- Numeric fields formatted with commas and points.

---

## 📈 Annual Report (Planned Feature)

### Purpose

Summarize a full year’s work and earnings per contract.

### Aggregations

| Metric                 | Description            |
| ---------------------- | ---------------------- |
| `totalHoursPerMonth`   | Hours worked per month |
| `totalIncomePerMonth`  | Income per month       |
| `averageMonthlyIncome` | Mean monthly income    |
| `annualTotal`          | Yearly total           |

**Example Implementation**

```java
public AnnualReportDto getAnnualJobsByContract(int contractId, int year) {
    Contract contract = contractService.findById(contractId);
    Map<Integer, List<Job>> grouped = contract.getJobs().stream()
        .filter(job -> job.getDate().getYear() == year)
        .collect(Collectors.groupingBy(job -> job.getDate().getMonthValue()));

    return aggregateByMonth(grouped);
}
```

---

## 🧮 DataCollectionService Utilities

Central service for all job, attendance, and payroll aggregations.

| Method                                          | Description                     |
| ----------------------------------------------- | ------------------------------- |
| `filterJobs(List<Job>, Predicate<Job>)`         | Filter jobs dynamically         |
| `sumJobs(List<Job>, Function<Job, BigDecimal>)` | Sum a mapped property           |
| `calculateAverage(List<Job>)`                   | Compute monthly/weekly averages |
| `groupByMonth(List<Job>)`                       | Group for annual reports        |

**Constants**

```java
private static final int SCALE = 2;
private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;
private static final BigDecimal AVERAGE_WEEKS_PER_MONTH = BigDecimal.valueOf(4.345);
```

---

## 🧾 Payslip Generation (Upcoming)

Planned `PayrollService` will use this data to generate PDF payslips.

**Workflow**

1. Aggregate monthly jobs and attendance
2. Compute gross/net salary
3. Generate PDF (e.g., JasperReports)
4. Email to employer and employee

---

## 🌎 Localization

Supports **Spanish (Argentina)** for:

- Month names (`Enero`, `Febrero`, …)
- Currency/number formatting
- Validation messages

```java
String jobMonth = StringUtils.capitalize(
    getMonthNameInSpanish(LocalDate.of(year, month, 1))
);
```

---

## 🧠 Business Rules in Reporting

| Rule                                                  | Description |
| ----------------------------------------------------- | ----------- |
| Jobs must belong to active contracts                  | —           |
| Only jobs within the selected month/year are included | —           |
| Jobs are sorted chronologically                       | —           |
| Rounding scale = 2, mode = HALF_UP                    | —           |
| Averages use monthly-to-weekly factor 4.345           | —           |
| Deductions handled separately in payroll              | —           |

---

## 🪄 Example User Flow

```
Employer → Select contract → Choose month
   ↓
Controller → getMonthlyJobsByContract()
   ↓
Service → filters, computes, maps DTO
   ↓
Thymeleaf → renders report
   ↓
User → views, prints, or exports
```

---

## 🔮 Future Enhancements

- Filter by date range
- Export to PDF/Excel
- Include attendance-based deductions
- Global admin dashboard
- REST API endpoints for mobile clients

---

### 👨‍💻 Maintainer

**Matías Tailler**
Full Stack Developer — Java · Spring Boot · React · Node · Nest.js
📧 [matiastailler@gmail.com](mailto:matiastailler@gmail.com)
🌐 [GitHub](https://github.com/matiast86)
````
