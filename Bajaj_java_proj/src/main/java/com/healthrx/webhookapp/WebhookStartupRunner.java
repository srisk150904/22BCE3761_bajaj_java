package com.healthrx.webhookapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.healthrx.webhookapp.dto.WebhookResponse;
import com.healthrx.webhookapp.service.SqlSolverService;
import com.healthrx.webhookapp.service.WebhookService;

@Component
public class WebhookStartupRunner implements CommandLineRunner {

    private final WebhookService webhookService;
    private final SqlSolverService SqlSolverService;

    public WebhookStartupRunner(WebhookService webhookService, SqlSolverService SqlSolverService) {
        this.webhookService = webhookService;
        this.SqlSolverService = SqlSolverService;
    }

    @Override
    public void run(String... args) {

        System.out.println("Initializing webhook–SQL submission workflow…");

        String name = "Sriram";
        String regNo = "22bce3761";
        String email = "your.email@example.com"; // Replace before final submit

        // Call webhook generator API
        WebhookResponse response = webhookService.generateWebhook(name, regNo, email);

        if (response == null) {
            System.err.println("Failed to initialize webhook. Aborting process.");
            return;
        }

        String webhookUrl = response.getWebhook();
        String accessToken = response.getAccessToken();

        System.out.println("Webhook URL: " + webhookUrl);

        // Build SQL Query for Question 1 (Odd case)
        String finalQuery = SqlSolverService.buildFinalQuery();

        System.out.println("Generated SQL query:\n" + finalQuery);

        // Submit SQL Query to webhook
        webhookService.sendFinalQuery(webhookUrl, accessToken, finalQuery);

        System.out.println("Workflow execution completed.");
    }
}
