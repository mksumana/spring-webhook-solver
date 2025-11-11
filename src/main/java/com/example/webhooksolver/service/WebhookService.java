package com.example.webhooksolver.service;

import com.example.webhooksolver.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class WebhookService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${hiring.generate.url}")
    private String generateUrl;

    @Value("${hiring.submit.url}")
    private String submitUrl;

    @Value("${app.person.name}")
    private String name;

    @Value("${app.person.regNo}")
    private String regNo;

    @Value("${app.person.email}")
    private String email;

    @Value("${app.output.finalQueryFile}")
    private String finalQueryFile;

    private static final String Q1 = "https://drive.google.com/file/d/1IeSI6l6KoSQAFfRihIT9tEDICtoz-G/view?usp=sharing";
    private static final String Q2 = "https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view?usp=sharing";

    public void executeStartupFlow() {
        System.out.println("🚀 Starting Webhook Solver Application...");
        try {
            // Step 1: Generate webhook and token
            GenerateWebhookRequest req = new GenerateWebhookRequest(name, regNo, email);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<GenerateWebhookResponse> response = restTemplate.exchange(
                    generateUrl, HttpMethod.POST, new HttpEntity<>(req, headers), GenerateWebhookResponse.class
            );

            GenerateWebhookResponse body = response.getBody();
            if (body == null || body.getWebhook() == null || body.getAccessToken() == null) {
                System.err.println("❌ Invalid response from generateWebhook API!");
                return;
            }

            String webhookUrl = body.getWebhook();
            String accessToken = body.getAccessToken();

            System.out.println("✅ Webhook generated: " + webhookUrl);
            System.out.println("🔐 Access token received.");

            // Step 2: Determine SQL question
            int lastTwoDigits = extractLastTwoDigitsFromRegNo(regNo);
            boolean isOdd = (lastTwoDigits % 2) == 1;
            String questionUrl = isOdd ? Q1 : Q2;
            System.out.println("📘 Selected question: " + (isOdd ? "Question 1" : "Question 2"));
            System.out.println("🔗 Question URL: " + questionUrl);

            // Step 3: Solve SQL question (replace with real solution)
            String finalSql = solveSQLQuestion(isOdd);

            // Step 4: Save SQL query locally
            saveFinalQueryToFile(finalSql);

            // Step 5: Submit final query with JWT header
            sendFinalQueryToWebhook(webhookUrl, accessToken, finalSql);

            System.out.println("✅ Completed full workflow successfully!");

        } catch (Exception e) {
            System.err.println("⚠️ Error occurred during flow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int extractLastTwoDigitsFromRegNo(String r) {
        String digits = r.replaceAll("\\D+", "");
        if (digits.isEmpty()) return 0;
        return Integer.parseInt(digits.substring(Math.max(0, digits.length() - 2)));
    }

    private String solveSQLQuestion(boolean isOdd) {
        // 👇 Replace with your actual SQL queries once you read the questions
        if (isOdd) {
            return """
                SELECT department_id, COUNT(employee_id) AS total_employees
                FROM employees
                GROUP BY department_id
                HAVING COUNT(employee_id) > 5
                ORDER BY total_employees DESC;
            """;
        } else {
            return """
                SELECT customer_id, SUM(order_amount) AS total_spent
                FROM orders
                WHERE order_date >= '2024-01-01'
                GROUP BY customer_id
                ORDER BY total_spent DESC;
            """;
        }
    }

    private void saveFinalQueryToFile(String sql) {
        try (FileWriter fw = new FileWriter(new File(finalQueryFile))) {
            fw.write(sql);
            System.out.println("💾 Final SQL saved to: " + finalQueryFile);
        } catch (IOException e) {
            System.err.println("❌ Failed to save SQL: " + e.getMessage());
        }
    }

    private void sendFinalQueryToWebhook(String webhookUrl, String token, String sql) {
        FinalQueryRequest req = new FinalQueryRequest(sql);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token); // direct token as per problem

        try {
            ResponseEntity<String> res = restTemplate.exchange(webhookUrl, HttpMethod.POST, new HttpEntity<>(req, headers), String.class);
            System.out.println("📡 Submission Response: " + res.getStatusCodeValue() + " - " + res.getBody());
        } catch (Exception e) {
            System.err.println("❌ Submission failed: " + e.getMessage());
        }
    }
}
