package com.techphantomexample.OrderReciever.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTGeneratorTest {

    private JWTGenerator jwtGenerator;

    @Mock
    private Authentication authentication;

    private Key key;
    private static final String USERNAME = "testuser";
    private String token;

    @BeforeEach
    public void setUp() {
        key = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // Generate a key for testing
        jwtGenerator = new JWTGenerator(key);               // Inject the same key into the JWTGenerator

        token = Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000)) // 1 minute expiration
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    @Test
    public void testGenerateToken() {
        when(authentication.getName()).thenReturn(USERNAME);

        String generatedToken = jwtGenerator.generateToken(authentication);

        assertNotNull(generatedToken);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(generatedToken)
                .getBody();

        assertEquals(USERNAME, claims.getSubject());
        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiration());
    }

    @Test
    public void testGetUsernameFromJWT() {
        String username = jwtGenerator.getUsernameFromJWT(token);
        assertEquals(USERNAME, username);
    }

    @Test
    public void testValidateToken_ValidToken() {
        assertTrue(jwtGenerator.validateToken(token));
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String invalidToken = token.substring(1); // invalidate the token

        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            jwtGenerator.validateToken(invalidToken);
        });
    }

    @Test
    public void testValidateToken_ExpiredToken() {
        String expiredToken = Jwts.builder()
                .setSubject(USERNAME)
                .setIssuedAt(new Date(System.currentTimeMillis() - 60000)) // issued 1 minute ago
                .setExpiration(new Date(System.currentTimeMillis() - 30000)) // expired 30 seconds ago
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            jwtGenerator.validateToken(expiredToken);
        });
    }
}
