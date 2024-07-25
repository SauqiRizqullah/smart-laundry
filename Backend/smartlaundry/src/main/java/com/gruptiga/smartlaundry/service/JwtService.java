package com.gruptiga.smartlaundry.service;


import com.gruptiga.smartlaundry.dto.response.JwtClaims;
import com.gruptiga.smartlaundry.entity.Account;


public interface JwtService  {
    String generateToken(Account userAccount);


    boolean verifyJwtToken(String token);

    JwtClaims getClaimsByToken(String token);
}
