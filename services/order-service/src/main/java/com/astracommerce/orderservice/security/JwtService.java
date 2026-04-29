package com.astracommerce.orderservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    public boolean isTokenValid(String token) {
        try { return !extractClaim(token, Claims::getExpiration).before(new Date()); }
        catch (Exception e) { return false; }
    }
    public String extractEmail(String token) { return extractClaim(token, Claims::getSubject); }
    public String extractRole(String token) { return extractClaim(token, c -> c.get("role", String.class)); }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(Jwts.parser().verifyWith((javax.crypto.SecretKey) getKey()).build().parseSignedClaims(token).getPayload());
    }
    private Key getKey() { return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)); }
}
