package com.healthrx.webhookapp.service;

import com.healthrx.webhookapp.dto.FinalQueryRequest;
import com.healthrx.webhookapp.dto.WebhookResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    public WebhookResponse generateWebhook(String name, String regNo, String email) {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        String payload = "{ \"name\": \"" + name + "\", \"regNo\": \"" + regNo + "\", \"email\": \"" + email + "\" }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<WebhookResponse> response =
                    restTemplate.exchange(url, HttpMethod.POST, requestEntity, WebhookResponse.class);
            return response.getBody();
        } catch (Exception e) {
            System.err.println("Webhook generation failed: " + e.getMessage());
            return null;
        }
    }

    public void sendFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
        FinalQueryRequest body = new FinalQueryRequest(finalQuery);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", accessToken);

        HttpEntity<FinalQueryRequest> entity = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(webhookUrl, entity, String.class);
            System.out.println("SQL query submitted to webhook successfully.");
        } catch (Exception e) {
            System.err.println("SQL submission failed: " + e.getMessage());
        }
    }
}
