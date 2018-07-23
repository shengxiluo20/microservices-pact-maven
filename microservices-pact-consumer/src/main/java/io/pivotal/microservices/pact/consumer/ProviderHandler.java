package io.pivotal.microservices.pact.consumer;

import au.com.dius.pact.model.Request;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ProviderHandler {

    private String backendURL = "http://localhost:8080/information?name=Miku";

    public String getBackendURL() {
        return this.backendURL;
    }

    public void setBackendURL(String URLBase) {
        this.backendURL = URLBase+"/information?name=Miku";
    }
    public void setBackendURL(String URLBase, String name) {
        this.backendURL = URLBase+"/information?name="+name;
    }

    public Information getInformation() {
        RestTemplate restTemplate = new RestTemplate();
        Information information = restTemplate.getForObject(getBackendURL(), Information.class);

        return information;
    }




    /*private String backendURL;

    public ProviderHandler(String backendURL) {
        this.backendURL = backendURL;
    }

    public void getInformation(Request request) {
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(request.getBody()), headers);
        ResponseEntity<String> exchange = restTemplate.exchange(backendURL + "/" + request.getPath(), HttpMethod.resolve(request.getMethod()), entity, responseType, request.getQuery());
        System.out.println("===================" + exchange.getBody());
    }*/
}
