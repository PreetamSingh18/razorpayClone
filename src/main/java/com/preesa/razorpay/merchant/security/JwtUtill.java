package com.preesa.razorpay.merchant.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtill {


    @Value("${jwt.secret-key}")
    private String secretKey;


    private SecretKey getSecretKey(){
      return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(String email, UUID merchantId, String role){
        return Jwts.builder().subject(email)
                .issuedAt(Date.from(Instant.now()))
                .claim("merchant_id", merchantId)
                .claim("role", role)
                .expiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
                .signWith(getSecretKey())
                .compact();
    }

    public Claims verifyAccessToken(String accessToken){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();
    }

    public String extractRole(Claims claims) {
       return claims.get("role",String.class);

    }

    public String extractMerchantId(Claims claims) {
        return claims.get("merchant_id",String.class);

    }
}
