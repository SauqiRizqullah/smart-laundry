//package com.gruptiga.smartlaundry.config;
//
//import com.gruptiga.smartlaundry.service.MidtransService;
//import com.gruptiga.smartlaundry.service.impl.MidtransServiceImpl;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MidtransConfig {
//
//    @Value("${midtrans.server.key}")
//    private String serverKey;
//
//    @Value("${midtrans.api.url}")
//    private String apiUrl;
//
//    @Bean
//    public MidtransService midtransService() {
//        return new MidtransServiceImpl(serverKey, apiUrl);
//    }
//}
//
