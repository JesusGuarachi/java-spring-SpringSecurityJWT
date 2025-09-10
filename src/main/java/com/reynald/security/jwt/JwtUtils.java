package com.reynald.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {
    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.time.expiration}")
    private String timeExpiration;

    public String generateAccessToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
                .signWith(getSignatureKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignatureKey()) // clave de verificación
                    .build()
                    .parseSignedClaims(token) // ✅ parsea JWT firmado
                    .getPayload();
           return Boolean.TRUE;
        }catch (Exception e){
            log.error("Token invalido, error".concat(e.getMessage()));
          return Boolean.FALSE;
        }
    }

    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }
    public <T> T getClaim(String token, Function<Claims,T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims) ;

    }
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignatureKey()) // clave de verificación
                .build()
                .parseSignedClaims(token) // ✅ parsea JWT firmado
                .getPayload();
    }
    private SecretKey getSignatureKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

}
