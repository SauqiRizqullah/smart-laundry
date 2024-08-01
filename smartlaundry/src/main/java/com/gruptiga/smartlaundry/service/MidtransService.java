package com.gruptiga.smartlaundry.service;

import java.io.IOException;
import java.util.Map;

public interface MidtransService {
    Map<String, Object> createTransaction(Map<String, Object> transactionRequest) throws IOException;
    Map<String, Object> getTransactionStatus(String orderId) throws IOException;
}
