package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.service.MidtransService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MidtransServiceImpl implements MidtransService {
    @Value("${midtrans.server.key}")
    private String serverKey;

    @Value("${midtrans.api.url}")
    private String apiUrl;


    @Override
    public Map<String, Object> createTransaction(Map<String, Object> transactionRequest) throws IOException {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(transactionRequest);

        RequestBody body = RequestBody.create(
                jsonRequest,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(apiUrl + "/charge")
                .post(body)
                .addHeader("Authorization", "Basic " + okhttp3.Credentials.basic(serverKey, ""))
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                return objectMapper.readValue(responseBody, Map.class);
            } else {
                String responseBody = response.body().string();
                throw new IOException("Unexpected code " + response + ": " + responseBody);
            }
        }
    }

    @Override
    public Map<String, Object> getTransactionStatus(String orderId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(apiUrl + "/" + orderId + "/status")
                .get()
                .addHeader("Authorization", "Basic " + okhttp3.Credentials.basic(serverKey, ""))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(responseBody, Map.class);
            } else {
                String responseBody = response.body().string();
                throw new IOException("Unexpected code " + response + ": " + responseBody);
            }
        }
    }
}
