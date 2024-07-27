package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping(path = APIUrl.MIDTRANS)
public class MidtransWebhookController {

    private static final Logger log = LoggerFactory.getLogger(MidtransWebhookController.class);
    private final TransactionService transactionService;
    private final RestTemplate restTemplate;

    public MidtransWebhookController(TransactionService transactionService, RestTemplate restTemplate) {
        this.transactionService = transactionService;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<String> handleMidtransWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String transactionId = (String) payload.get("order_id");
            String transactionStatus = (String) payload.get("transaction_status");

            if ("success".equalsIgnoreCase(transactionStatus) || "settlement".equalsIgnoreCase(transactionStatus)) {
                transactionService.updateStatusPembayaranDone(transactionId);
                log.info("Payment status updated for transaction ID: {}", transactionId);
                return ResponseEntity.ok("Success");
            }  else if ("success".equalsIgnoreCase(transactionStatus) || "expire".equalsIgnoreCase(transactionStatus)) {
                transactionService.updateStatusPembayaranExpired(transactionId);
                log.info("Payment status updated for transaction ID: {}", transactionId);
                return ResponseEntity.ok("Success");
            }

            else {
                log.info("Transaction status not updated: {}", transactionStatus);
                return ResponseEntity.ok("Ignored");
            }
        } catch (Exception e) {
            log.error("Error processing webhook notification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }
}

