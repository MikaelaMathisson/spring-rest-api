package com.example.springrestapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtTokenGenerator {

    private static final String SECRET_KEY = "common-secret-key";
    private static final String ISSUER = "my-app";

    public static String generateUserToken(String userEmail) {
        return generateToken(userEmail, "ROLE_USER");
    }

    public static String generateAdminToken(String adminEmail) {
        return generateToken(adminEmail, "ROLE_ADMIN");
    }

    private static String generateToken(String subject, String role) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(subject)
                .withClaim("roles", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 hour expiration
                .sign(algorithm);
    }

    public static void main(String[] args) {
        String userToken = generateUserToken("user@mydomain.com");
        String adminToken = generateAdminToken("admin@mydomain.com");
        System.out.println("Generated User JWT Token: " + userToken);
        System.out.println("Generated Admin JWT Token: " + adminToken);
    }
}