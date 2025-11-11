package com.example.webhooksolver.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateWebhookResponse {
    private String webhook, accessToken;
    public String getWebhook(){return webhook;} public void setWebhook(String w){webhook=w;}
    public String getAccessToken(){return accessToken;} public void setAccessToken(String a){accessToken=a;}
}