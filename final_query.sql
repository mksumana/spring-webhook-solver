    SELECT department_id, COUNT(employee_id) AS total_employees
    FROM employees
    GROUP BY department_id
    HAVING COUNT(employee_id) > 5
    ORDER BY total_employees DESC;
