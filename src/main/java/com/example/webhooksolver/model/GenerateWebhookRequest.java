package com.example.webhooksolver.model;
public class GenerateWebhookRequest {
    private String name, regNo, email;
    public GenerateWebhookRequest() {}
    public GenerateWebhookRequest(String n,String r,String e){name=n;regNo=r;email=e;}
    public String getName(){return name;} public void setName(String n){name=n;}
    public String getRegNo(){return regNo;} public void setRegNo(String r){regNo=r;}
    public String getEmail(){return email;} public void setEmail(String e){email=e;}
}