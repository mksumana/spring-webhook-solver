package com.example.webhooksolver;

import com.example.webhooksolver.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebhookSolverApplication implements ApplicationRunner {

    @Autowired
    private WebhookService webhookService;

    public static void main(String[] args) {
        SpringApplication.run(WebhookSolverApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        webhookService.executeStartupFlow();
        System.out.println("✅ Application finished execution successfully.");
    }
}
