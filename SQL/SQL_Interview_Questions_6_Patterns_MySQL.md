# SQL Interview Questions — 6 Patterns (MySQL)

This note covers six practical SQL interview questions using **MySQL 8.0+** syntax. Examples assume the table and column names shown in each section; adjust them to match the actual schema.

## SQL 01. Top 5 Customers in the Last 6 Months

**Problem:** Find the five customers who placed the most orders during the last six months.

### Example schema

```sql
orders (
    order_id BIGINT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    order_date DATETIME NOT NULL
)
```

### Query

```sql
SELECT
    customer_id,
    COUNT(*) AS total_orders
FROM orders
WHERE order_date >= CURRENT_DATE - INTERVAL 6 MONTH
  AND order_date < CURRENT_DATE + INTERVAL 1 DAY
GROUP BY customer_id
ORDER BY total_orders DESC, customer_id ASC
LIMIT 5;
```

**How it works**
- `WHERE` filters the date range before aggregation.
- `GROUP BY customer_id` creates one group per customer.
- `COUNT(*)` counts orders in each group.
- `ORDER BY total_orders DESC` places the largest counts first.
- `LIMIT 5` returns at most five customers.
- `customer_id ASC` is a deterministic tie-breaker.

**Date-boundary note:** This query includes orders from the date six months ago through the end of today. If the requirement means the previous six calendar months excluding today, or exactly six months back from the current instant, adjust the date boundaries accordingly.

### Indexing and billion-row considerations

A possible starting index for this date-filtered aggregation is:

```sql
CREATE INDEX idx_orders_order_date_customer
    ON orders (order_date, customer_id);
```

- With a range condition on `order_date`, a B-tree index beginning with `order_date` may help locate qualifying rows. Including `customer_id` may make the index covering for this query, depending on the engine and selected columns.
- The database may still need to read and aggregate a very large number of qualifying orders and sort grouped results. An index does not automatically make a billion-row aggregation cheap.
- For a high-volume reporting workload, consider summary tables/materialized rollups (maintained by an ETL or streaming process), partitioning when operationally justified, and pre-aggregated daily customer counts.
- Verify with `EXPLAIN ANALYZE` (MySQL 8.0.18+) and representative data. Check actual rows examined, execution time, and whether the optimizer chooses the intended access path.
- Keep indexes workload-driven: additional indexes consume storage and increase write costs.

---

## SQL 02. Employees with the Top 3 Distinct Salaries in Each Department

**Problem:** Return every employee whose salary is among the three highest distinct salary levels in their department.

### Example schema

```sql
employees (
    employee_id BIGINT PRIMARY KEY,
    employee_name VARCHAR(200),
    department_id BIGINT,
    salary DECIMAL(12, 2)
)
```

### Query (MySQL 8.0+)

```sql
WITH ranked_employees AS (
    SELECT
        employee_id,
        employee_name,
        department_id,
        salary,
        DENSE_RANK() OVER (
            PARTITION BY department_id
            ORDER BY salary DESC
        ) AS salary_rank
    FROM employees
)
SELECT
    employee_id,
    employee_name,
    department_id,
    salary
FROM ranked_employees
WHERE salary_rank <= 3
ORDER BY department_id, salary DESC, employee_id;
```

**How it works**
- `PARTITION BY department_id` starts a separate ranking for each department.
- `ORDER BY salary DESC` ranks the highest salary first.
- `DENSE_RANK()` gives equal salaries the same rank and does not skip the next rank.
- `salary_rank <= 3` returns employees in the top three distinct salary levels. A department can return more than three employees when salaries are tied.

**Interview distinction:** `ROW_NUMBER()` gives each row a unique number; `RANK()` can leave gaps after ties; `DENSE_RANK()` ranks distinct salary levels without gaps.

### Index consideration

An index such as `(department_id, salary)` may help other department/salary access patterns, but the window query may still need sorting or materialization. Check the execution plan before adding it solely for this query.

---

## SQL 03. Second-Highest Distinct Salary

**Problem:** Return the second-highest distinct non-NULL salary. If it does not exist, return `NULL`.

### Query

```sql
SELECT (
    SELECT DISTINCT salary
    FROM employees
    WHERE salary IS NOT NULL
    ORDER BY salary DESC
    LIMIT 1 OFFSET 1
) AS second_highest_salary;
```

**How it works**
- `WHERE salary IS NOT NULL` excludes NULL salary values from the ranking.
- `DISTINCT` removes repeated salaries.
- `ORDER BY salary DESC` sorts from highest to lowest.
- `LIMIT 1 OFFSET 1` skips the highest distinct salary and selects the next one.
- A scalar subquery that returns no row evaluates to `NULL`, so this also handles tables with fewer than two distinct non-NULL salaries.

### Example

For salaries `[90000, 70000, 70000, 50000]`, the distinct order is `90000, 70000, 50000`; the result is `70000`.

---

## SQL 04. Monthly and Country Transaction Summary

**Problem:** For each month and country, calculate:
1. Total transactions
2. Total transaction amount
3. Approved transaction count
4. Approved transaction amount

### Example schema

```sql
transactions (
    transaction_id BIGINT PRIMARY KEY,
    transaction_date DATETIME NOT NULL,
    country VARCHAR(100),
    amount DECIMAL(14, 2),
    status VARCHAR(30)
)
```

### Query

```sql
SELECT
    DATE_FORMAT(transaction_date, '%Y-%m-01') AS transaction_month,
    country,
    COUNT(*) AS total_transactions,
    SUM(amount) AS total_amount,
    SUM(CASE WHEN status = 'approved' THEN 1 ELSE 0 END)
        AS approved_transactions,
    SUM(CASE WHEN status = 'approved' THEN amount ELSE 0 END)
        AS approved_amount
FROM transactions
GROUP BY
    DATE_FORMAT(transaction_date, '%Y-%m-01'),
    country
ORDER BY transaction_month, country;
```

**How it works**
- `DATE_FORMAT(..., '%Y-%m-01')` groups dates by calendar month in a consistent year-month label.
- `GROUP BY` produces one row for each month-country combination.
- `CASE WHEN` counts or sums only approved transactions.
- `SUM(amount)` ignores NULL amounts. If NULL amounts should be treated as zero, use `SUM(COALESCE(amount, 0))` where appropriate.

**Index consideration:** If the query is filtered by a date range in production, put a sargable range predicate on the raw `transaction_date` column and consider a B-tree index beginning with that column. Avoid applying `DATE_FORMAT()` in the `WHERE` predicate if you expect a normal index on `transaction_date` to serve the range filter.

---

## SQL 05. Managers with at Least 5 Direct Reports

**Problem:** Find managers who have five or more direct reports.

### Example schema

```sql
employees (
    employee_id BIGINT PRIMARY KEY,
    employee_name VARCHAR(200),
    manager_id BIGINT NULL
)
```

### Query

```sql
SELECT
    manager.employee_id,
    manager.employee_name,
    COUNT(report.employee_id) AS direct_report_count
FROM employees AS report
JOIN employees AS manager
    ON report.manager_id = manager.employee_id
GROUP BY
    manager.employee_id,
    manager.employee_name
HAVING COUNT(report.employee_id) >= 5
ORDER BY direct_report_count DESC, manager.employee_id;
```

**How it works**
- The table is used twice: `report` represents employees and `manager` represents their managers.
- The self-join matches each report's `manager_id` to a manager's `employee_id`.
- `COUNT(report.employee_id)` counts direct reports per manager.
- `HAVING` filters the aggregated groups to managers with at least five reports.

**WHERE vs HAVING**
- `WHERE` filters individual rows before grouping.
- `HAVING` filters groups after aggregation.

**Index consideration:** An index on `manager_id` can help the join and reporting patterns:

```sql
CREATE INDEX idx_employees_manager_id ON employees (manager_id);
```

Check whether a suitable index already exists before creating another one.

---

## SQL 06. Delete Duplicate Emails, Keeping the Smallest ID

**Problem:** For each email, keep the row with the smallest `id` and delete other rows with the same email.

### Example schema

```sql
users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(320)
)
```

The following examples assume duplicates mean exact matches under the column's MySQL collation. Case sensitivity and NULL-email handling should be decided explicitly for a real system.

### Step 1: Preview duplicate rows

MySQL 8.0+:

```sql
WITH ranked_emails AS (
    SELECT
        id,
        email,
        ROW_NUMBER() OVER (
            PARTITION BY email
            ORDER BY id
        ) AS rn
    FROM users
)
SELECT id, email, rn
FROM ranked_emails
WHERE rn > 1
ORDER BY email, id;
```

Rows with `rn = 1` are retained; rows with `rn > 1` are candidates for deletion.

### Step 2: Delete duplicates

MySQL 8.0+ supports a CTE before a DELETE statement:

```sql
WITH ranked_emails AS (
    SELECT
        id,
        ROW_NUMBER() OVER (
            PARTITION BY email
            ORDER BY id
        ) AS rn
    FROM users
)
DELETE FROM users
WHERE id IN (
    SELECT id
    FROM ranked_emails
    WHERE rn > 1
);
```

**Before running a destructive query:**
1. Preview the exact IDs to be deleted.
2. Take a backup or run in a controlled transaction after checking the table engine and operational constraints.
3. Confirm how NULL emails and case-insensitive email comparisons should be treated.
4. Run this during an appropriate maintenance window if the table is large.
5. After cleanup, consider preventing future duplicates with a unique constraint, if the business rules and NULL behavior allow it.

### Alternative using a self-join

Preview duplicates with the larger ID:

```sql
SELECT duplicate.id, duplicate.email
FROM users AS duplicate
JOIN users AS keeper
    ON duplicate.email = keeper.email
   AND duplicate.id > keeper.id;
```

This identifies rows whose email matches a row with a smaller ID. Note that `NULL = NULL` is not true in SQL, so this join does not group NULL emails together; use explicit NULL-safe logic if that is the desired policy.

---

## Quick Revision Table

| Question | Main concepts |
|---|---|
| 01. Top 5 customers in the last 6 months | `WHERE`, `GROUP BY`, `COUNT`, `ORDER BY`, `LIMIT`, B-tree indexing |
| 02. Top 3 distinct salaries per department | CTE, `DENSE_RANK()`, `PARTITION BY` |
| 03. Second-highest salary | `DISTINCT`, `ORDER BY`, `LIMIT/OFFSET`, scalar subquery, `NULL` |
| 04. Monthly/country transaction summary | `CASE WHEN`, `SUM`, `COUNT`, `GROUP BY`, `DATE_FORMAT()` |
| 05. Managers with at least 5 direct reports | Self-join, `GROUP BY`, `HAVING`, `COUNT` |
| 06. Delete duplicate emails | `ROW_NUMBER()`, self-join, deduplication, safe deletes |

## Interview Practice Plan

1. Write each query from memory.
2. Explain what each clause does.
3. Dry-run the query on a tiny sample table.
4. Discuss NULLs, ties, duplicate values, and date boundaries.
5. For performance questions, explain the likely index and validate it with `EXPLAIN ANALYZE` rather than claiming an index guarantees faster execution.
