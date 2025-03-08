package com.example.jwt_auth_server.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private JwtEncoder jwtEncoder;
    
    @Mock
    private Authentication authentication;
    
    private JwtService jwtService;
    
    @BeforeEach
    public void setup() {
        List<SimpleGrantedAuthority> authorities = 
            List.of(new SimpleGrantedAuthority("ROLE_USER"));
        
        when(authentication.getName()).thenReturn("testuser");
        doReturn(authorities).when(authentication).getAuthorities();
        
        jwtService = new JwtService(jwtEncoder);
        ReflectionTestUtils.setField(jwtService, "expiryMinutes", 60L);
        
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenAnswer(invocation -> {
            JwtEncoderParameters parameters = invocation.getArgument(0);
            Map<String, Object> claims = parameters.getClaims().getClaims();
            
            return Jwt.withTokenValue("test-token")
                    .header("alg", "RS256")
                    .subject(claims.get("sub").toString())
                    .claim("scope", claims.get("scope"))
                    .issuedAt((Instant) claims.get("iat"))
                    .expiresAt((Instant) claims.get("exp"))
                    .build();
        });
    }
    
    @Test
    public void generateToken_ShouldCreateValidJwtToken() {
        String token = jwtService.generateToken(authentication);
        
        assertNotNull(token);
        assertEquals("test-token", token);
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }
}
