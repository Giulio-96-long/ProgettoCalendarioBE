package com.example.demo.authenticationToken;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JwtUtil {
   
	private final SecretKey jwtSecret;
    private final long jwtExpirationMs = 3600000;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.jwtSecret = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

	   public String generateToken(String email) {

	        // Usa Instant per ottenere il timestamp corrente
	        Instant now = Instant.now();

	        // Aggiungi la durata dell'expiration in millisecondi
	        Instant expiration = now.plus(jwtExpirationMs, ChronoUnit.MILLIS);

	        // Converti Instant in Date per l'uso con JWT
	        Date issuedAt = Date.from(now);
	        Date expirationDate = Date.from(expiration);

	        // Costruisci il token JWT
	        var token = Jwts.builder()
	                .setSubject(email)
	                .setIssuedAt(issuedAt)
	                .setExpiration(expirationDate)
	                .signWith(jwtSecret)
	                .compact();

	        return token;
	    }

    public String extractEmail(String token) {
     
        Claims claims = Jwts.parserBuilder()  // Usa parserBuilder
                .setSigningKey(jwtSecret)            // Imposta la chiave sicura
                .build()                       // Crea il parser
                .parseClaimsJws(token)         // Decodifica il token
                .getBody();                    // Ottieni il corpo del JWT

        return claims.getSubject();            // Restituisci l'email (o il subject)
    }
    
    public boolean validateToken(String token) {
        try {
          
            Jwts.parserBuilder()              // Usa parserBuilder
                .setSigningKey(jwtSecret)           // Imposta la chiave sicura
                .build()                      // Costruisci il parser
                .parseClaimsJws(token);       // Analizza il token

            return true;                      // Se il parsing è riuscito, il token è valido
        } catch (JwtException e) {
            return false;                     // Se il parsing fallisce, il token non è valido
        }
    }
}