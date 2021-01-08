package com.chqbook.assignment.Assignment.services;

import com.chqbook.assignment.Assignment.common.APICaller;
import com.chqbook.assignment.Assignment.common.IdGenerator;
import com.chqbook.assignment.Assignment.model.Orders;
import com.chqbook.assignment.Assignment.model.Payments;
import com.chqbook.assignment.Assignment.queries.OrderRepo;
import com.chqbook.assignment.Assignment.queries.PaymentRepo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@SuppressWarnings("rawtypes")
public class ServiceClass {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Value("${userId}")
    private String key;
    @Value("${secret}")
    private String secret;
    @Autowired
    private APICaller apiCaller;
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private PaymentRepo paymentRepo;

    public ResponseEntity<?> capturePaymentViaClient(String id) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(key, secret, true);
            JSONObject options = new JSONObject();
            options.put("amount", 1000);
            options.put("currency", "INR");
            Payment res = razorpayClient.Payments.capture(id, options);
            return ResponseEntity.status(500).body(res);
        } catch (RazorpayException e) {
            logger.error(e.getLocalizedMessage(), e);
            return ResponseEntity.status(500).body(e.getLocalizedMessage());
        }
    }

    public ResponseEntity capturePayment(String id) {
        String endpoint = "https://api.razorpay.com/v1/payments/";
        Map<String, Object> options = new HashMap<>();
        options.put("amount", 1000);
        options.put("currency", "INR");
        return apiCaller.callAPI(endpoint + id, options, "POST");
    }

    public ResponseEntity<?> makeOrder(org.json.simple.JSONObject input) {
        String endpoint = "https://api.razorpay.com/v1/orders";
        Map<String, Object> options = new HashMap<>();
        options.put("amount", input.get("amount"));
        options.put("currency", "INR");
        options.put("receipt", "txn_" + IdGenerator.getId());
        ResponseEntity<?> res = apiCaller.callAPI(endpoint, options, "POST");
        if (res.getStatusCode().is2xxSuccessful()) {
            CompletableFuture.runAsync(() -> orderRepo.save(new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).convertValue(res.getBody(), Orders.class)));
        }
        return res;
    }

    public Payment fetchPayment(String paymentId) throws RazorpayException {
        RazorpayClient razorpay = new RazorpayClient(key, secret);
        return razorpay.Payments.fetch(paymentId);
    }

    public boolean verifySignature(String razorpay_order_id, String razorpay_payment_id, String razorpay_signature) {
        try {
            if (Signature.calculateRFC2104HMAC(razorpay_order_id + "|" + razorpay_payment_id, secret).equals(razorpay_signature)) {
                Orders orderObj = orderRepo.findByOrderId(razorpay_order_id);
                Payment rzrPaymentObj = fetchPayment(razorpay_payment_id);
                orderObj.setAmount_paid(rzrPaymentObj.get("amount"));
                orderObj.setAmount_due(orderObj.getAmount().doubleValue() - ((Number) rzrPaymentObj.get("amount")).doubleValue());
                CompletableFuture.runAsync(() -> paymentRepo.save(new Payments(rzrPaymentObj.toJson())))
                        .thenRun(() -> orderRepo.save(orderObj));
                return true;
            }
            return false;
        } catch (SignatureException | RazorpayException e) {
            logger.error(e.getLocalizedMessage(), e);
            return false;
        }
    }
}
