package io.pivotal.microservices.pact.consumer;

import com.alibaba.fastjson.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderHandler {




    private String backendURL;

    public ProviderHandler(String backendURL) {
        this.backendURL = backendURL;
    }


    public void getInformation(String path, Object body, String method, Map<String, List<String>> query) {

        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<String> responseType = new ParameterizedTypeReference<String>() {
        };

        HashMap<String, String> map = new HashMap<>();
        for (String s : query.keySet()) {
            try {
                map.put(s,query.get(s).get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(body), headers);

        ResponseEntity<String> exchange = restTemplate.exchange(backendURL + "/" + path+ mapToQueryString(map), HttpMethod.resolve(method), entity, responseType, map);
        System.out.println("===================" + exchange.getBody());
    }


    private String mapToQueryString(Map<String, String> map) {
        StringBuilder string = new StringBuilder();

        if(map.size() > 0) {
            string.append("?");
        }

        for(Map.Entry<String, String> entry : map.entrySet()) {
            string.append(entry.getKey());
            string.append("=");
            string.append(entry.getValue());
            string.append("&");
        }

        return string.toString();
    }

}
