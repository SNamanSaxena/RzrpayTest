package com.chqbook.assignment.Assignment.controller;

import com.chqbook.assignment.Assignment.common.Constants;
import com.chqbook.assignment.Assignment.services.ServiceClass;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SuppressWarnings({"rawtypes", "unchecked"})
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Autowired
    private ServiceClass service;

    //API to make an order
    @PostMapping("/api/makeOrder")
    public ResponseEntity makeOrder(@RequestBody JSONObject input) {
        return service.makeOrder(input);
    }

    //Callback API to register payment and update order details
    @PostMapping(path = "/exposed/payment/success", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ResponseEntity logPayment(String razorpay_payment_id, String razorpay_order_id, String razorpay_signature) {
        JSONObject json = new JSONObject();
        if (service.verifySignature(razorpay_order_id, razorpay_payment_id, razorpay_signature)) {
            json.put("status", Constants.success);
            json.put("isVerified", true);
        } else {
            json.put("status", Constants.failed);
            json.put("isVerified", false);
        }
        return ResponseEntity.status(200).body(json);
    }
}
