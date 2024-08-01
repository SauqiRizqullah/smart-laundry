package com.gruptiga.smartlaundry.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gruptiga.smartlaundry.dto.response.JwtClaims;
import com.gruptiga.smartlaundry.entity.Account;
import com.gruptiga.smartlaundry.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final String JWT_SECRET;
    private final String ISSUER;
    private final long JWT_EXPIRATION;

    public JwtServiceImpl(
            @Value("${smartlaundry.jwt.secret_key}") String jwtSecret,
            @Value("${smartlaundry.jwt.issuer}") String issuer,
            @Value("${smartlaundry.jwt.expirationInSecond}") long expiration
    ) {
        JWT_SECRET = jwtSecret;
        ISSUER = issuer;
        JWT_EXPIRATION = expiration;
    }

    @Override
    public String generateToken(Account userAccount) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            return JWT.create()
                    .withSubject(userAccount.getAccountId())
                    .withIssuedAt(Instant.now())
                    .withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION))
                    .withIssuer(ISSUER)
                    .sign(algorithm);
        }	catch (JWTCreationException exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error while creating jwt token");
        }
    }

    @Override
    public boolean verifyJwtToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            String token = parseJwt(bearerToken);
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception){
            log.error("Invalid JWT Signature/Claims : {}", exception.getMessage());
            return false;
        }
    }

    @Override
    public JwtClaims getClaimsByToken(String bearerToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();

            DecodedJWT decodedJWT = verifier.verify(parseJwt(bearerToken));
            return JwtClaims.builder()
                    .userAccountId(decodedJWT.getSubject())
                    .build();
        } catch (JWTVerificationException exception){
            log.error("Invalid JWT Signature/Claims : {}", exception.getMessage());
            return null;
        }
    }

    private String parseJwt(String token) {
        if(token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}


