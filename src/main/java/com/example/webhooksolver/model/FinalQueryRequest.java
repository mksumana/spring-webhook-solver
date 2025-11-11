package com.example.webhooksolver.model;
public class FinalQueryRequest {
    private String finalQuery;
    public FinalQueryRequest(){} public FinalQueryRequest(String q){finalQuery=q;}
    public String getFinalQuery(){return finalQuery;} public void setFinalQuery(String q){finalQuery=q;}
}