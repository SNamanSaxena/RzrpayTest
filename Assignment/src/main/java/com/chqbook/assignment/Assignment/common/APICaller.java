package com.chqbook.assignment.Assignment.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class APICaller {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Value("${userId}")
    private String key;
    @Value("${secret}")
    private String secret;
    @Autowired
    private RestTemplate restTemplate;

    //Module to call APIs and handle exceptions
    public ResponseEntity<?> callAPI(String endpoint, Object body, String requestType) {
        ResponseEntity<?> res;
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> request;
            if (requestType.equals("POST")) {
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBasicAuth(key, secret);
                request = new HttpEntity<>(body, headers);
                res = restTemplate.postForEntity(endpoint, request, Object.class);
            } else {
                res = restTemplate.getForEntity(endpoint, Object.class);
            }
        } catch (RestClientResponseException e) {
            logger.error(e.getLocalizedMessage());
            return ResponseEntity.status(e.getRawStatusCode()).body(e.getResponseBodyAsString());
        }
        return res;
    }

}
