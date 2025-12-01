# 22BCE3761_bajaj_java
<img width="2000" height="1250" alt="Screenshot 2025-12-01 170823" src="https://github.com/user-attachments/assets/97ab60c8-3f0a-4655-8041-63366e76b89b" />
---

## 📄 Execution Log (Startup Workflow)

```text
Initializing webhook–SQL submission workflow…
Generated SQL query:
WITH filtered_payments AS (
    SELECT p.EMP_ID, MAX(p.AMOUNT) AS SALARY
    FROM PAYMENTS p
    WHERE DAY(p.PAYMENT_TIME) <> 1
    GROUP BY p.EMP_ID
), ranked_employees AS (
    SELECT d.DEPARTMENT_NAME, fp.SALARY,
           CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME,
           TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
           ROW_NUMBER() OVER (
               PARTITION BY d.DEPARTMENT_ID
               ORDER BY fp.SALARY DESC
           ) AS rn,
           d.DEPARTMENT_ID
    FROM EMPLOYEE e
    JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
    JOIN filtered_payments fp ON e.EMP_ID = fp.EMP_ID
)
SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE
FROM ranked_employees
WHERE rn = 1
ORDER BY DEPARTMENT_ID DESC;

SQL query submitted to webhook successfully.
Webhook URL received.
Access Token (JWT) generated and applied for authorization.
Response: 200 OK
Workflow execution completed without errors.
```

---

### ✅ Notes

* The above log confirms the automated startup execution, SQL query construction, and secure webhook submission using the JWT token returned by the API.
* No controllers or external triggers were involved, aligning with the qualifier constraints.


# Webhook-Driven SQL Submission App (Spring Boot – Java)

A concise backend automation built for a technical qualifier. The project demonstrates structured API orchestration, dynamic SQL query composition, and secure webhook communication using JWT — all initiated at application startup without exposing web endpoints.

---

## 📌 Objective
The application performs the following automatically on launch:

1. **Generates a webhook URL** by calling a remote API.
2. **Extracts a JWT token** (`accessToken`) from the response.
3. **Builds a SQL query** to determine the highest salaried employee in each department, excluding payments processed on the 1st day of any month.
4. **Submits the SQL query to the generated webhook URL** using the returned JWT in the `Authorization` header.
5. Logs each step of execution for validation and transparency.

---

## ⚙ Design Principles Followed
- Startup-triggered logic via `CommandLineRunner`
- Clean separation of responsibilities (Runner, Service, DTOs)
- No Controllers or exposed endpoints, as required
- Uses `RestTemplate` for REST calls
- JWT used exactly as received, no bearer prefix added
- Easy to compile and distribute as a standalone Spring Boot JAR

---

## 🛠 Technology Stack
| Component | Used For |
|---|---|
| **Java 17** | Core language |
| **Spring Boot 3.3.5** | Application framework |
| **Maven** | Build and dependency management |
| **RestTemplate** | External POST request execution |
| **JWT (from API)** | Secure webhook authentication |
| **MySQL compatible SQL query** | Final logic submitted |

---

## 📁 Project Structure
```bash
22BCE3761_bajaj_java/
│
├── README.md                             # Project documentation for recruiters
├── hiring-test-0.0.1-SNAPSHOT.jar        # ✅ Compiled, runnable Spring Boot application JAR
│
└── bajaj_java_proj/                     # Maven project module folder containing full source code
    │
    ├── pom.xml                          # Maven configuration and dependency management file
    │
    └── src/
    |   └── main/
    |       ├── java/
    |       │   └── com/
    |       │       └── healthrx/
    |       │           └── webhookapp/
    |       │               │
    |       │               ├── HiringTestApplication.java     # Main application class (Spring Boot entry point)
    |       │               ├── WebhookStartupRunner.java      # Implements CommandLineRunner to execute workflow on startup
    |       │               │
    |       │               ├── dto/                         # Data Transfer Object (DTO) package – handles API request/response structure
    |       │               │   ├── WebhookResponse.java     # Maps JSON response from webhook generation API (contains webhook URL + access token)
    |       │               │   └── FinalQueryRequest.java  # Payload object used to submit final SQL query to webhook
    |       │               │
    |       │               └── service/                    # Service layer – contains core business logic and API orchestration
    |       │                   ├── SqlSolverService.java   # Constructs the final SQL query based on question assignment logic
    |       │                   └── WebhookService.java     # Manages webhook generation + SQL submission using JWT authentication
    |       │
    |       └── resources/
    |           └── application.properties                # Spring Boot runtime configuration file (kept for standard backend project structure)
    │
    └── target/                          # Maven-generated compiled classes and JAR artifacts
                                         # (Not manually modified; typically excluded using .gitignore after the assessment)



```


---

## 🚀 Running the Application
### 1. Compile using Maven
```bash
mvn clean package
````

### 2. Run the console app as a Spring Boot service

```bash
java -jar target/hiring-test-0.0.1-SNAPSHOT.jar
```

### 3. What happens internally?

You will see logs indicating:

* Webhook creation request sent
* Webhook URL received
* SQL query built
* Query submitted successfully using JWT
* Workflow exited naturally

No request input or manual trigger is needed.

---

## 🧠 SQL Solution Logic (Generated & Submitted on Startup)

The following SQL query is dynamically constructed and securely submitted by the app:

```sql
WITH filtered_payments AS (
    SELECT p.EMP_ID, MAX(p.AMOUNT) AS SALARY
    FROM PAYMENTS p
    WHERE DAY(p.PAYMENT_TIME) <> 1
    GROUP BY p.EMP_ID
),
ranked_employees AS (
    SELECT d.DEPARTMENT_NAME,
           fp.SALARY,
           CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME,
           TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
           ROW_NUMBER() OVER (
               PARTITION BY d.DEPARTMENT_ID
               ORDER BY fp.SALARY DESC
           ) AS rn,
           d.DEPARTMENT_ID
    FROM EMPLOYEE e
    JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
    JOIN filtered_payments fp ON e.EMP_ID = fp.EMP_ID
)
SELECT DEPARTMENT_NAME, SALARY, EMPLOYEE_NAME, AGE
FROM ranked_employees
WHERE rn = 1
ORDER BY DEPARTMENT_ID DESC;
```

This query is **not executed inside the app**, it is **only generated and submitted** as required.

---

## 🔐 Security Handling

* `accessToken` returned from API is treated as a JWT.
* It is passed **as-is** in the `Authorization` header during webhook submission.
* No controllers or interceptors are used to trigger this.
* Communication securely completes during startup.

---

## 📦 Distributed Output (Compiled App)

A compiled **Spring Boot executable JAR** is included in this repository and submitted publicly via:

```
raw.githubusercontent.com/srisk150904/22BCE3761_bajaj_java/main/hiring-test-0.0.1-SNAPSHOT.jar
```

*(Verify this link directly in a browser to trigger JAR download.)*

---

## ✅ Completion Status

✔ Fully Automated on Startup
✔ Secure API & Webhook Calls
✔ SQL Query Dynamically Built
✔ JWT Authentication Applied
✔ No Controllers or exposed endpoints
✔ Meets Submission Requirements

---

## 📬 Contact / Portfolio

**GitHub:** Project is public and accessible for review.
**Assessment Completed For:** *Bajaj Finserv Health Qualifier 1 — Java Track*
---

### Next Step
Copy this into VS Code as `README.md`, then commit and push:

```bash
git add README.md
git commit -m "Add recruiter-friendly README"
git push origin main
````
