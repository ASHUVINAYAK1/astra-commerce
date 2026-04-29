package com.astracommerce.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**
 * JWT Authentication Filter for the API Gateway.
 *
 * This filter runs BEFORE routing to any downstream service.
 * It validates the Bearer token in the Authorization header and
 * rejects requests with missing/invalid/expired tokens.
 *
 * Public routes (bypass this filter):
 *   - POST /auth/register
 *   - POST /auth/login
 *   - GET  /actuator/**
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/register",
            "/auth/login",
            "/actuator"
    );

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String path = request.getPath().toString();

            // Skip JWT validation for public routes
            if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
                return chain.filter(exchange);
            }

            // Extract Authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return reject(exchange, "Missing Authorization header");
            }

            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return reject(exchange, "Invalid Authorization format — expected 'Bearer <token>'");
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = validateAndExtract(token);

                // Forward user info as headers to downstream services
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Auth-User-Email", claims.getSubject())
                        .header("X-Auth-User-Role", claims.get("role", String.class))
                        .build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());

            } catch (Exception e) {
                log.warn("Gateway JWT validation failed for path {}: {}", path, e.getMessage());
                return reject(exchange, "Invalid or expired token");
            }
        };
    }

    private Claims validateAndExtract(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        if (claims.getExpiration().before(new Date())) {
            throw new RuntimeException("Token has expired");
        }
        return claims;
    }

    private Mono<Void> reject(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        var buffer = response.bufferFactory()
                .wrap(("{\"error\":\"" + message + "\"}").getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public static class Config {
        // Configuration properties (extendable)
    }
}
