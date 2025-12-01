# 22BCE3761_bajaj_java
<img width="1919" height="1199" alt="Screenshot 2025-12-01 170823" src="https://github.com/user-attachments/assets/97ab60c8-3f0a-4655-8041-63366e76b89b" />
Initializing webhook–SQL submission workflow…
Generated SQL query:
WITH filtered_payments AS (    SELECT p.EMP_ID, MAX(p.AMOUNT) AS SALARY     FROM PAYMENTS p     WHERE DAY(p.PAYMENT_TIME) <> 1     GROUP BY p.EMP_ID), ranked_employees AS (    SELECT d.DEPARTMENT_NAME, fp.SALARY,            CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME,            TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,            ROW_NUMBER() OVER (PARTITION BY d.DEPARTMENT_ID ORDER BY fp.SALARY DESC) AS rn,            d.DEPARTMENT_ID     FROM EMPLOYEE e     JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID     JOIN filtered_payments fp ON e.EMP_ID = fp.EMP_ID ) SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE FROM ranked_employees WHERE rn = 1 ORDER BY DEPARTMENT_ID DESC;
SQL query submitted to webhook successfully.
Workflow execution completed.
Webhook URL received: https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA
Access Token received (JWT): eyJhbGciOiJIUzI1NiJ9.eyJyZWdObyI6IjIyYmNlMzc2MSIsIm5hbWUiOiJTcmlyYW0iLCJlbWFpbCI6InlvdXIuZW1haWxAZXhhbXBsZS5jb20iLCJzdWIiOiJ3ZWJob29rLXVzZXIiLCJpYXQiOjE3NjQ1ODg5ODcsImV4cCI6MTc2NDU4OTg4N30.kogMdF2Rz6h-f1k_eOi52xG92wlJAuMDHw76SuRDAKs
SQL query submitted successfully. Response Code: 200 OK
