package com.example.webhooksolver.service;
import com.example.webhooksolver.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.FileWriter; import java.io.File;

@Service
public class WebhookService {
    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${hiring.generate.url}") private String generateUrl;
    @Value("${hiring.submit.url}") private String submitUrl;
    @Value("${app.person.name}") private String name;
    @Value("${app.person.regNo}") private String regNo;
    @Value("${app.person.email}") private String email;
    @Value("${app.output.finalQueryFile}") private String finalQueryFile;

    private static final String Q1="https://drive.google.com/file/d/1IeSI6l6KoSQAFfRihIT9tEDICtoz-G/view?usp=sharing";
    private static final String Q2="https://drive.google.com/file/d/143MR5cLFrlNEuHzzWJ5RHnEWuijuM9X/view?usp=sharing";

    public void executeStartupFlow(){
        try{
            System.out.println("Generating webhook...");
            GenerateWebhookRequest req=new GenerateWebhookRequest(name,regNo,email);
            HttpHeaders h=new HttpHeaders(); h.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<GenerateWebhookResponse> resp=restTemplate.exchange(generateUrl,HttpMethod.POST,new HttpEntity<>(req,h),GenerateWebhookResponse.class);
            GenerateWebhookResponse body=resp.getBody();
            String wh=body.getWebhook(),token=body.getAccessToken();
            System.out.println("Webhook: "+wh);
            int last=extractLastTwoDigitsFromRegNo(regNo);
            boolean odd=(last%2)==1; String qurl=odd?Q1:Q2;
            System.out.println("Question URL: "+qurl);
            String finalSql="SELECT * FROM dummy_table;";
            saveFinalQueryToFile(finalSql);
            sendFinalQueryToWebhook(wh,token,finalSql);
        }catch(Exception e){e.printStackTrace();}
    }
    private int extractLastTwoDigitsFromRegNo(String r){String d = r.replaceAll("\\D+", "");return d.isEmpty()?0:Integer.parseInt(d.substring(Math.max(0,d.length()-2)));}
    private void saveFinalQueryToFile(String sql){try(FileWriter fw=new FileWriter(new File(finalQueryFile))){fw.write(sql);}catch(Exception e){e.printStackTrace();}}
    private void sendFinalQueryToWebhook(String wh,String token,String sql){
        FinalQueryRequest req=new FinalQueryRequest(sql);
        HttpHeaders h=new HttpHeaders(); h.setContentType(MediaType.APPLICATION_JSON); h.set("Authorization",token);
        try{ResponseEntity<String> r=restTemplate.exchange(wh,HttpMethod.POST,new HttpEntity<>(req,h),String.class);
            System.out.println("Response:"+r.getStatusCodeValue()); System.out.println(r.getBody());
        }catch(Exception e){e.printStackTrace();}
    }
}