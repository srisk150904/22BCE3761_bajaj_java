// import org.springframework.boot.CommandLineRunner;
// import org.springframework.boot.SpringApplication;

// public class HiringTestApplication implements CommandLineRunner {

// 	public static void main(String[] args) {
// 		SpringApplication.run(HiringTestApplication.class, args);
// 	}

// 	@Override
// 	public void run(String... args) throws Exception {
// 		// Application startup logic (no-op for now)
// 	}
// }

package com.healthrx.webhookapp;

import com.healthrx.webhookapp.dto.WebhookResponse;
import com.healthrx.webhookapp.dto.FinalQueryRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class HiringTestApplication implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(HiringTestApplication.class, args);
    }

    @Override
    public void run(String... args) {

        try {
            // Step 1: Prepare request payload
            String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            String requestJson = """
                {
                    "name": "Sriram",
                    "regNo": "22bce3761",
                    "email": "your.email@example.com"
                }
            """;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

            // Step 2: Fetch webhook and access token (JWT)
            ResponseEntity<WebhookResponse> webhookResponse =
                    restTemplate.exchange(generateUrl, HttpMethod.POST, request, WebhookResponse.class);

            WebhookResponse body = webhookResponse.getBody();
            if (body == null || body.getWebhook() == null || body.getAccessToken() == null) {
                System.err.println("Invalid response received from webhook generation.");
                return;
            }

            System.out.println("Webhook URL received: " + body.getWebhook());
            System.out.println("Access Token received (JWT): " + body.getAccessToken());

            // Step 3: Build final SQL query for Question 1
            String finalQuery = """
                WITH filtered_payments AS (
                    SELECT p.EMP_ID, SUM(p.AMOUNT) AS total_salary
                    FROM PAYMENTS p
                    WHERE DAY(p.PAYMENT_TIME) <> 1
                    GROUP BY p.EMP_ID
                ), ranked_employees AS (
                    SELECT d.DEPARTMENT_NAME,
                           fp.total_salary AS SALARY,
                           CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS EMPLOYEE_NAME,
                           TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
                           ROW_NUMBER() OVER (
                               PARTITION BY d.DEPARTMENT_ID
                               ORDER BY fp.total_salary DESC
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
            """;

            // Step 4: Submit SQL query to webhook URL using JWT in Authorization header
            HttpHeaders submitHeaders = new HttpHeaders();
            submitHeaders.setContentType(MediaType.APPLICATION_JSON);
            submitHeaders.set("Authorization", body.getAccessToken());

            HttpEntity<FinalQueryRequest> submitRequest =
                    new HttpEntity<>(new FinalQueryRequest(finalQuery), submitHeaders);

            ResponseEntity<String> submitResponse =
                    restTemplate.exchange(body.getWebhook(), HttpMethod.POST, submitRequest, String.class);

            System.out.println("SQL query submitted successfully. Response Code: " + submitResponse.getStatusCode());

        } catch (Exception ex) {
            System.err.println("Error occurred while executing startup workflow:");
            ex.printStackTrace();
        }
    }
}
